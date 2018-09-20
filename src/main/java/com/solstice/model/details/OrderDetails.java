package com.solstice.model.details;

import com.solstice.model.info.AddressInfo;
import com.solstice.model.info.OrderLineItemInfo;
import java.util.List;

public class OrderDetails {
  private long accountId;
  private int orderNum;
  private AddressInfo shippingAddress;
  private double totalPrice;
  private List<OrderLineItemInfo> lineItems;
  private List<ShipmentDetails> shipments;

  public OrderDetails() {

  }

  public OrderDetails(long accountId, int orderNum, AddressInfo shippingAddress, double totalPrice,
      List<OrderLineItemInfo> lineItems,
      List<ShipmentDetails> shipments) {
    this.accountId = accountId;
    this.orderNum = orderNum;
    this.shippingAddress = shippingAddress;
    this.totalPrice = totalPrice;
    this.lineItems = lineItems;
    this.shipments = shipments;
  }

  public int getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(int orderNum) {
    this.orderNum = orderNum;
  }

  public AddressInfo getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(AddressInfo shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public double getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(double totalPrice) {
    this.totalPrice = totalPrice;
  }

  public long getAccountId() {
    return accountId;
  }

  public void setAccountId(long accountId) {
    this.accountId = accountId;
  }


  public List<OrderLineItemInfo> getLineItems() {
    return lineItems;
  }

  public void setLineItems(List<OrderLineItemInfo> lineItems) {
    this.lineItems = lineItems;
  }

  public List<ShipmentDetails> getShipments() {
    return shipments;
  }

  public void setShipments(List<ShipmentDetails> shipments) {
    this.shipments = shipments;
  }
}
