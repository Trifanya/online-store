package ru.devtrifanya.online_store.models;

import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "item")
public class Item {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "price")
    private double price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "description")
    private String description;

    @Column(name = "rating")
    private double rating;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private List<ItemFeature> features;

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private List<Review> reviews;

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private List<CartElement> cartElements;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "item_image",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private List<ItemImage> itemImages;

    public Item(int id, String name, String manufacturer, double price, int quantity, String description, double rating) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.rating = rating;
    }
}
