/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.goliath.actuator.logic;

import com.ruski.goliath.actuator.entity.Actuator;
import com.ruski.goliath.actuator.entity.ActuatorCommand;
import com.ruski.goliath.actuator.repository.ActuatorCommandRepository;
import com.ruski.goliath.actuator.repository.ActuatorRepository;
import com.ruski.logger.RuskiLogger;
import com.ruski.mediator.actuator.ActuatorCommandDto;
import javax.ejb.Stateless;
import com.ruski.mediator.actuator.ActuatorDto;
import com.ruski.mediator.actuator.CommandSequenceDto;
import com.ruski.mediator.actuator.Commands;
import com.ruski.repository.RepositoryException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Luis
 */
@Stateless
@LocalBean
public class GoliathActuatorBean implements ActuatorBean {
    
    @EJB  private RuskiLogger log4jRuskiLogger;
    
    @EJB(beanName = "JpaActuatorRepository")
    private ActuatorRepository actuatorRepository;

    @EJB(beanName = "JpaCommandRepository")
    private ActuatorCommandRepository actuatorCommandRepository;

    @Override
    public void addActuator(ActuatorDto actuatorToAdd) throws ActuatorValidationException, ActuatorOperationException {
        try {
            log4jRuskiLogger.logInfo("Start add actuator" + actuatorToAdd);
            validateDto(actuatorToAdd);
            Actuator actuator = createActuatorFromDto(actuatorToAdd);
            actuator.setId(UUID.randomUUID().toString());
            actuatorRepository.addActuator(actuator);
            addCommandFromActuators(actuatorToAdd, actuator);
            log4jRuskiLogger.logInfo("Finish add actuator");
        } catch (RepositoryException ex) {
            log4jRuskiLogger.logError("Error saving actuator" + ex.getMessage());
            throw new ActuatorOperationException("Error en el repositorio: ", ex);
        } catch (NullPointerException ex) {
            log4jRuskiLogger.logError("Error getting actuator" + ex.getMessage());
            throw new ActuatorOperationException("Error al obtener el actuador: ", ex);
        }
    }

    private void addCommandFromActuators(ActuatorDto actuatorDto, Actuator actuator) throws RepositoryException {
        List<ActuatorCommandDto> commands = actuatorDto.getCommands();
        log4jRuskiLogger.logInfo("Start add commands from actuator" + actuatorDto);
        for (ActuatorCommandDto item : commands) {
            ActuatorCommand command = createCommandFromDto(item);
            command.setActuator(actuator);
            command.setId(UUID.randomUUID().toString());
            actuatorCommandRepository.addActuatorCommand(command);
        }
        log4jRuskiLogger.logInfo("Finish add commands");
    }

    @Override
    public void executeProgram(List<CommandSequenceDto> commandsToExecute) 
            throws ActuatorOperationException {
        try {
            log4jRuskiLogger.logInfo("Start execute program");
            validateProgram(commandsToExecute);
            for (CommandSequenceDto item : commandsToExecute) {
                Actuator actuator = actuatorRepository.getActuatorById(item.getActuatorId());
                List<ActuatorCommand> listFromDb = actuatorCommandRepository.getCommandsFromActuator(actuator);
                for (Commands itemCmd : item.getCommandasToExecute()) {
                    ActuatorCommand commandToExecute = getCommandConfiguration(listFromDb, itemCmd);
                    executeConcreteCommand(commandToExecute);
                }
            }
            log4jRuskiLogger.logInfo("Finish execute program");
        } catch (RepositoryException | NoSuchMethodException  
                | SecurityException | IllegalArgumentException 
                | ClassNotFoundException | InstantiationException  
                | IllegalAccessException | InvocationTargetException ex) {
            log4jRuskiLogger.logError("Error execute program" + ex.getMessage());
            throw new ActuatorOperationException("Error execute command", ex);
        }
    }

    private ActuatorCommand getCommandConfiguration(List<ActuatorCommand> listFromDb, Commands commandToExecute) {
        for (ActuatorCommand item : listFromDb) {
            if (item.getCommand().equals(commandToExecute)) {
                return item;
            }
        }
        return null;
    }

