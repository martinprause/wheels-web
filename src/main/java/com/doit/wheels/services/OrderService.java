package com.doit.wheels.services;

import com.doit.wheels.dao.entities.Order;
import com.doit.wheels.utils.enums.AccessLevelTypeEnum;
import com.doit.wheels.utils.exceptions.NoPermissionsException;

public interface OrderService extends GenericService<Order>{
    void deleteOrder(Order order) throws NoPermissionsException;

    boolean checkIfCurrentUserHasPermissions(AccessLevelTypeEnum accessLevelTypeEnum);
}
