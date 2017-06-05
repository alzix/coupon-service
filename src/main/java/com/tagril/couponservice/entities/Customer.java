package com.tagril.couponservice.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Customer implements Serializable {

	private static final long serialVersionUID = 8242990697692408757L;  //auto-generated
	
	@Id
    @GeneratedValue
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	public String email;
	
	protected Customer() {
        // no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
    }
	
	public Customer(String name, String password, String email) {
		super();
		this.name = name;
		this.password = password;
		this.email = email;
	}
	
	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
    		name = "customer_coupon", 
    		joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "id"),
    		inverseJoinColumns = @JoinColumn(name = "coupon_id", referencedColumnName = "id"))
    private List<Coupon> coupons;

	public List<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}
	
	// private Collection<Coupon> coupons;
	
	

}
