package baitaplon.mv.repositoryimpl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import baitaplon.mv.entity.Order;
import baitaplon.mv.entity.OrderDetail;
import baitaplon.mv.entity.Product;
import baitaplon.mv.model.CartInfo;
import baitaplon.mv.model.CartLineInfo;
import baitaplon.mv.model.CustomerInfo;
import baitaplon.mv.model.OrderDetailInfo;
import baitaplon.mv.model.OrderInfo;
import baitaplon.mv.pagination.PaginationResult;
import baitaplon.mv.repository.OrderRepository;
import baitaplon.mv.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
	@Autowired
	EntityManager entityManager;
	@Autowired
	ProductRepository productRepository;

	@Override
	public Order findOrder(String orderId) {
		return entityManager.find(Order.class, orderId);
	}

	@Override
	public OrderInfo getOrderInfo(String orderId) {
		Order order = this.findOrder(orderId);
		if (order == null) {
			return null;
		}
		return new OrderInfo(order.getId(), order.getOrderDate(), order.getOrderNum(), order.getAmount(),
				order.getCustomerName(), order.getCustomerAddress(), order.getCustomerEmail(),
				order.getCustomerPhone());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveOrder(CartInfo cartInfo) {
		int orderNum = this.getMaxOrderNum() + 1;
		Order order = new Order();
		// thông tin giỏ hàng
		order.setId(UUID.randomUUID().toString());
		order.setOrderNum(orderNum);
		order.setOrderDate(new Date());
		order.setAmount(cartInfo.getAmountTotal());
		// thông tin khách hàng
		CustomerInfo customerInfo = cartInfo.getCustomerInfo();
		order.setCustomerName(customerInfo.getName());
		order.setCustomerEmail(customerInfo.getEmail());
		order.setCustomerPhone(customerInfo.getPhone());
		order.setCustomerAddress(customerInfo.getAddress());

		entityManager.persist(order);

		List<CartLineInfo> lines = cartInfo.getCartLines();
		// thông tin chi tiết giỏ hàng sản phẩm số lượng đơn giá, tiền
		for (CartLineInfo line : lines) {
			OrderDetail detail = new OrderDetail();
			detail.setId(UUID.randomUUID().toString());
			detail.setOrder(order);
			detail.setAmount(line.getAmount());
			detail.setPrice(line.getProductInfo().getPrice());
			detail.setQuanity(line.getQuantity());

			String code = line.getProductInfo().getCode();
			Product product = this.productRepository.findProduct(code);
			detail.setProduct(product);

			entityManager.persist(detail);
		}

		// Order Number!
		cartInfo.setOrderNum(orderNum);
		// Flush
		entityManager.flush();

	}

	@Override
	public PaginationResult<OrderInfo> listOrderInfo(int page, int maxResult, int maxNavigationPage) {
		String sql = "Select new " + OrderInfo.class.getName()//
				+ "(ord.id, ord.orderDate, ord.orderNum, ord.amount, "
				+ " ord.customerName, ord.customerAddress, ord.customerEmail, ord.customerPhone) " + " from "
				+ Order.class.getName() + " ord "//
				+ " order by ord.orderNum desc";
		TypedQuery<OrderInfo> typeQuery = entityManager.createQuery(sql, OrderInfo.class);
		Query<OrderInfo> query = typeQuery.unwrap(Query.class);
		return new PaginationResult<OrderInfo>(query, page, maxResult, maxNavigationPage);
	}

	@Override
	public List<OrderDetailInfo> listOrderDetailInfos(String orderId) {
		String sql = "Select new " + OrderDetailInfo.class.getName() //
				+ "(d.id, d.product.code, d.product.name , d.quanity,d.price,d.amount) "//
				+ " from " + OrderDetail.class.getName() + " d "//
				+ " where d.order.id = :orderId ";

		TypedQuery<OrderDetailInfo> query = entityManager.createQuery(sql, OrderDetailInfo.class);
		query.setParameter("orderId", orderId);

		return query.getResultList();
	}

	private int getMaxOrderNum() {
		String sql = "Select max(o.orderNum) from " + Order.class.getName() + " o ";
		TypedQuery<Integer> query = entityManager.createQuery(sql, Integer.class);
		Integer value = (Integer) query.getSingleResult();
		if (value == null) {
			return 0;
		}
		return value;
	}

}
