package com.tagril.couponservice.dao;

import org.springframework.data.repository.CrudRepository;

import com.tagril.couponservice.entities.Company;

public interface CompanyRepository extends CrudRepository<Company, Long>{
}
