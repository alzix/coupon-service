package com.tagril.couponservice.dao;

import org.springframework.data.repository.CrudRepository;

import com.tagril.couponservice.entities.Coupon;

public interface CouponRepository extends CrudRepository<Coupon, Long>{
}
