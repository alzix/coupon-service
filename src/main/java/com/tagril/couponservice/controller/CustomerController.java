package com.tagril.couponservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tagril.couponservice.entities.Coupon;
import com.tagril.couponservice.entities.CouponType;
import com.tagril.couponservice.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	
	private final CustomerService customerService;
	
	@Autowired
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@RequestMapping(value="/{id}/coupons/{coupon-id}", method={RequestMethod.POST, RequestMethod.PUT})
	public Coupon purchaseCoupon(
			@PathVariable("id") Long customerId, 
			@PathVariable("coupon-id") Long couponId
	){
		return customerService.purchaseCoupon(customerId, couponId);
	}
	
	@RequestMapping(value="/{id}/coupons", method=RequestMethod.GET)
	public Iterable<Coupon> getCoupons(
			@PathVariable("id") Long customerId,
			@RequestParam(name="type", required=false) CouponType type,
			@RequestParam(name="price", required=false) Double price){
		if (null != type){
			return customerService.getAllPurchasedCouponsByType(customerId, type);
		} else if (null != price) {
			return customerService.getAllPurchasedCouponsByPrice(customerId, price);
		}
		return customerService.getAllPurchasedCoupons(customerId);
	}
}
