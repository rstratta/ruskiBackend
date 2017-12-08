package com.ruski.mediator.actuator;

/**
 *
 * @author Luis
 */
public class ActuatorCommandDto {    
    private Commands command;
    private String jarToExecute;
    private String classToExecute;
    private String methodToExecute;
    private String methodParam;
    

    public ActuatorCommandDto(Commands command) {        
        this.command = command;
        this.jarToExecute = "";
        this.classToExecute = "";
        this.methodToExecute = "";
        this.methodParam = "";
    }

    public String getJarToExecute() {
        return jarToExecute;
    }

    public void setJarToExecute(String jarToExecute) {
        this.jarToExecute = jarToExecute;
    }

    public String getMethodParam() {
        return methodParam;
    }

    public void setMethodParam(String methodParam) {
        this.methodParam = methodParam;
    }

    public Commands getCommand() {
        return command;
    }

    public void setCommand(Commands command) {
        this.command = command;
    }

    public String getClassToExecute() {
        return classToExecute;
    }

    public void setClassToExecute(String classToExecute) {
        this.classToExecute = classToExecute;
    }

    public String getMethodToExecute() {
        return methodToExecute;
    }

    public void setMethodToExecute(String methodToExecute) {
        this.methodToExecute = methodToExecute;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ActuatorCommandDto other = (ActuatorCommandDto) obj;
        if (this.command != other.command) {
            return false;
        }
        return true;
    }
    
    
}
