package com.tagril.couponservice.dao;

import org.springframework.data.repository.CrudRepository;

import com.tagril.couponservice.entities.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long>{
}
