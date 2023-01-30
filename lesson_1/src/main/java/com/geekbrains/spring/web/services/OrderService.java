package com.geekbrains.spring.web.services;

import com.geekbrains.spring.web.dto.Cart;
import com.geekbrains.spring.web.dto.OrderItemDto;
import com.geekbrains.spring.web.entities.Order;
import com.geekbrains.spring.web.entities.OrderItems;
import com.geekbrains.spring.web.entities.Product;
import com.geekbrains.spring.web.entities.User;
import com.geekbrains.spring.web.exceptions.ResourceNotFoundException;
import com.geekbrains.spring.web.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final ProductsService productsService;

    @Transactional
    public Order saveNewOrder(String username) {
        User user =userService.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("Пользователь не найден: " + username));
        Cart cart=cartService.getCurrentCart();
        Order order=new Order();
        order.setUser(user);
        order.setTotalPrice(cart.getTotalPrice());

        List<OrderItems> orderItems=new ArrayList<>();
        List<OrderItemDto> dto=cart.getItems();
        for(OrderItemDto item: dto){
            OrderItems orderItem=new OrderItems();
            Product product=productsService.findById(item.getProductId()).orElseThrow(()->new ResourceNotFoundException("Продукт не найден: " + item.getProductId()));
            orderItem.setUser(user);
            orderItem.setProduct(product);
            orderItem.setOrder(order);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice());
            orderItem.setPricePerProduct(item.getPricePerProduct());

            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);

        order=orderRepository.save(order);

        return order;
    }
}
