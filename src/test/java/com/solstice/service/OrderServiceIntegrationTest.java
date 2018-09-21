package com.solstice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.solstice.model.domain.OrderLineItem;
import com.solstice.util.NotFoundException;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace= Replace.NONE)
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class
})
@DatabaseSetup("classpath:test-dataset.xml")
public class OrderServiceIntegrationTest {

  @Autowired
  private OrderService orderService;

  @Test
  @Transactional
  public void testFetchLineItems_validOrderId_success() {
    List<OrderLineItem> outcome = orderService.fetchLineItems(99);

    assertThat(outcome.size(), is(equalTo(1)));
    assertThat(outcome.get(0).getLineItemOrder().getOrderId(), is((long)99));
    assertThat(outcome.get(0).getLineItemId(), is((long)2));
  }

  @Test(expected = NotFoundException.class)
  @Transactional
  public void testFetchLineItems_invalidOrderId_failure() {


    orderService.fetchLineItems(-1);
  }


}
