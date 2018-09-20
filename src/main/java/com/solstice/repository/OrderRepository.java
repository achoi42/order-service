package com.solstice.repository;

import com.solstice.model.domain.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
//  @Query("SELECT o FROM Order o WHERE o.orderAccountId = :accountId")
  List<Order> findOrdersByOrderAccountId(@Param("accountId") long id);
//  ORDER BY o.orderDate ASC

  List<Order> findByOrderAccountIdOrderByOrderDateAsc(long accountId);
}
