package com.doit.wheels.services;

import com.doit.wheels.dao.entities.Order;
import com.doit.wheels.utils.AccessLevelType;
import com.doit.wheels.utils.exceptions.NoPermissionsException;

public interface OrderService extends GenericService<Order>{
    void deleteOrder(Order order) throws NoPermissionsException;

    boolean checkIfCurrentUserHasPermissions(AccessLevelType accessLevelType);
}
