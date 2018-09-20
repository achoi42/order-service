package com.solstice.controller;

import com.solstice.model.domain.Order;
import com.solstice.model.domain.OrderLineItem;
import com.solstice.model.details.OrderDetails;
import com.solstice.model.summary.OrderLineItemSummary;
import com.solstice.service.OrderService;
import com.solstice.util.OrderLineItemPresenter;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

  private OrderService orderService;
  private OrderLineItemPresenter lineItemPresenter;

  private Logger logger = LoggerFactory.getLogger(getClass());

  protected OrderController() {
  }

  @Autowired
  public OrderController(OrderService orderService,
      OrderLineItemPresenter lineItemPresenter) {
    this.orderService = orderService;
    this.lineItemPresenter = lineItemPresenter;
  }

  @GetMapping("/orders")
  public List<Order> getAccountOrders(@RequestParam(value="accountId", required = false, defaultValue = "-1") long accountId) {
    return orderService.fetchAccountOrders(accountId);
  }

  @GetMapping("/orders/lines/shipments/{shipmentId}")
  public List<OrderLineItemSummary> getLineItemsByShipment(@PathVariable(value="shipmentId") long shipmentId, @RequestParam(value="accountId", required = false, defaultValue = "-1") long accountId) {
    logger.info("Request received to get line items for shipment {} for account {}", shipmentId, accountId);
    return orderService.fetchLineItemsByShipment(shipmentId, accountId).stream().map(i -> lineItemPresenter.present(i)).collect(Collectors.toList());
  }

  @PostMapping("/orders")
  public Order createOrder(@RequestBody Order order) {
    return orderService.addOrder(order);
  }

  @GetMapping("/orders/{id}")
  public OrderDetails getOrderDetails(@PathVariable(name="id") long orderId) {
    return orderService.fetchOrderDetails(orderId);
  }

  @PutMapping("/orders/{id}")
  public Order updateOrder(@PathVariable(name="id") long id, @RequestBody Order toUpdate) {
    return orderService.updateOrder(id, toUpdate);
  }

  @GetMapping("/orders/{orderId}/lines")
  public List<OrderLineItemSummary> readLineItems(@PathVariable(name="orderId") long orderId) {
    return orderService.fetchLineItems(orderId).stream().map(l -> lineItemPresenter.present(l)).collect(Collectors.toList());
  }

  @PostMapping("/orders/{orderId}/lines")
  public OrderLineItemSummary createLineItem(@PathVariable(name="orderId") long orderId, @RequestBody OrderLineItem created) {
    return lineItemPresenter.present(orderService.addLineItem(orderId, created));
  }

  @GetMapping("/orders/{orderId}/lines/{lineId}")
  public OrderLineItemSummary readLineItem(@PathVariable(name="orderId") long orderId, @PathVariable(name="lineId") long lineId) {
    return lineItemPresenter.present(orderService.fetchLineItem(orderId, lineId));
  }

  @PutMapping("/orders/{orderId}/lines/{lineId}")
  public OrderLineItem updateLineItem(
      @PathVariable(name="orderId") long orderId,
      @PathVariable(name="lineId") long lineId,
      @RequestBody OrderLineItem lineItem
      ) {
    return orderService.updateLineItem(orderId, lineId, lineItem);
  }

  @DeleteMapping("/orders/{orderId}/lines/{lineId}")
  public void deleteLineItem(@PathVariable(name="orderId") long orderId, @PathVariable(name="lineId") long lineId) {
    orderService.deleteLineItem(orderId, lineId);
  }
}
