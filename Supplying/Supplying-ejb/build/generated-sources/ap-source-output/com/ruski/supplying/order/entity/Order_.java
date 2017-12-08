package com.ruski.supplying.order.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-12-05T22:36:13")
@StaticMetamodel(Order.class)
public class Order_ { 

    public static volatile SingularAttribute<Order, Integer> volume;
    public static volatile SingularAttribute<Order, Long> clientId;
    public static volatile SingularAttribute<Order, Boolean> deleted;
    public static volatile SingularAttribute<Order, Integer> servicePointId;
    public static volatile SingularAttribute<Order, Date> dateFrom;
    public static volatile SingularAttribute<Order, Integer> billingCloseday;

}