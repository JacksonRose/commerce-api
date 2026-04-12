package com.example.commerceapi.user.document;

public class Address {
    private String city;
    private String type;

    public Address() {
    }

    public Address(String city, String type) {
        this.city = city;
        this.type = type;
    }

	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
	@Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
