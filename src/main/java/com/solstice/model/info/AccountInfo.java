package com.solstice.model.info;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccountInfo {

  private long accountId;
  private String firstName;
  private String lastName;
  private String email;
  private List<AddressInfo> addresses;

  public AccountInfo() {

  }

  public AccountInfo(long accountId, String firstName, String lastName, String email) {
    this.accountId = accountId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    if(addresses == null) {
      this.addresses = new ArrayList<>(Collections.emptyList());
    }
    else {
      this.addresses = addresses;
    }
  }

  public long getAccountId() {
    return accountId;
  }

  public void setAccountId(long accountId) {
    this.accountId = accountId;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<AddressInfo> getAddresses() {
    return addresses;
  }

  public void setAddresses(List<AddressInfo> addresses) {
    this.addresses = addresses;
  }
}
