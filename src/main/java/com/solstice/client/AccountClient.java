package com.solstice.client;

import com.solstice.model.info.AddressInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "account-service")
@Component
public interface AccountClient {

  @GetMapping("/accounts/{accountId}/address/{addressId}")
  Resource<AddressInfo> fetchAccountAddress(@PathVariable(value = "accountId") long accountId, @PathVariable(value = "addressId") long addressId);

}
