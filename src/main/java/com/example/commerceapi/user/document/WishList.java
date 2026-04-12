package com.example.commerceapi.user.document;

public class WishList {
    
     private String name;

	public WishList() {
    }

    public WishList(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Wishlist{" +
                "name='" + name + '\'' +
                '}';
    }
}
