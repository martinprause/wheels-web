package com.doit.wheels.services.impl;

import com.doit.wheels.dao.entities.Order;
import com.doit.wheels.dao.entities.User;
import com.doit.wheels.dao.repositories.GenericRepository;
import com.doit.wheels.services.OrderService;
import com.doit.wheels.services.UserService;
import com.doit.wheels.utils.enums.AccessLevelTypeEnum;
import com.doit.wheels.utils.enums.StatusTypeEnum;
import com.doit.wheels.utils.exceptions.NoPermissionsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class OrderServiceImpl extends GenericServiceImpl<Order> implements OrderService{

    private final UserService userService;

    @Autowired
    public OrderServiceImpl(GenericRepository<Order> genericRepository, UserService userService) {
        super(genericRepository);
        this.userService = userService;
    }

    @Override
    public Order save(Order order) {
        User currentUser = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(order.getId() == null) {
            order.setStatus(StatusTypeEnum.CREATED);
            order.setCreatedByUser(currentUser);
            order.setCreated(Calendar.getInstance().getTime());
        }
        order.setLastUpdated(Calendar.getInstance().getTime());
        order.setLastUpdatedByUser(currentUser);
        return super.save(order);
    }

    @Override
    public void deleteOrder(Order order) throws NoPermissionsException {
        User currentUser = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        boolean isHasAccess = currentUser.getAccesses().stream().anyMatch(dto -> dto.getAccessLevel() == AccessLevelTypeEnum.DeleteOrder);
        if(isHasAccess)
            super.delete(order);
        else
            throw new NoPermissionsException("Permission for user + " + currentUser.getUsername() + " denied!");

    }

    @Override
    public boolean checkIfCurrentUserHasPermissions(AccessLevelTypeEnum AccessLevelTypeEnum) {
        User currentUser = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return currentUser.getAccesses().stream().anyMatch(dto -> dto.getAccessLevel() == AccessLevelTypeEnum);
    }

    @Override
    public List<Order> findAll() {
        List<Order> sortedOrders = super.findAll();
        sortedOrders.sort((o1, o2) -> (int)(Long.valueOf(o2.getOrderNo()) - Long.valueOf(o1.getOrderNo())));
        return sortedOrders;
    }
}
