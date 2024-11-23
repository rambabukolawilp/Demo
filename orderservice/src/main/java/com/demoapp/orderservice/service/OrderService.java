package com.demoapp.orderservice.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.stereotype.Service;

import com.demoapp.orderservice.dto.OrderItemDto;
import com.demoapp.orderservice.dto.OrderRequestDto;
import com.demoapp.orderservice.dto.OrderResponseDto;
import com.demoapp.orderservice.entity.OrderEntity;
import com.demoapp.orderservice.entity.OrderItemEntity;
import com.demoapp.orderservice.repository.OrderRepository;
import com.demoapp.orderservice.types.OrderStatus;

@Service
public class OrderService {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public OrderService(OrderRepository orderRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    public OrderResponseDto saveOrder(OrderRequestDto orderRequestDto) {
    	logger.info("saving order to db table");
    	
    	List<OrderItemDto> orderItemsDto = orderRequestDto.getItems();
        OrderEntity orderEntity = modelMapper.map(orderRequestDto, OrderEntity.class);
    	
    	List<OrderItemEntity> orderItemEntities = orderItemsDto.stream()
    				.map(itemDto -> modelMapper.map(itemDto, OrderItemEntity.class))
    				.toList();
     
    	orderItemEntities.stream().forEach(orderItemEntity -> orderItemEntity.setOrder(orderEntity));
        
        Double totalCost = orderRequestDto.getItems().stream()
        						  .mapToDouble(item-> item.getPrice() * item.getQuantity())
        						  .sum();
        
        orderEntity.setTotalAmount(totalCost);
        orderEntity.setOrderStatus(OrderStatus.ACCEPTED.name());
        orderEntity.setCreatedAt(new Date());
        orderEntity.setItems(orderItemEntities);
    	logger.info("order totalCost: {}",totalCost);

        OrderEntity savedOrder = orderRepository.save(orderEntity);
        return modelMapper.map(savedOrder, OrderResponseDto.class);
    }

    public OrderResponseDto getOrderById(Long id) {
    	logger.info("getting order from db table");
        Optional<OrderEntity> orderEntityOptional =  orderRepository.findById(id);
        
        if(orderEntityOptional.isPresent()) {
        	logger.info("order found from db table");
        	OrderResponseDto orderResponseDto = modelMapper.map(orderEntityOptional.get(), OrderResponseDto.class);
        	return orderResponseDto;
        }
        
    	logger.info("order not found from db table for Id: {}",id);
        return null;
    }
}