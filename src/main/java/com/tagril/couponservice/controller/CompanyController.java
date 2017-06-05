package com.tagril.couponservice.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.tagril.couponservice.entities.Coupon;
import com.tagril.couponservice.entities.CouponType;
import com.tagril.couponservice.service.CompanyService;

@RestController
@RequestMapping("/companies")
public class CompanyController {
	private final CompanyService companySerivce;
	
	@Autowired
	public CompanyController(CompanyService companySerivce) {
		this.companySerivce = companySerivce;
	}
	
	@RequestMapping(
			value="/{id}/coupons", 
			method={RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH}
	)
	public Coupon createCoupon(@PathVariable("id") Long companyId, @RequestBody Coupon coupon){
		return companySerivce.createOrUpdateCoupon(companyId, coupon);
	}
	
	@RequestMapping(value="/{id}/coupons/{coupon_id}", method=RequestMethod.DELETE)
	public void deleteCoupon(
		@PathVariable("id") Long companyId, 
		@PathVariable("coupon_id") Long couponId
	){
		companySerivce.removeCoupon(couponId);
	}
	
	@RequestMapping(value="/{id}/coupons", method=RequestMethod.GET)
	public Iterable<Coupon> getCoupon(
			@PathVariable("id") Long companyId, 
			@RequestParam(name="coupon_id", required=false) Long couponId,
			@RequestParam(name="type", required=false) CouponType type,
			@RequestParam(name="price", required=false) Double price,
			@RequestParam(name="date", required=false) Date date
	){
		if (null != couponId){
			return companySerivce.getCoupon(couponId);
		} else if (null != type) {
			return companySerivce.getCouponsByType(companyId, type);
		} else if (null != price) {
			return companySerivce.getCouponsByPrice(companyId, price);
		} else if (null != date) {
			return companySerivce.getCouponsByDate(companyId, date);
		} else {
			return companySerivce.getAllCoupons(companyId);
		}
	}
}