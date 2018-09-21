package com.solstice.model.info;

import org.springframework.beans.factory.annotation.Autowired;

public class OrderLineItemInfo {

  private long lineItemShipmentId;
  private String productName;
  private int quantity;

  public OrderLineItemInfo() {

  }

  @Autowired
  public OrderLineItemInfo(long lineItemShipmentId, String productName, int quantity) {
    this.lineItemShipmentId = lineItemShipmentId;
    this.productName = productName;
    this.quantity = quantity;
  }

  public long getLineItemShipmentId() {
    return lineItemShipmentId;
  }

  public void setLineItemShipmentId(long lineItemShipmentId) {
    this.lineItemShipmentId = lineItemShipmentId;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
