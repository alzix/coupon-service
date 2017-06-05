package com.tagril.couponservice.entities;

import java.util.Date;

public class CouponBuilder {
	private String title;
	private Date startDate = new Date();
	private Date endDate;
	private int amount;
	private CouponType type;
	private String message;
	private Double price;
	private String image;
	private Company owner;
	
	public CouponBuilder setTitle(String title) {
		this.title = title;
		return this;
	}
	public CouponBuilder setStartDate(Date startDate) {
		this.startDate = startDate;
		return this;
	}
	public CouponBuilder setEndDate(Date endDate) {
		this.endDate = endDate;
		return this;
	}
	public CouponBuilder setAmount(int amount) {
		this.amount = amount;
		return this;
	}
	public CouponBuilder setType(CouponType type) {
		this.type = type;
		return this;
	}
	public CouponBuilder setMessage(String message) {
		this.message = message;
		return this;
	}
	public CouponBuilder setPrice(Double price) {
		this.price = price;
		return this;
	}
	public CouponBuilder setImage(String image) {
		this.image = image;
		return this;
	}
	public CouponBuilder setOwner(Company owner) {
		this.owner = owner;
		return this;
	}
	
	public Coupon build(){
		Coupon coupon = new Coupon();
		coupon.setTitle(title);
		coupon.setStartDate(startDate);
		coupon.setEndDate(endDate);
		coupon.setAmount(amount);
		coupon.setType(type);
		coupon.setMessage(message);
		coupon.setPrice(price);
		coupon.setImage(image);
		coupon.setOwner(owner);
		return coupon;
	}
	
}
