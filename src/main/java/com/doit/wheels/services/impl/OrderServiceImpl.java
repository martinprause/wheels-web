package com.doit.wheels.services.impl;

import com.doit.wheels.dao.entities.Order;
import com.doit.wheels.dao.repositories.GenericRepository;
import com.doit.wheels.services.OrderService;
import com.doit.wheels.utils.StatusTypeEnum;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends GenericServiceImpl<Order> implements OrderService{

    public OrderServiceImpl(GenericRepository<Order> genericRepository) {
        super(genericRepository);
    }

    @Override
    public Order save(Order order) {
        if(order.getId() == null) {
            order.setStatus(StatusTypeEnum.CREATED);
        }
        return super.save(order);
    }
}
