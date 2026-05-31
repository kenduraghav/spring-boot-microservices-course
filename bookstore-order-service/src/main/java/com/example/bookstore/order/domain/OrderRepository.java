package com.example.bookstore.order.domain;

import com.example.bookstore.order.domain.models.OrderStatus;
import com.example.bookstore.order.domain.models.OrderSummaryDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByStatus(OrderStatus orderStatus);

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    default void updateOrderStatus(String orderNumber, OrderStatus orderStatus) {
        OrderEntity order = this.findByOrderNumber(orderNumber).orElseThrow();
        order.setStatus(orderStatus);
        order.setUpdatedAt(LocalDateTime.now());
        this.save(order);
    }

    @Query(
            """
    				SELECT new com.example.bookstore.order.domain.models.OrderSummaryDTO(o.orderNumber, o.status)
		FROM OrderEntity o
		WHERE o.userName = :currentUser
    		""")
    List<OrderSummaryDTO> findByUserName(String currentUser);

    @Query(
            """
    		SELECT o FROM OrderEntity o LEFT JOIN FETCH
    		o.items i WHERE o.orderNumber = :orderNumber AND o.userName = :currentUser
    		""")
    Optional<OrderEntity> findUserOrderDetails(String orderNumber, String currentUser);
}
