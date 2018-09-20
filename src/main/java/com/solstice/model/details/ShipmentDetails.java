package com.solstice.model.details;

import java.time.LocalDate;
import java.util.List;

public class ShipmentDetails {

  private List<Long> lineItemIds;
  private LocalDate shippedDate;
  private LocalDate deliveryDate;

  public ShipmentDetails() {

  }

  public ShipmentDetails(List<Long> lineItemIds, LocalDate shippedDate,
      LocalDate deliveryDate) {
    this.lineItemIds = lineItemIds;
    this.shippedDate = shippedDate;
    this.deliveryDate = deliveryDate;
  }

  public List<Long> getLineItemIds() {
    return lineItemIds;
  }

  public void setLineItemIds(List<Long> lineItemIds) {
    this.lineItemIds = lineItemIds;
  }

  public LocalDate getShippedDate() {
    return shippedDate;
  }

  public void setShippedDate(LocalDate shippedDate) {
    this.shippedDate = shippedDate;
  }

  public LocalDate getDeliveryDate() {
    return deliveryDate;
  }

  public void setDeliveryDate(LocalDate deliveryDate) {
    this.deliveryDate = deliveryDate;
  }
}
