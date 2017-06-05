package com.tagril.couponservice;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.tagril.couponservice.dao.CompanyRepository;
import com.tagril.couponservice.dao.CouponRepository;
import com.tagril.couponservice.dao.CustomerRepository;
import com.tagril.couponservice.entities.Company;
import com.tagril.couponservice.entities.Coupon;
import com.tagril.couponservice.entities.CouponType;
import com.tagril.couponservice.entities.Customer;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
//	@Bean
//    public HibernateJpaSessionFactoryBean sessionFactory() {
//        return new HibernateJpaSessionFactoryBean();
//    }
	
	@Autowired
	private CompanyRepository companyRepo;
	
	@Autowired
	private CouponRepository couponRepo;
	
	@Autowired
	private CustomerRepository customerRepo;
	
	
	@Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			Company company = null;
			company = new Company("Dummy", "123", "support@dummy.com");
			company = companyRepo.save(company);
			
			for (CouponType type : CouponType.values()){
				Coupon coupon = new Coupon();
				coupon.setTitle(type.toString());
				coupon.setMessage("this is a coupon for " + type.toString());
				coupon.setPrice((double)type.ordinal());
				coupon.setAmount(10);
				coupon.setStartDate(new Date());
				coupon.setEndDate(new Date(System.currentTimeMillis() + 1000*86400*5));
				coupon.setType(type);
				coupon.setOwner(company);
				couponRepo.save(coupon);
			}
			
			Customer customer = new Customer("noob", "****", "noob@mail.com");
			customerRepo.save(customer);
			
		};
	}
}
