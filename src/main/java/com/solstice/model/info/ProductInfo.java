package com.solstice.model.info;

public class ProductInfo {

  private String productName;
  private String productDescription;
  private String image;
  private double productPrice;

  public ProductInfo() {

  }

  public ProductInfo(String productName, String productDescription, String image,
      double productPrice) {
    this.productName = productName;
    this.productDescription = productDescription;
    this.image = image;
    this.productPrice = productPrice;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getProductDescription() {
    return productDescription;
  }

  public void setProductDescription(String productDescription) {
    this.productDescription = productDescription;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public double getProductPrice() {
    return productPrice;
  }

  public void setProductPrice(double productPrice) {
    this.productPrice = productPrice;
  }
}
