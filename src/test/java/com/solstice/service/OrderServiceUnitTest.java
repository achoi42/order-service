package com.solstice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.solstice.model.domain.Order;
import com.solstice.model.domain.OrderLineItem;
import com.solstice.repository.OrderLineItemRepository;
import com.solstice.repository.OrderRepository;
import com.solstice.util.NotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class OrderServiceUnitTest {

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private OrderLineItemRepository lineItemRepository;

  @InjectMocks
  private OrderService orderService;

  @Rule
  public ExpectedException exceptionGrabber = ExpectedException.none();

  private Order order;
  private OrderLineItem lineItem;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    order = new Order();
    order.setOrderNum(999);
    order.setOrderAccountId(-1);
    order.setOrderDate(LocalDate.now());
    order.setShippingAddressId(-2);
    order.setOrderLineItems(new ArrayList<>(Collections.emptyList()));

    lineItem = new OrderLineItem();
    lineItem.setLineItemProductId(-1);
    lineItem.setQuantity(111);
    lineItem.setLineItemPrice(11.11);
    lineItem.setLineItemShipmentId(-2);
    lineItem.setLineItemOrder(order);
  }

  @Test
  public void testFetchLineItems_validOrderId_success() {
    when(orderRepository.findById(anyLong()))
        .thenReturn(Optional.of(order));

    when(orderRepository.getOne(anyLong()))
        .thenReturn(order);

    List<OrderLineItem> outcome = orderService.fetchLineItems(-1);

    assertThat(outcome.size(), is(equalTo(1)));
    assertThat(outcome.get(0).getLineItemPrice(), is(equalTo(lineItem.getLineItemPrice())));
    assertThat(outcome.get(0).getLineItemId(), is(equalTo(lineItem.getLineItemId())));
    assertThat(outcome.get(0).getLineItemOrder().getOrderId(), is(equalTo(order.getOrderId())));
  }

  @Test
  public void testFetchLineItems_validOrderIdNoLineItems_success() {
    Order noLineItems = new Order();

    when(orderRepository.findById(anyLong()))
        .thenReturn(Optional.of(noLineItems));

    when(orderRepository.getOne(anyLong()))
        .thenReturn(noLineItems);

    List<OrderLineItem> outcome = orderService.fetchLineItems(-1);

    assertThat(outcome.isEmpty(), is(true));
  }

  @Test
  public void testFetchLineItems_invalidOrderId_failure() {
    when(orderRepository.findById(anyLong()))
        .thenReturn(Optional.empty());

    exceptionGrabber.expect(NotFoundException.class);

    List<OrderLineItem> outcome = orderService.fetchLineItems(-1);

    assertThat(outcome, is(nullValue()));
  }

  @Test
  public void testAddLineItem_validOrderIdValidAddress_success() {
    OrderLineItem created = new OrderLineItem();
    created.setLineItemProductId(-4);
    created.setQuantity(444);
    created.setLineItemPrice(44.44);
    created.setLineItemShipmentId(-44);

    when(orderRepository.findById(anyLong()))
        .thenReturn(Optional.of(order));

    when(orderRepository.getOne(anyLong()))
        .thenReturn(order);

    when(lineItemRepository.save(any()))
        .thenReturn(created);

    OrderLineItem outcome = orderService.addLineItem(-1, created);

    assertThat(outcome.getLineItemProductId(), is(equalTo(created.getLineItemProductId())));
    assertThat(outcome.getLineItemOrder().getOrderId(),is(equalTo(order.getOrderId())));
  }

  @Test
  public void testAddLineItem_validOrderIdNullLineItem_failure() {
    when(orderRepository.findById(anyLong()))
        .thenReturn(Optional.of(order));

    when(orderRepository.getOne(anyLong()))
        .thenReturn(order);

    OrderLineItem outcome = orderService.addLineItem(-1, null);

    assertThat(outcome, is(nullValue()));
  }

  @Test
  public void testAddLineItem_invalidOrderId_failure() {
    when(orderRepository.findById(anyLong()))
        .thenReturn(Optional.empty());

    exceptionGrabber.expect(NotFoundException.class);

    OrderLineItem outcome = orderService.addLineItem(-1, null);

    assertThat(outcome, is(nullValue()));
  }

  @Test
  public void testUpdateLineItem_validIds_success() {
    OrderLineItem updated = new OrderLineItem();
    updated.setLineItemProductId(-3);
    updated.setQuantity(333);
    updated.setLineItemPrice(33.33);

    when(orderRepository.findById(anyLong()))
        .thenReturn(Optional.of(order));

    when(lineItemRepository.findById(anyLong()))
        .thenReturn(Optional.of(lineItem));

    when(orderRepository.getOne(anyLong()))
        .thenReturn(order);

    when(lineItemRepository.save(any()))
        .thenReturn(updated);

    OrderLineItem outcome = orderService.updateLineItem(order.getOrderId(), lineItem.getLineItemId(), updated);

    assertThat(outcome.getQuantity(), is(equalTo(updated.getQuantity())));
    assertThat(outcome.getLineItemPrice(), is(equalTo(updated.getLineItemPrice())));
    assertThat(outcome.getLineItemOrder().getOrderId(), is(equalTo(updated.getLineItemOrder().getOrderId())));
  }

  @Test
  public void testUpdateLineItem_invalidIds_failure() {
    when(orderRepository.findById(anyLong()))
        .thenReturn(Optional.empty());

    exceptionGrabber.expect(NotFoundException.class);

    OrderLineItem outcome = orderService.updateLineItem(-9999, lineItem.getLineItemId(), null);

    assertThat(outcome, is(nullValue()));
  }
}
