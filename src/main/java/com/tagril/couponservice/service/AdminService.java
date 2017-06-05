package com.tagril.couponservice.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tagril.couponservice.dao.CompanyRepository;
import com.tagril.couponservice.dao.CouponRepository;
import com.tagril.couponservice.dao.CustomerRepository;
import com.tagril.couponservice.entities.Company;
import com.tagril.couponservice.entities.Customer;

@Service
public class AdminService {
	
	private final CompanyRepository companyRepo;
	private final CustomerRepository customerRepo;
	
	
	@Autowired
	public AdminService(
			CompanyRepository companyRepo, 
			CouponRepository couponRepo, 
			CustomerRepository customerRepo
	) {
		this.companyRepo = companyRepo;
		this.customerRepo = customerRepo;
	}

	public Company createOrUpdateCompany(Company company){
		return companyRepo.save(company);
	}
	
	public void deleteCompany(long id){
		companyRepo.delete(id);
	}
	
	public Iterable<Company> findCompanyById(long id){
		Company company = companyRepo.findOne(id);
		if (null == company){
			throw new RuntimeException("no such company " + id);
		}
		return Collections.singletonList(company);
	}
	
	public Iterable<Company> listCompanies(){
		return companyRepo.findAll();
	}
	
	//-----------------------------------------------------------------------------
	public Customer createOrUpdateCustomer(Customer customer){
		return customerRepo.save(customer);
	}
	
	public void deleteCustomer(long id){
		customerRepo.delete(id);
	}
	
	public Iterable<Customer> findCustomerById(long companyId){
		Customer customer = customerRepo.findOne(companyId);
		if (null == customer){
			throw new RuntimeException("no such company " + companyId);
		}
		return Collections.singletonList(customer);
	}
	
	public Iterable<Customer> listCustomers(){
		return customerRepo.findAll();
	}
	
	
}
