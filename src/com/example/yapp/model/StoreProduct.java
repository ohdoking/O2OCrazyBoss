package com.example.yapp.model;

public class StoreProduct {

	private String productImage;
	private String storeName;
	private String productName;
	private String originPrice;
	private String discountPrice;
	private String discountPercent;
	private String finishTime;
	private String time;
	
	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public StoreProduct(){
		
	}
	
	public StoreProduct(String productImage, String storeName,
			String productName, String originPrice, String discountPrice,
			String discountPercent, String time) {
		super();
		this.productImage = productImage;
		this.storeName = storeName;
		this.productName = productName;
		this.originPrice = originPrice;
		this.discountPrice = discountPrice;
		this.discountPercent = discountPercent;
		this.time = time;
	}
	
	
	public String getProductImage() {
		return productImage;
	}
	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getOriginPrice() {
		return originPrice;
	}
	public void setOriginPrice(String originPrice) {
		this.originPrice = originPrice;
	}
	public String getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}
	public String getDiscountPercent() {
		return discountPercent;
	}
	public void setDiscountPercent(String discountPercent) {
		this.discountPercent = discountPercent;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
