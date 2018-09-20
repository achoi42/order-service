package com.solstice.model.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "orders")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "orderId", scope = Long.class)
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long orderId;

//  @Value("${id.default.null}")
//  private long defaultId;
  private long orderAccountId;
  private int orderNum;
  private LocalDate orderDate;
  private long shippingAddressId;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "order_id")
  private List<OrderLineItem> orderLineItems = new ArrayList<>(Collections.emptyList());

  private int totalPrice;

  @Transient
  private Logger logger = LoggerFactory.getLogger(getClass());

  public Order() {
  }

  public Order(long orderAccountId, int orderNum, LocalDate orderDate, long shippingAddressId,
      List<OrderLineItem> orderLineItems, int totalPrice) {
    this.orderAccountId = orderAccountId;
    this.orderNum = orderNum;
    this.orderDate = orderDate;
    this.shippingAddressId = shippingAddressId;
    if(orderLineItems == null) {
      this.orderLineItems = new ArrayList<>(Collections.emptyList());
    }
    else {
      this.orderLineItems = orderLineItems;
    }
    this.totalPrice = totalPrice;
  }

  public long getOrderId() {
    return orderId;
  }

  public void setOrderId(long orderId) {
    this.orderId = orderId;
  }

  public long getOrderAccountId() {
    return orderAccountId;
  }

  public void setOrderAccountId(long orderAccountId) {
    this.orderAccountId = orderAccountId;
  }

  public int getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(int orderNum) {
    this.orderNum = orderNum;
  }

  public LocalDate getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(LocalDate orderDate) {
    this.orderDate = orderDate;
  }

  public long getShippingAddressId() {
    return shippingAddressId;
  }

  public void setShippingAddressId(long shippingAddressId) {
    this.shippingAddressId = shippingAddressId;
  }

  public List<OrderLineItem> getOrderLineItems() {
    return orderLineItems;
  }

  public void setOrderLineItems(List<OrderLineItem> orderLineItems) {
    this.orderLineItems = orderLineItems;
    if(this.orderLineItems == null) {
      logger.warn("Set order " + this.orderId + " with null value for order line items");
      return;
    }
    logger.info("Set order " + this.orderId + " with " + orderLineItems.size() + " order line items");
    if(this.orderLineItems != null || !this.orderLineItems.isEmpty()) {
      for(OrderLineItem oli : this.getOrderLineItems()) {
        oli.setLineItemOrder(this);
      }
    }
  }

  public void addOrderLineItem(OrderLineItem orderLineItem) {
    if(orderLineItem == null) {
      logger.warn("Client attempted to add null order line item to order " + this.orderId);
      return;
    }
    if(!this.orderLineItems.contains(orderLineItem)) {
      this.orderLineItems.add(orderLineItem);
      logger.info("Successfully added order line item " + orderLineItem.getLineItemId());
    }

    orderLineItem.setLineItemOrder(this);
  }

  public void removeOrderLineItem(OrderLineItem orderLineItem) {
    if(orderLineItem == null) {
      logger.warn("Attempting to remove null value from order " + this.orderId + " line items");
      return;
    }
    this.orderLineItems.remove(orderLineItem);
    logger.info("Removed line item " + orderLineItem.getLineItemId() + " from order " + this.orderId);
    orderLineItem.setLineItemOrder(null);
  }

  public int getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(int totalPrice) {
    this.totalPrice = totalPrice;
  }
}
