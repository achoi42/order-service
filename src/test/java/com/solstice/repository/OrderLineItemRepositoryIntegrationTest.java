package com.solstice.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.solstice.model.domain.OrderLineItem;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class OrderLineItemRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private OrderLineItemRepository lineItemRepository;

  @Test
  public void testCreateLineItem_valid_success() {
    OrderLineItem newLineItem = new OrderLineItem();
    newLineItem.setLineItemPrice(-99.99);
    entityManager.persist(newLineItem);

    OrderLineItem saved = lineItemRepository.save(newLineItem);
    assertThat(saved.getLineItemPrice(), is(equalTo(newLineItem.getLineItemPrice())));
  }

  @Test
  public void testReadLineItem_valid_success() {
    OrderLineItem newLineItem = new OrderLineItem();
    newLineItem.setLineItemPrice(-99.99);
    entityManager.persist(newLineItem);

    OrderLineItem found = lineItemRepository.getOne(newLineItem.getLineItemId());
    assertThat(found.getLineItemPrice(), is(equalTo(newLineItem.getLineItemPrice())));
  }

  @Test
  public void testUpdateLineItem_valid_success() {
    OrderLineItem newLineItem = new OrderLineItem();
    newLineItem.setLineItemPrice(-99.99);
    entityManager.persist(newLineItem);

    OrderLineItem found = lineItemRepository.getOne(newLineItem.getLineItemId());
    found.setLineItemPrice(-11.11);
    OrderLineItem outcome = lineItemRepository.save(found);

    assertThat(outcome.getLineItemPrice(), is(equalTo(found.getLineItemPrice())));
    assertThat(outcome.getLineItemId(), is(equalTo(found.getLineItemId())));
  }
  @Test
  public void testDeleteLineItem_valid_success() {
    OrderLineItem toDelete = new OrderLineItem();
    entityManager.persist(toDelete);

    lineItemRepository.deleteById(toDelete.getLineItemId());
    Optional<OrderLineItem> outcome = lineItemRepository.findById(toDelete.getLineItemId());
    assertThat(outcome.isPresent(), is(false));
  }
}
