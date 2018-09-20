package com.solstice.config;

import com.solstice.model.domain.Order;
import com.solstice.model.domain.OrderLineItem;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class RepositoryConfig extends RepositoryRestConfigurerAdapter {

  @Override
  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
    config.exposeIdsFor(Order.class);
    config.exposeIdsFor(OrderLineItem.class);
  }
}
