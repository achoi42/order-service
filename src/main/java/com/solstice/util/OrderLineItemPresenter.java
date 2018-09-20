package com.solstice.util;

import com.solstice.model.domain.OrderLineItem;
import com.solstice.model.summary.OrderLineItemSummary;
import org.springframework.stereotype.Component;

@Component
public class OrderLineItemPresenter {

  public OrderLineItemSummary present(OrderLineItem lineItem) {
    return new OrderLineItemSummary(
        lineItem.getLineItemId(),
        lineItem.getLineItemProductId(),
        lineItem.getQuantity(),
        lineItem.getLineItemPrice(),
        lineItem.getLineItemOrder().getOrderId(),
        lineItem.getLineItemShipmentId()
    );
  }
}
