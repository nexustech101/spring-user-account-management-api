package com.archtech.store.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {

    // Data fields
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String description;
    private double price;
    private boolean hasDiscount;
    private double discount;

    // Constructors
    public Product() {
        this.hasDiscount = false;
        this.discount = 1; // 100% of original price (no discount)
    }

    public Product(String name, double price, String description, boolean hasDiscount, double discount) {
        this.name = name;
        this.description = description;
        this.hasDiscount = hasDiscount;
        this.discount = discount;
        this.price = price;
    }

    /** Getter and setter methods for data model */
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHasDiscount(boolean hasDiscount) {
        this.hasDiscount = hasDiscount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean hasDiscount() {
        return this.hasDiscount;
    }

    public double getPrice() {
        if (this.hasDiscount) {
            return this.calculateDiscount();
        }
        return this.price;
    }

    private double calculateDiscount() {
        return this.price * (1 - this.discount);
    }
}