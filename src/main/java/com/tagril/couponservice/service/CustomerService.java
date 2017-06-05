package com.tagril.couponservice.service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tagril.couponservice.dao.CouponRepository;
import com.tagril.couponservice.dao.CustomerRepository;
import com.tagril.couponservice.entities.Coupon;
import com.tagril.couponservice.entities.CouponType;
import com.tagril.couponservice.entities.Customer;

@Service
public class CustomerService {
	
	private final CouponRepository couponRepo;
	
	private final CustomerRepository customerRepo;
	
	@Autowired
	public CustomerService(CouponRepository couponRepo, CustomerRepository customerRepo) {
		this.couponRepo = couponRepo;
		this.customerRepo = customerRepo;
	}



	public synchronized  Coupon purchaseCoupon(long customerId, long couponId){
		Coupon coupon = couponRepo.findOne(couponId);
		if (null == coupon){
			throw new RuntimeException("no such coupon for " + couponId);
		}
		Customer customer = customerRepo.findOne(customerId);
		if (null == customer){
			throw new RuntimeException("no such customer for " + customerId);
		}
		
		coupon.acquire(customer);
		coupon = couponRepo.save(coupon);
		return coupon;
	}
	
	public Iterable<Coupon> getAllPurchasedCoupons(long customerId) {
		Customer customer = customerRepo.findOne(customerId);
		if (null == customer){
			throw new RuntimeException("no such customer for " + customerId);
		}
		return customer.getCoupons();

	}
	
	private Iterable<Coupon> getCouponsByPredicate(long customerId, Predicate<Coupon> predicate){
		Iterable<Coupon> allCoupons = getAllPurchasedCoupons(customerId);
		
		List<Coupon> coupons = StreamSupport
			.stream(allCoupons.spliterator(), /*parallel=*/false)
			.filter(predicate)
			.collect(Collectors.toList());
		return coupons;
	}
	
	public Iterable<Coupon> getAllPurchasedCouponsByType(long customerId, CouponType couponType){
		return getCouponsByPredicate(
				customerId,
				coupon -> !coupon.getType().equals(couponType)
		);
	}
	
	public Iterable<Coupon> getAllPurchasedCouponsByPrice(long customerId, double price){
		return getCouponsByPredicate(
				customerId,
				coupon -> coupon.getPrice() != price
		);
	}
	
}
