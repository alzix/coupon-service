package com.tagril.couponservice.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tagril.couponservice.dao.CompanyRepository;
import com.tagril.couponservice.dao.CouponRepository;
import com.tagril.couponservice.entities.Company;
import com.tagril.couponservice.entities.Coupon;
import com.tagril.couponservice.entities.CouponType;

@Service
public class CompanyService {
	
	private final CouponRepository couponRepo;
	
	private final CompanyRepository companyRepo;
	
	@Autowired
	public CompanyService(CouponRepository couponRepo, CompanyRepository companyRepo) {
		this.couponRepo = couponRepo;
		this.companyRepo = companyRepo;
	}
	
	public Coupon createOrUpdateCoupon(long companyId, Coupon coupon) {
		
		Company company = companyRepo.findOne(companyId);
		if (null == company){
			throw new RuntimeException("no such company " + companyId);
		}
		
		coupon.setOwner(company);
		coupon = couponRepo.save(coupon);
		return coupon;
	}
	
	public void removeCoupon(long couponId) {
		Coupon coupon = couponRepo.findOne(couponId);
		if (null == coupon){
			return;
		}
		if (coupon.isActive() && !coupon.getCustomers().isEmpty()){
			throw new RuntimeException(
					String.format(
							"cannot delete coupon - owned by %d customers", 
							coupon.getCustomers().size()
					)
			);
		}
		couponRepo.delete(couponId);
	}
	
	public Iterable<Coupon> getCoupon(long couponId){
		Coupon coupon = couponRepo.findOne(couponId);
		if (null == coupon){
			throw new RuntimeException("no such coupon " + couponId);
		}
		return Collections.singletonList(coupon);
	}

	public Iterable<Coupon> getAllCoupons(long companyId) {
		Company company = companyRepo.findOne(companyId);
		if (null == company){
			throw new RuntimeException("no such company " + companyId);
		}
		return company.getCoupons();
	}

	public Iterable<Coupon> getCouponsByType(long companyId, CouponType couponType) {
		Iterable<Coupon> allCoupons = getAllCoupons(companyId);
		List<Coupon> coupons = new ArrayList<>(); 
		for (Coupon coupon : allCoupons) {
			if (coupon.getType().equals(couponType)){
				coupons.add(coupon);
			}
		}
		return coupons;
	}

	public Iterable<Coupon> getCouponsByPrice(long companyId, Double price) {
		Iterable<Coupon> allCoupons = getAllCoupons(companyId);
		List<Coupon> coupons = new ArrayList<>(); 
		for (Coupon coupon : allCoupons) {
			if (coupon.getPrice() <= price){
				coupons.add(coupon);
			}
		}
		return coupons;
	}

	public Iterable<Coupon> getCouponsByDate(long companyId, Date date) {
		Iterable<Coupon> allCoupons = getAllCoupons(companyId);
		List<Coupon> coupons = new ArrayList<>(); 
		for (Coupon coupon : allCoupons) {
			if (coupon.getEndDate().after(date)){
				coupons.add(coupon);
			}
		}
		return coupons;
	}
	
	
}
