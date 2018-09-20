package com.solstice.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.solstice.model.domain.Order;
import java.util.List;
import java.util.Optional;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= Replace.NONE)
public class OrderRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private OrderRepository orderRepository;

  @Rule
  public ExpectedException exceptionGrabber = ExpectedException.none();

  @Test
  public void testCreateOrder_valid_success() {
    Order newOrder = new Order();
    newOrder.setOrderNum(99);
    entityManager.persist(newOrder);

    Order savedOrder = orderRepository.save(newOrder);

    assertThat(savedOrder.getOrderNum(), is(equalTo(newOrder.getOrderNum())));
  }

  @Test
  public void testReadOrder_valid_success() {
    Order newOrder = new Order();
    newOrder.setOrderNum(99);
    entityManager.persist(newOrder);

    Order foundOrder = orderRepository.getOne(newOrder.getOrderId());

    assertThat(foundOrder.getOrderNum(), is(equalTo(newOrder.getOrderNum())));
  }

  @Test
  public void testUpdateOrder_valid_success() {
    Order newOrder = new Order();
    newOrder.setOrderNum(99);
    entityManager.persist(newOrder);

    Order foundOrder = orderRepository.getOne(newOrder.getOrderId());
    foundOrder.setOrderNum(100);

    // spring-data-jpa repository.save dual purposed for Create and Update operations
    Order outcome = orderRepository.save(foundOrder);

    assertThat(outcome.getOrderNum(), is(equalTo(foundOrder.getOrderNum())));
    assertThat(outcome.getOrderId(), is(equalTo(foundOrder.getOrderId())));
  }

  @Test
  public void testDeleteOrder_valid_success() {
    Order toDelete = new Order();
    entityManager.persist(toDelete);

    orderRepository.deleteById(toDelete.getOrderId());

    Optional<Order> outcome = orderRepository.findById(toDelete.getOrderId());
    assertThat(outcome.isPresent(), is(false));
  }

  @Test
  public void testFetchAccountOrders_validAccountId_success() {
    Optional<List<Order>> response = orderRepository.findOrdersByOrderAccountId(11);
    List<Order> outcome = response.get();

    assertThat(outcome.size(), is(1));
    assertThat(outcome.get(0).getOrderId(), is((long) 99));
  }
}