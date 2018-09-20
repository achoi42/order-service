package com.solstice.model.summary;

public class OrderLineItemSummary {
  private long lineItemId;
  private long lineItemProductId;
  private int quantity;
  private double lineItemPrice;
  private long lineItemOrderId;
  private long lineItemShipmentId;

  public OrderLineItemSummary() {

  }

  public OrderLineItemSummary(long lineItemId, long lineItemProductId, int quantity,
      double lineItemPrice, long lineItemOrderId, long lineItemShipmentId) {
    this.lineItemId = lineItemId;
    this.lineItemProductId = lineItemProductId;
    this.quantity = quantity;
    this.lineItemPrice = lineItemPrice;
    this.lineItemOrderId = lineItemOrderId;
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

  public long getLineItemOrderId() {
    return lineItemOrderId;
  }

  public void setLineItemOrderId(long lineItemOrderId) {
    this.lineItemOrderId = lineItemOrderId;
  }

  public long getLineItemShipmentId() {
    return lineItemShipmentId;
  }

  public void setLineItemShipmentId(long lineItemShipmentId) {
    this.lineItemShipmentId = lineItemShipmentId;
  }
}
