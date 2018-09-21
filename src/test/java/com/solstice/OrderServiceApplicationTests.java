package com.solstice;

import com.solstice.repository.OrderLineItemRepositoryIntegrationTest;
import com.solstice.repository.OrderRepositoryIntegrationTest;
import com.solstice.service.OrderServiceIntegrationTest;
import com.solstice.service.OrderServiceUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    OrderLineItemRepositoryIntegrationTest.class,
    OrderRepositoryIntegrationTest.class,
    OrderServiceUnitTest.class,
    OrderServiceIntegrationTest.class
})
public class OrderServiceApplicationTests {
}
