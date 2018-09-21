package com.solstice.model.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "lineItemId", scope = Long.class)
public class OrderLineItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long lineItemId;

  private long lineItemProductId;
  private int quantity;
  private double lineItemPrice;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order lineItemOrder;

  private long lineItemShipmentId;

  @Transient
  private Logger logger = LoggerFactory.getLogger(getClass());

  public OrderLineItem() {
  }

  public OrderLineItem(long lineItemProductId, int quantity, double lineItemPrice,
      Order lineItemOrder, long lineItemShipmentId) {
    this.lineItemProductId = lineItemProductId;
    this.quantity = quantity;
    this.lineItemPrice = lineItemPrice;
    this.lineItemOrder = lineItemOrder;
    if(lineItemOrder != null) {
      lineItemOrder.addOrderLineItem(this);
    }
    this.lineItemShipmentId = lineItemShipmentId;
  }

  public long getLineItemId() {
    return lineItemId;
  }

  public void setLineItemId(long lineItemId) {
    this.lineItemId = lineItemId;
  }

  public long getLineItemProductId() {
    return lineItemProductId;
  }

  public void setLineItemProductId(long lineItemProductId) {
    this.lineItemProductId = lineItemProductId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public double getLineItemPrice() {
    return lineItemPrice;
  }

  public void setLineItemPrice(double lineItemPrice) {
    this.lineItemPrice = lineItemPrice;
  }

  public Order getLineItemOrder() {
    return lineItemOrder;
  }

  public void setLineItemOrder(Order lineItemOrder) {
    this.lineItemOrder = lineItemOrder;
    if(this.lineItemOrder == null) {
      logger.warn("Setting null value for line item " + this.lineItemId + "'s order");
      return;
    }
    if(this.lineItemOrder.getOrderLineItems() == null) {
      logger.warn("Passed order " + lineItemOrder.getOrderId() + " with null address list");
    }
    if(this.lineItemOrder.getOrderLineItems() != null &&
        this.lineItemOrder.getOrderLineItems().stream().filter(i -> i.getLineItemId() == this.lineItemId).collect(Collectors.toList()).size() == 0) {
      this.lineItemOrder.addOrderLineItem(this);
      logger.info("Successfully added order " + lineItemOrder.getOrderId() + " to line item " + this.lineItemId);
    }
  }

  public long getLineItemShipmentId() {
    return lineItemShipmentId;
  }

  public void setLineItemShipmentId(long lineItemShipmentId) {
    this.lineItemShipmentId = lineItemShipmentId;
  }
}
