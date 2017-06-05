package com.tagril.couponservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tagril.couponservice.entities.Company;
import com.tagril.couponservice.entities.Customer;
import com.tagril.couponservice.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminContorller {
	
	private final AdminService adminService;
	
	@Autowired
	public AdminContorller(AdminService adminService) {
		this.adminService = adminService;
	}
    
	//-----------------------------------------------------------------------------------------
	@RequestMapping(
			value="/companies", 
			method={RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH}
	)
	public Company createOrUpdateCompany(@RequestBody Company company){
		return adminService.createOrUpdateCompany(company);
	}
	
	@RequestMapping(
			value="/companies/{id}", 
			method=RequestMethod.DELETE
	)
	public void deleteCompany(@PathVariable("id") Long companyId){
		adminService.deleteCompany(companyId);
	}
	
	@RequestMapping(
			value="/companies", 
			method=RequestMethod.GET
	)
	public Iterable<Company> getCompanies(@RequestParam(name="id", required=false) Long companyId){
		if (null == companyId){
			return adminService.listCompanies();
		} else {
			return adminService.findCompanyById(companyId);
		}
	}
	
	//----------------------------------------------------------------------------------------
	@RequestMapping(
			value="/customers", 
			method={RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH}
	)
	public Customer createOrUpdateCustomer(@RequestBody Customer customer){
		return adminService.createOrUpdateCustomer(customer);
	}
	
	@RequestMapping(
			value="/customers/{id}", 
			method=RequestMethod.DELETE
	)
	public void deleteCustomer(@PathVariable("id") Long customerId){
		adminService.deleteCustomer(customerId);
	}
	
	@RequestMapping(
			value="/customers", 
			method=RequestMethod.GET
	)
	public Iterable<Customer> getCustomers(@RequestParam(name="id", required=false) Long customerId){
		if (null == customerId){
			return adminService.listCustomers();
		} else {
			return adminService.findCustomerById(customerId);
		}
	}
	
}