    private void executeConcreteCommand(ActuatorCommand commandToExecute) throws ClassNotFoundException, 
             NoSuchMethodException, InstantiationException, IllegalAccessException, 
                InvocationTargetException, ActuatorOperationException {
        try {
            log4jRuskiLogger.logInfo("Start execute concrete command");
            String jarFileUrl = "file:///" + commandToExecute.getJarToExecute();
            URL url = new URL(jarFileUrl);
            URL[] arrayUrl = new URL[1];
            arrayUrl[0] = url;
            URLClassLoader child = new URLClassLoader(arrayUrl, this.getClass().getClassLoader());
            Class classToLoad = Class.forName(commandToExecute.getClassToExecute(), true, child);
            
            Object instance = classToLoad.newInstance();
            if (!commandToExecute.getMethodParam().isEmpty()) {
                executeCommandWithParams(classToLoad, commandToExecute);                
            } else {
                Method method = classToLoad.getMethod(commandToExecute.getMethodToExecute());
                method.invoke(instance);
            }
            log4jRuskiLogger.logInfo("Finish execute concrete command");
        } catch (MalformedURLException | IllegalAccessException | InvocationTargetException | ParseException  ex ) {
            log4jRuskiLogger.logError("Error executing concrete command" + ex.getMessage());
            throw new ActuatorOperationException("Error executing concrete command", ex);
        }
    }

    private void executeCommandWithParams(Class classToLoad, ActuatorCommand cmd) throws ParseException, 
            NoSuchMethodException, IllegalAccessException, IllegalArgumentException, 
            InvocationTargetException, InstantiationException {
        Object[] paramToExecute = new Object[1];
        Commands command = cmd.getCommand();
        Method method = null;
        switch (command) {
            case ENABLE:
                paramToExecute[0] = getLocalDate(cmd.getMethodParam());
                method = classToLoad.getMethod(cmd.getMethodToExecute(), Date.class);
                break;
            case DISABLE:
                paramToExecute[0] = getLocalDate(cmd.getMethodParam());
                method = classToLoad.getMethod(cmd.getMethodToExecute(), Date.class);
                break;
            case SET_LEVEL:
                paramToExecute[0] = Double.parseDouble(cmd.getMethodParam());
                method = classToLoad.getMethod(cmd.getMethodToExecute(), Double.class);
                break;
            case SET_TIMER:
                paramToExecute[0] = Long.parseLong(cmd.getMethodParam());
                method = classToLoad.getMethod(cmd.getMethodToExecute(), Long.class);
                break;
            default:

        }
        Object instance = classToLoad.newInstance();
        method.invoke(instance, paramToExecute);
    }

    private Date getLocalDate(String dateStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.parse(dateStr);
    }

    private void validateProgram(List<CommandSequenceDto> commandsToValidate) throws RepositoryException {
        for (CommandSequenceDto item : commandsToValidate) {
            validateExistActuator(item.getActuatorId());
        }
    }

    private void validateExistActuator(String actuatorId) throws RepositoryException {
        actuatorRepository.getActuatorById(actuatorId);
    }

    private void validateDto(ActuatorDto dto) throws ActuatorValidationException {
        if (StringUtils.isEmpty(dto.getName())) {
            throw new ActuatorValidationException("Verifique el nombre de actuador ingresado");
        }
        List<ActuatorCommandDto> commands = dto.getCommands();
        if (!hasAllCommandsConfigrated(commands)) {
            throw new ActuatorValidationException("El actuador no tiene configurados todos sus comandos");
        }
    }

    private boolean hasAllCommandsConfigrated(List<ActuatorCommandDto> commands) {
        return commands.contains(new ActuatorCommandDto(Commands.DISABLE))
                && commands.contains(new ActuatorCommandDto(Commands.ENABLE))
                && commands.contains(new ActuatorCommandDto(Commands.SET_LEVEL))
                && commands.contains(new ActuatorCommandDto(Commands.SET_TIMER))
                && commands.contains(new ActuatorCommandDto(Commands.TURN_OFF))
                && commands.contains(new ActuatorCommandDto(Commands.TURN_ON));
    }

    private Actuator createActuatorFromDto(ActuatorDto dto) {
        Actuator actuator = new Actuator();
        actuator.setName(dto.getName());
        return actuator;
    }

    private ActuatorCommand createCommandFromDto(ActuatorCommandDto dto) {
        ActuatorCommand actuator = new ActuatorCommand();
        actuator.setJarToExecute(dto.getJarToExecute());
        actuator.setMethodParam(dto.getMethodParam());
        actuator.setClassToExecute(dto.getClassToExecute());
        actuator.setCommand(dto.getCommand());
        actuator.setMethodToExecute(dto.getMethodToExecute());
        return actuator;
    }
}
