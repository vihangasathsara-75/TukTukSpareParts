package com.tuktuk.tuktukspareparts;

import java.util.ArrayList;

public class Cart {

    // list to hold items added to the cart
    private ArrayList<Part> cartItems;

    // list to hold the quantity for each item in the cart
    private ArrayList<Integer> cartQuantities;

    public Cart() {
        cartItems = new ArrayList<>();
        cartQuantities = new ArrayList<>();
    }

    // adds a part and quantity to the cart
    public boolean addItem(Part part, int quantity) {

        if (quantity <= 0) {
            System.out.println("quantity must be more than 0");
            return false;
        }

        // cant add more than whats in stock
        if (quantity > part.getQuantity()) {
            System.out.println("not enough stock for: " + part.getName());
            return false;
        }

        // check if this part is already in the cart
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getCode().equals(part.getCode())) {
                // update quantity instead of adding again
                int newQuantity = cartQuantities.get(i) + quantity;

                // make sure combined quantity doesnt exceed stock
                if (newQuantity > part.getQuantity()) {
                    System.out.println("total quantity exceeds stock");
                    return false;
                }

                cartQuantities.set(i, newQuantity);
                System.out.println("updated quantity for: " + part.getName());
                return true;
            }
        }

        // part not in cart yet so add it
        cartItems.add(part);
        cartQuantities.add(quantity);
        System.out.println("added to cart: " + part.getName());
        return true;
    }

}