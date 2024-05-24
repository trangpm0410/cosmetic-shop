package baitaplon.mv.repository;

import java.util.List;

import baitaplon.mv.entity.Order;
import baitaplon.mv.model.CartInfo;
import baitaplon.mv.model.OrderDetailInfo;
import baitaplon.mv.model.OrderInfo;
import baitaplon.mv.pagination.PaginationResult;

public interface OrderRepository {
	public Order findOrder(String orderId);

	public OrderInfo getOrderInfo(String orderId);

	public void saveOrder(CartInfo cartInfo);

	public PaginationResult<OrderInfo> listOrderInfo(int page, int maxResult, int maxNavigationPage);

	public List<OrderDetailInfo> listOrderDetailInfos(String orderId);

}
