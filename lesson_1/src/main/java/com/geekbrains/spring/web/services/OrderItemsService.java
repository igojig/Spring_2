package com.geekbrains.spring.web.services;

import com.geekbrains.spring.web.repositories.OrderItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemsService {

    private final OrderItemsRepository orderItemsRepository;

//    OrderItems saveNew(User user, Order order, List<OrderItemDto> orderItemDto){
//
//    }
}
