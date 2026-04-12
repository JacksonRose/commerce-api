package com.example.commerceapi.user.dto;

import java.util.List;

public class UserRequest{

    private String name;
    private String email;
    private String phone;

	private List<AddressRequest> addresses;
    private List<WishListRequest> wishlists;

	public List<AddressRequest> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<AddressRequest> addresses) {
		this.addresses = addresses;
	}
	public List<WishListRequest> getWishlists() {
		return wishlists;
	}
	public void setWishlists(List<WishListRequest> wishlists) {
		this.wishlists = wishlists;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

    
}
