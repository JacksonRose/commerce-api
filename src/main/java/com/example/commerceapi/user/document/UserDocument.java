package com.example.commerceapi.user.document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;;

@Document(collection = "users")
public class UserDocument {
    @Id
    private String id;

    private String name;
    private String email;
    private String phone;

    private List<Address> addresses =  new ArrayList<>();;
    private List<WishList> wishLists =  new ArrayList<>();;

	public UserDocument() {
	}

	public UserDocument(String name, String email, String phone){
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
	
	public UserDocument(String name, String email, String phone, 
		List<Address> addresses, List<WishList> wishLists){
		this.name = name;
        this.email = email;
        this.phone = phone;
		this.addresses = this.addresses != null ? addresses : new ArrayList<>();
		this.wishLists = this.wishLists != null ? wishLists : new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	
    public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = this.addresses != null ? addresses : new ArrayList<>();
	}
    
    public List<WishList> getWishLists() {
		return wishLists;
	}

	public void setWishLists(List<WishList> wishLists) {
		this.wishLists = this.wishLists != null ? wishLists : new ArrayList<>();
	}

	public void addAddress(Address address){
		if(address != null) {
			this.addresses.add(address);
		}
	}

	public void removeAddress(Address address){
		if(address != null){
			this.addresses.remove(address);
		}
	}
	public void addWishList(WishList wishList){
		if(wishList != null) {
			this.wishLists.add(wishList);
		}
	}

	public void removeWishList(WishList wishList){
		if(wishList != null){
			this.wishLists.remove(wishList);
		}
	}

	@Override
	public boolean equals(Object o){
		if(this == o) return true;
		if(!(o instanceof UserDocument)) return false;
		UserDocument that = (UserDocument) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode(){
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "UserDocument{" +
			"id='" + id +'\'' + 
			", name='" + name +'\'' + 
			", email='" + email +'\'' +
			", addresses=" + addresses +
			", wishLists=" + wishLists +
			'}';
	}
}
