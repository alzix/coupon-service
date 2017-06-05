package com.tagril.couponservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.tagril.couponservice.dao.CompanyRepository;
import com.tagril.couponservice.dao.CouponRepository;
import com.tagril.couponservice.dao.CustomerRepository;
import com.tagril.couponservice.entities.Company;
import com.tagril.couponservice.entities.Coupon;
import com.tagril.couponservice.entities.CouponBuilder;
import com.tagril.couponservice.entities.CouponType;
import com.tagril.couponservice.entities.Customer;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminRestControllerTest {
	
	private static final int SEC_IN_DAY = 86400;  // seconds in 1 day - used for time calculation
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	public CompanyRepository companyRepo;
	
	@Autowired
	public CouponRepository couponRepo;
	
	@Autowired
	public CustomerRepository customerRepo;
	
	@Before
	public void bootstrap(){
		companyRepo.deleteAll();
		couponRepo.deleteAll();
		customerRepo.deleteAll();
		
		Company company = new Company("TestComp1", "123", "support@yes.com");
		companyRepo.save(company);
	
		Coupon coupon = new CouponBuilder()
				.setTitle(CouponType.ELECTRICITY.toString())
				.setMessage("this is a coupon for " + CouponType.ELECTRICITY.toString())
				.setPrice((double)CouponType.ELECTRICITY.ordinal())
				.setAmount(1)
				.setEndDate(new Date(System.currentTimeMillis() + 1000*SEC_IN_DAY*5))
				.setType(CouponType.ELECTRICITY)
				.setOwner(company)
				.build();
		couponRepo.save(coupon);
		
		Customer customer = new Customer("TestComp1", "****", "igor@mail.ru");
		customerRepo.save(customer);
		
		
	}	
	
	@Test
	public void adminCreateCompanyTest(){
		Company newComp = new Company("CompanyUnderTest", "321321", "foo@mail.com");
		this.restTemplate.postForObject("/admin/companies", newComp, String.class);
	}
	
	@Test
	public void adminCreateCompanyAndCouponTest(){
		Company newComp = new Company("CompanyUnderTest", "321321", "foo@mail.com");
		newComp = this.restTemplate.postForObject("/admin/companies", newComp, Company.class);
		Coupon newCoupon = new CouponBuilder()
				.setTitle(CouponType.ELECTRICITY.toString())
				.setMessage("this is a coupon for " + CouponType.ELECTRICITY.toString())
				.setPrice((double)CouponType.ELECTRICITY.ordinal())
				.setAmount(10)
				.setEndDate(new Date(System.currentTimeMillis() + 1000*SEC_IN_DAY*5))
				.setType(CouponType.ELECTRICITY)
				.setOwner(newComp)
				.build();
		newCoupon = this.restTemplate.postForObject("/admin/coupons", newCoupon, Coupon.class);
	}
	
	@Test
	public void adminDeleteCompanyTest(){
		//get all companies
		Company[] companies = restTemplate.getForObject("/admin/companies", Company[].class);
		assertTrue(companies.length == 1); //expecting only one
		
		restTemplate.delete("/admin/companies/" + companies[0].getId()); // delete single company
		
		Iterable<Coupon> coupons = couponRepo.findAll();
		
		// all related coupons should also be deleted
		assertEquals(0, coupons.spliterator().getExactSizeIfKnown());
	}
	
	
	@Test
	public void adminCreateCustomerTest(){
		Customer customer = new Customer("COPY", "PASTE", "copy@paste.com");
		customer = restTemplate.postForObject("/admin/customers", customer, Customer.class);
		
		assertNotNull(customer.getId());
	}
	
	@Test
	public void adminDeleteCustomerTest(){
		Customer customer = new Customer("COPY", "PASTE", "copy@paste.com");
		customer = restTemplate.postForObject("/admin/customers", customer, Customer.class);
		
		assertNotNull(customer.getId());
		
		restTemplate.delete("/admin/customers/" + customer.getId()); // delete single company
		
		// verify only single one customer left in repository
		Iterable<Customer> customers = customerRepo.findAll();
		assertEquals(1, customers.spliterator().getExactSizeIfKnown());
	}
	
	@Test
	public void adminFindCustomerTest(){
		Customer customer = new Customer("COPY", "PASTE", "copy@paste.com");
		customer = restTemplate.postForObject("/admin/customers", customer, Customer.class);
		
		assertNotNull(customer.getId());
		
		Customer[] customers = restTemplate.getForObject("/admin/customers", Customer[].class);
		
		assertTrue(customers.length == 2);
		
		customers = restTemplate.getForObject("/admin/customers?id={id}", Customer[].class, customer.getId());
		
		assertEquals(1, customers.length);
		
		assertEquals(customer.getName(), customers[0].getName());
		
	}
	
	@Test
	public void customerPurchaseCoupon(){
		//Company company = companyRepo.findAll().iterator().next();
		Coupon coupon = couponRepo.findAll().iterator().next();
		Customer customer = customerRepo.findAll().iterator().next();
		
		Coupon purchasedCoupon = restTemplate.postForObject(
				"/customers/{id}/coupons/{couponId}", 
				null, 
				Coupon.class,
				customer.getId(),
				coupon.getId()
		);
		assertEquals(1, coupon.getAmount());
		
		assertEquals(0, purchasedCoupon.getAmount());
		assertEquals(0, couponRepo.findAll().iterator().next().getAmount()); //same check but from DB
		
		// validate coupon cannot be purchased 
		ResponseEntity<Coupon> response = restTemplate.postForEntity(
				"/customers/{id}/coupons/{couponId}", 
				null, 
				Coupon.class,
				customer.getId(),
				coupon.getId()
		);
		assertNotEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void customerPurchaseExpiredCoupon(){
		Company company = companyRepo.findAll().iterator().next();
		Customer customer = customerRepo.findAll().iterator().next();
		
		// inject new expired coupon to DB
		Coupon coupon = new CouponBuilder()
				.setOwner(company)
				.setPrice(new Double(111))
				.setAmount(115)
				.setType(CouponType.TRAVELLING)
				.setStartDate(new Date(System.currentTimeMillis() - 1000*SEC_IN_DAY*5))
				.setEndDate(new Date(System.currentTimeMillis() - 1000*SEC_IN_DAY*1))
				.setMessage("should fail to purchase this coupon")
				.setTitle("expired coupon")
				.build();
		coupon = couponRepo.save(coupon);
		
		// validate coupon cannot be purchased 
		ResponseEntity<Coupon> response = restTemplate.postForEntity(
				"/customers/{id}/coupons/{couponId}", 
				null, 
				Coupon.class,
				customer.getId(),
				coupon.getId()
		);
		assertNotEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void companyCreateCoupon(){
		Company company = companyRepo.findAll().iterator().next();
		company.setCoupons(null); // otherwise will will run in to JSON serialization error
		Coupon coupon = new CouponBuilder()
				.setOwner(company)
				.setPrice(new Double(111))
				.setAmount(115)
				.setType(CouponType.TRAVELLING)
				.setStartDate(new Date(System.currentTimeMillis() - 1000*SEC_IN_DAY*5))
				.setEndDate(new Date(System.currentTimeMillis() - 1000*SEC_IN_DAY*1))
				.setMessage("should fail to purchase this coupon")
				.setTitle("expired coupon")
				.build();
		
		Coupon newCoupon = restTemplate.postForObject(
				"/companies/{id}/coupons", 
				coupon, 
				Coupon.class,
				company.getId()
		);
		assertNotNull(newCoupon.getId());
	}
	
	@Test
	public void companyListCoupons(){
		Company company = companyRepo.findAll().iterator().next();
		Coupon[] coupons = restTemplate.getForObject(
				"/companies/{id}/coupons", 
				Coupon[].class, 
				company.getId()
		);
		assertEquals(coupons.length, 1);
		
		// find by id
		coupons = restTemplate.getForObject(
				"/companies/{id}/coupons?coupon_id={couponId}", 
				Coupon[].class, 
				company.getId(),
				coupons[0].getId()
		);
		assertEquals(coupons.length, 1);
		
		// find by type
		coupons = restTemplate.getForObject(
				"/companies/{id}/coupons?type={type}", 
				Coupon[].class, 
				company.getId(),
				coupons[0].getType()
		);
		assertEquals(coupons.length, 1);
		
		// find by price
		coupons = restTemplate.getForObject(
				"/companies/{id}/coupons?price={price}", 
				Coupon[].class, 
				company.getId(),
				coupons[0].getPrice()
		);
		assertEquals(coupons.length, 1);
		
		// find by price
		coupons = restTemplate.getForObject(
				"/companies/{id}/coupons?price={price}", 
				Coupon[].class, 
				company.getId(),
				coupons[0].getPrice() - 1
		);
		assertEquals(coupons.length, 0);
		
	}
	
	
}
