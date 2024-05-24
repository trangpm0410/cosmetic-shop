package baitaplon.mv.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import baitaplon.mv.entity.Order;
import baitaplon.mv.model.CartInfo;
import baitaplon.mv.model.OrderDetailInfo;
import baitaplon.mv.model.OrderInfo;
import baitaplon.mv.pagination.PaginationResult;
import baitaplon.mv.repository.OrderRepository;
import baitaplon.mv.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	OrderRepository repo;

	@Override
	public Order findOrder(String orderId) {
		return repo.findOrder(orderId);
	}

	@Override
	public OrderInfo getOrderInfo(String orderId) {

		return repo.getOrderInfo(orderId);
	}

	@Override
	public void saveOrder(CartInfo cartInfo) {
		repo.saveOrder(cartInfo);
	}

	@Override
	public PaginationResult<OrderInfo> listOrderInfo(int page, int maxResult, int maxNavigationPage) {

		return repo.listOrderInfo(page, maxResult, maxNavigationPage);
	}

	@Override
	public List<OrderDetailInfo> listOrderDetailInfos(String orderId) {

		return repo.listOrderDetailInfos(orderId);
	}

}
