package com.solstice.client;

import com.solstice.model.info.ShipmentInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("shipment-service")
@Component
public interface ShipmentClient {

  @GetMapping("/shipments/{id}")
  ShipmentInfo getShipment(@PathVariable(value = "id") long shipmentId);
}
