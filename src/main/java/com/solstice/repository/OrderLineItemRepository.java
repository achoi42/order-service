package com.solstice.repository;

import com.solstice.model.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

  @Modifying
  @Query("UPDATE OrderLineItem o SET "
      + "o.lineItemProductId = :productId, "
      + "o.quantity = :quantity, "
      + "o.lineItemPrice = :price, "
      + "o.lineItemShipmentId = :shipmentId "
      + "WHERE o.lineItemId = :lineItemId")
  @Transactional
  void updateLineItem(
      @Param("productId") long productId,
      @Param("quantity") int quantity,
      @Param("price") double price,
      @Param("shipmentId") long shipmentId,
      @Param("lineItemId") long lineItemId
  );
}
