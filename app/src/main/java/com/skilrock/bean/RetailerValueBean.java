package com.skilrock.bean;

import java.io.Serializable;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class RetailerValueBean implements Serializable {
	private static final long serialVersionUID = 9L;
	private String addr_1;
	private String phoneNbr;
	private String lastName;
	private String mobileNbr;
	private String addr_2;
	private String firstName;
	private String email_id;
	private LatLng latLng;
    public String type;

    public List<String> getServices() {
        return services;
    }

    public List<String> services;
    public String getType() {
        return type;
    }



	public RetailerValueBean(String addr_1, String phoneNbr, String lastName,
			String mobileNbr, String addr_2, String firstName, String email_id,String type, List<String> services) {
		super();
		this.addr_1 = addr_1;
		this.phoneNbr = phoneNbr;
		this.lastName = lastName;
		this.mobileNbr = mobileNbr;

		try {
			if (addr_2.length() > 0) {
				this.addr_2 = ", " + addr_2;
			} else {
				this.addr_2 = addr_2;
			}

		} catch (NullPointerException e) {
			this.addr_2 = addr_2;

		} catch (Exception e) {
			this.addr_2 = addr_2;

		}

		this.firstName = firstName;
		this.email_id = email_id;
        this.type = type;
        this.services = services;
	}

	public RetailerValueBean(String addr_1, String phoneNbr, String lastName,
			String mobileNbr, String addr_2, String firstName, String email_id,
			LatLng latLng,String type, List<String> services) {
		super();
        this.type = type;
        this.services = services;
		this.addr_1 = addr_1;
		this.phoneNbr = phoneNbr;
		this.lastName = lastName;
		this.mobileNbr = mobileNbr;
		this.addr_2 = addr_2;
		this.firstName = firstName;
		this.email_id = email_id;
		this.latLng = latLng;
	}

	public LatLng getLatLng() {
		return latLng;
	}

	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}

	public String getAddr_1() {
		return addr_1;
	}

	public void setAddr_1(String addr_1) {
		this.addr_1 = addr_1;
	}

	public String getPhoneNbr() {
		return phoneNbr;
	}

	public void setPhoneNbr(String phoneNbr) {
		this.phoneNbr = phoneNbr;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobileNbr() {
		return mobileNbr;
	}

	public void setMobileNbr(String mobileNbr) {
		this.mobileNbr = mobileNbr;
	}

	public String getAddr_2() {
		return addr_2;
	}

	public void setAddr_2(String addr_2) {
		this.addr_2 = addr_2;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail_id() {
		return email_id;
	}

	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}

}