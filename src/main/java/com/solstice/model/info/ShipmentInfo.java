package com.solstice.model.info;

import java.time.LocalDate;

public class ShipmentInfo {
  private long shipmentAccountId;
  private long shipmentAddressId;
  private LocalDate shippedDate;
  private LocalDate deliveryDate;

  public ShipmentInfo() {
  }

  public ShipmentInfo(long shipmentAccountId, long shipmentAddressId, LocalDate shippedDate,
      LocalDate deliveryDate) {
    this.shipmentAccountId = shipmentAccountId;
    this.shipmentAddressId = shipmentAddressId;
    this.shippedDate = shippedDate;
    this.deliveryDate = deliveryDate;
  }

  public long getShipmentAccountId() {
    return shipmentAccountId;
  }

  public void setShipmentAccountId(long shipmentAccountId) {
    this.shipmentAccountId = shipmentAccountId;
  }

  public long getShipmentAddressId() {
    return shipmentAddressId;
  }

  public void setShipmentAddressId(long shipmentAddressId) {
    this.shipmentAddressId = shipmentAddressId;
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
