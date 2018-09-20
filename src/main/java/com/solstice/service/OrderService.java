package com.solstice.service;

import com.solstice.client.AccountClient;
import com.solstice.client.ProductClient;
import com.solstice.client.ShipmentClient;
import com.solstice.model.details.OrderDetails;
import com.solstice.model.details.ShipmentDetails;
import com.solstice.model.domain.Order;
import com.solstice.model.domain.OrderLineItem;
import com.solstice.model.info.AddressInfo;
import com.solstice.model.info.OrderLineItemInfo;
import com.solstice.model.info.ProductInfo;
import com.solstice.model.info.ShipmentInfo;
import com.solstice.repository.OrderLineItemRepository;
import com.solstice.repository.OrderRepository;
import com.solstice.util.BadRequestException;
import com.solstice.util.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private OrderRepository orderRepository;
  private OrderLineItemRepository lineItemRepository;
  private AccountClient accountClient;
  private ProductClient productClient;
  private ShipmentClient shipmentClient;

  private Logger logger = LoggerFactory.getLogger(getClass());

  protected OrderService() {
  }

  @Autowired
  public OrderService(OrderRepository orderRepository,
      OrderLineItemRepository lineItemRepository, AccountClient accountClient,
      ProductClient productClient, ShipmentClient shipmentClient) {
    this.orderRepository = orderRepository;
    this.lineItemRepository = lineItemRepository;
    this.accountClient = accountClient;
    this.productClient = productClient;
    this.shipmentClient = shipmentClient;
  }

  public Order addOrder(Order order) {
    if(order == null) {
      logger.error("Client attempted to create null order");
      throw new BadRequestException();
    }

    if(order.getOrderLineItems() != null && !order.getOrderLineItems().isEmpty()) {
      logger.warn("Client attempted to add references to existing line items during order creation");
    }

    return orderRepository.save(order);
  }

  public List<OrderLineItem> fetchLineItems(long orderId) {
    checkId(orderRepository, orderId);
    Order order = orderRepository.getOne(orderId);
    return order.getOrderLineItems();
  }

  public OrderLineItem fetchLineItem(long orderId, long lineId) {
    checkId(orderRepository, orderId);
    checkId(lineItemRepository, lineId);

    Order order = orderRepository.getOne(orderId);
    OrderLineItem lineItem = lineItemRepository.getOne(lineId);
    if(!order.getOrderLineItems().contains(lineItem) || lineItem.getLineItemOrder().getOrderId() != orderId) {
      logger.error("Line item {} found but does not belong to order {}", lineId, orderId);
      throw new BadRequestException();
    }
    return lineItem;
  }

  public OrderLineItem addLineItem(long orderId, OrderLineItem lineItem) {
    checkId(orderRepository, orderId);
    Order order = orderRepository.getOne(orderId);
    if(lineItem == null) {
      logger.error("Client attempted to add null line item to order " + orderId);
      return null;
    }
    OrderLineItem saved = lineItemRepository.save(lineItem);
    order.addOrderLineItem(saved);
    orderRepository.save(order);
    return saved;
  }


  public OrderLineItem updateLineItem(long orderId, long lineItemId, OrderLineItem toUpdate) {
    checkId(orderRepository, orderId);
    checkId(lineItemRepository, lineItemId);

    if(toUpdate == null) {
      logger.warn("Null request body when attempting to update line item " + lineItemId + " for order " + orderId);
      return lineItemRepository.getOne(lineItemId);
    }

    if(toUpdate.getLineItemOrder() != null && orderId != toUpdate.getLineItemOrder().getOrderId()) {
      logger.warn("Line item " + lineItemId + "'s parent order " + orderId + " does not match updated line item's order " + toUpdate.getLineItemOrder().getOrderId());
    }

    // Disassociate old line item with order
//    Order myOrder = orderRepository.getOne(orderId);
//    OrderLineItem old = lineItemRepository.getOne(lineItemId);
//    int oldIdx = myOrder.getOrderLineItems().indexOf(old);
//    myOrder.removeOrderLineItem(old);

    // Update old line item with new attributes in repository
//    lineItemRepository.updateLineItem(
//        toUpdate.getLineItemProductId(),
//        toUpdate.getQuantity(),
//        toUpdate.getLineItemPrice(),
//        toUpdate.getLineItemShipmentId(),
//        lineItemId
//    );

    // Associate updated line item with order
//    OrderLineItem updated = lineItemRepository.getOne(lineItemId);
//    myOrder.addOrderLineItem(updated);
//    orderRepository.save(myOrder);

    OrderLineItem existing = lineItemRepository.getOne(lineItemId);
//    toUpdate.setLineItemId(lineItemId);
//    toUpdate.setLineItemOrder(orderRepository.getOne(orderId));
    existing.setLineItemProductId(toUpdate.getLineItemProductId());
    existing.setQuantity(toUpdate.getQuantity());
    existing.setLineItemPrice(toUpdate.getLineItemPrice());
    existing.setLineItemShipmentId(toUpdate.getLineItemShipmentId());
    return lineItemRepository.save(existing);
  }

  public void deleteLineItem(long orderId, long lineItemId) {
    checkId(orderRepository, orderId);
    checkId(lineItemRepository, lineItemId);

    Order order = orderRepository.getOne(orderId);
    OrderLineItem toDelete = lineItemRepository.getOne(lineItemId);
    order.removeOrderLineItem(toDelete);

    lineItemRepository.deleteById(lineItemId);
  }

  public Order updateOrder(long orderId, Order toUpdate) {
    checkId(orderRepository, orderId);

    if(toUpdate == null) {
      logger.warn("Null request body when attempting to update order {}",orderId);
      throw new BadRequestException();
    }

    Order myOrder = orderRepository.getOne(orderId);

    if(myOrder.getOrderId() != toUpdate.getOrderId()) {
      logger.error("Existing order id {} does not match updated order id {}");
      throw new BadRequestException();
    }

    myOrder.setOrderLineItems(updateOrderLineItems(myOrder.getOrderLineItems(), toUpdate.getOrderLineItems(), orderId));
    return orderRepository.save(myOrder);
  }

  public List<Order> fetchAccountOrders(long accountId) {
    // Default request param, no account specified so return all orders
    if(accountId == -1) {
      return orderRepository.findAll();
    }

    return orderRepository.findByOrderAccountIdOrderByOrderDateAsc(accountId);
  }

  public OrderDetails fetchOrderDetails(long orderId) {
    checkId(orderRepository, orderId);

    Order myOrder = orderRepository.getOne(orderId);
    long accountId = myOrder.getOrderAccountId();
    long addressId = myOrder.getShippingAddressId();


    Resource<AddressInfo> fetchedAddress = accountClient.fetchAccountAddress(accountId, addressId);
    AddressInfo shippingAddress = fetchedAddress.getContent();
    logger.info("Fetched shipping address {} for order {}", shippingAddress.getAddressId(), orderId);

    List<OrderLineItemInfo> lineItemDetails = generateLineItemInfo(myOrder.getOrderLineItems());
    logger.info("Generated {} line item details for order {}", lineItemDetails.size(), orderId);
    List<ShipmentDetails> shipmentDetails = generateShipmentInfo(myOrder.getOrderLineItems());
//    logger.info("Generated {} shipment details for line item {} for order {}", shipmentDetails.size(), shipmentDetails.get(0).getLineItemIds().get(0),orderId);

    if(shippingAddress == null) {
      logger.error("Null address returned from account-service for account {} and address {}", accountId, addressId);
    }

    if(shippingAddress.getAccountId() != accountId) {
      logger.error("Address {} does not belong to account {}", addressId, accountId);
      logger.error("Address {} belongs to account {}", shippingAddress.getAddressId(),shippingAddress.getAccountId());
      throw new BadRequestException();
    }
    return generateOrderDetails(myOrder, shippingAddress, accountId, lineItemDetails, shipmentDetails);
  }

  private OrderDetails generateOrderDetails(Order order, AddressInfo addressInfo, long accountId, List<OrderLineItemInfo> lineItemInfo, List<ShipmentDetails> shipmentDetails) {
//    addressInfo.setAccountId(accountI);
    OrderDetails orderDetails = new OrderDetails(
        accountId,
        order.getOrderNum(),
        addressInfo,
        order.getTotalPrice(),
        lineItemInfo,
        shipmentDetails
    );

    return orderDetails;
  }

  public List<OrderLineItem> fetchLineItemsByShipment(long shipmentId, long accountId) {
    List<Order> accountOrders = fetchAccountOrders(accountId);
    logger.info("Fetched {} orders for account {}", accountOrders.size(), accountId);
    Order shipmentOrder;
    for(Order o: accountOrders) {
      if(o.getOrderLineItems().isEmpty()) {
        continue;
      }
      // Filter to line items with queried shipmentId
      // If filtered list is not empty then order with line items with shipmentId found
      if(!o.getOrderLineItems().stream().filter(oli -> oli.getLineItemShipmentId() == shipmentId).collect(Collectors.toList()).isEmpty()) {
        logger.info("Found list of line items for shipment {}", shipmentId);
        return o.getOrderLineItems().stream().filter(oli -> oli.getLineItemShipmentId() == shipmentId).collect(Collectors.toList());
      }

      // Remaining case - line items with shipmentId not found, check next order
    }

    throw new NotFoundException();
  }

  private AddressInfo getAccountAddress(List<AddressInfo> accountAddresses, long addressId) {
    for(AddressInfo a : accountAddresses) {
      if(a.getAddressId() == addressId) {
        logger.info("Found address with id {} from account-service", addressId);
        return a;
      }
    }

    logger.error("Shipping address {} does not exist in account-service");
    throw new NotFoundException();
  }

  private void getLineItemProductDetails() {

  }

  private void getLineItemShipmentDetails() {

  }

  private void checkId(JpaRepository repository, long id) {
    Optional checker = repository.findById(id);
    if(!checker.isPresent()) {
      logger.error("Entity with id " + id + " not found");
      throw new NotFoundException();
    }
  }

  private List<OrderLineItem> updateOrderLineItems(List<OrderLineItem> existing, List<OrderLineItem> toUpdate, long orderId) {
    // Delete exiting line items that are not in updated line item list
    for(OrderLineItem o: existing) {
      if(toUpdate.contains(o)) {
        continue;
      }
      else {
        logger.info("Existing order line item {} removed in order {} update", o.getLineItemId(),orderId);
        existing.remove(o);
        deleteLineItem(orderId,o.getLineItemId());
      }
    }

    // Add any new line items during order update
    for(OrderLineItem u: toUpdate) {
      if(existing.contains(u)) {
        continue;
      }
      else {
        logger.info("Adding order line item {} to order {} during update");
        addLineItem(orderId,u);
      }
    }

    if(!existing.containsAll(toUpdate) && !toUpdate.containsAll(existing)) {
      logger.error("Resulting list of line items does not match expected during order update");
    }

    return existing;
  }

  private List<OrderLineItemInfo> generateLineItemInfo(List<OrderLineItem> lineItems) {
    Resource<ProductInfo> fetched;
    ProductInfo productInfo;
    List<OrderLineItemInfo> lineItemDetails = lineItems.stream().map( o -> new OrderLineItemInfo(
        productClient.getOne(o.getLineItemProductId()).getContent().getProductName(),
        o.getQuantity())).collect(Collectors.toList());

    return lineItemDetails;
  }

  private List<ShipmentDetails> generateShipmentInfo(List<OrderLineItem> lineItems) {
    List<Long> uniqueShipmentIds = lineItems.stream().map(i -> i.getLineItemShipmentId()).distinct().collect(Collectors.toList());
    logger.info("Found {} unique shipment ids in line item list", uniqueShipmentIds.size());
    return uniqueShipmentIds
        .stream()
        .map(id -> {
          logger.info("Getting shipment {} info", id);
          ShipmentInfo fetched = shipmentClient.getShipment(id);
          List<OrderLineItem> shipmentLineItems = lineItems.stream().filter(i -> i.getLineItemShipmentId() == id.longValue()).collect(Collectors.toList());
          logger.info("Generated {} line item details for shipment {}", shipmentLineItems.size(), id);
          return new ShipmentDetails(
              shipmentLineItems.stream().map(i -> i.getLineItemShipmentId()).collect(Collectors.toList()),
              fetched.getShippedDate(),
              fetched.getDeliveryDate()
          );
        }).collect(Collectors.toList());
  }
}
