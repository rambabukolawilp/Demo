package com.demoapp.orderservice.dto;

import java.util.List;

public class OrderRequestDto {
	
	private Long userId;
	private List<OrderItemDto> items;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<OrderItemDto> getItems() {
		return items;
	}

	public void setItems(List<OrderItemDto> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "OrderRequestDto [userId=" + userId + ", items=" + items + "]";
	}
	
	

}