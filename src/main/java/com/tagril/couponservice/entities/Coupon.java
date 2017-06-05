package com.tagril.couponservice.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Coupon implements Serializable{

	private static final long serialVersionUID = 6170026463082955621L; // auto-generated
	
	@TableGenerator(name = "coupon_gen", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "coupon_gen")
	private Long id;
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	@Column(nullable = false)
	private int amount;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private CouponType type;
	
	@Column(nullable = false)
	private String message;
	
	@Column(nullable = false)
	private Double price;

	@Column(nullable = true)
	private String image;
	
	@ManyToOne()
	private Company owner;
	
	@ManyToMany(mappedBy = "coupons")
    private Set<Customer> customers;
	
	
	public Set<Customer> getCustomers() {
		return customers;
	}


	public void setCustomers(Set<Customer> customers) {
		this.customers = customers;
	}


	public Coupon() {
		// no-args constructor required by JPA spec
	}
	
	public Long getId() {
		return id;
	}


	public String getTitle() {
		return title;
	}


	public Date getStartDate() {
		return startDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public int getAmount() {
		return amount;
	}


	public CouponType getType() {
		return type;
	}


	public String getMessage() {
		return message;
	}


	public Double getPrice() {
		return price;
	}
	
	
	public Company getOwner() {
		return owner;
	}

	public void setOwner(Company company) {
		this.owner = company;
	}


	public String getImage() {
		return image;
	}
	
	public boolean isActive() {
		Date now = new Date();
		if (now.after(startDate) && now.before(endDate)) {
			return true;
		}
		return false;
	}
	
	public void acquire(Customer customer){
		if (!isActive()){
			throw new RuntimeException("coupon #" + id + " is exprired");
		}
		if (amount == 0){
			throw new RuntimeException("coupon #" + id + " is exhausted");
		}
		amount--;
		customers.add(customer);
	}

	public void setId(Long id) {
		this.id = id;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public void setAmount(int amount) {
		this.amount = amount;
	}


	public void setType(CouponType type) {
		this.type = type;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public void setPrice(Double price) {
		this.price = price;
	}


	public void setImage(String image) {
		this.image = image;
	}


	public Boolean isExpired() {
		long ts = System.currentTimeMillis();
		Date cd = new Date(ts);
		if (cd.after(endDate)) {
			return true;
		}
		return false;
	}

	public boolean leftCoupon() {
		if (amount > 0) {
			return true;
		}
		return false;
	}
}
