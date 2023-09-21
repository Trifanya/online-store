package ru.devtrifanya.online_store.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "item")
public class Item {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Вы не указали название товара.")
    private String name;

    @Column(name = "price")
    @NotEmpty(message = "Вы не указали цену товара.")
    @Min(value = 0, message = "Цена не может быть ниже 0 руб.")
    private double price;

    @Column(name = "description")
    @NotEmpty(message = "Вы не добавили описание товара.")
    private String description;

    @Column(name = "image")
    private String imageURL;

    @Column(name = "quantity")
    @NotEmpty(message = "Вы не указали количество товара.")
    private int quantity;

    @Column(name = "category")
    @NotEmpty(message = "Вы не указали категорию товара.")
    private String category;

    @Column(name = "parent_category")
    @NotEmpty(message = "Вы не указали родительскую категорию товара.")
    private String parent_category;

    public Item() {

    }

    public Item(String name, double price, String description, String imageURL, int quantity, String category, String parent_category) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageURL = imageURL;
        this.quantity = quantity;
        this.category = category;
        this.parent_category = parent_category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getParent_category() {
        return parent_category;
    }

    public void setParent_category(String parent_category) {
        this.parent_category = parent_category;
    }
}
