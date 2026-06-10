package com.orm.app;

import com.orm.annotations.*;

/**
 * Example entity — a Product.
 */
@Table(name = "products")
@GenerateRepository(className = "ProductRepository")   // explicit name demo
@Validated
public class Product {

    @Id
    private Long id;

    @NotNull
    @MaxLength(200)
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "price")
    private Double price;

    public Product() {}

    public Product(String title, Double price) {
        this.title = title;
        this.price = price;
    }

    public Long   getId()    { return id; }
    public String getTitle() { return title; }
    public Double getPrice() { return price; }

    public void setTitle(String title) { this.title = title; }
    public void setPrice(Double price) { this.price = price; }

    @Override
    public String toString() {
        return "Product{id=" + id + ", title='" + title + "', price=" + price + "}";
    }
}
