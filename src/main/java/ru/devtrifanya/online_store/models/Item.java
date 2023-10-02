package ru.devtrifanya.online_store.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item implements Searchable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Вы не указали название товара.")
    private String name;

    @Column(name = "manufacturer")
    @NotEmpty(message = "Вы не указали производителя товара.")
    private String manufacturer;

    @Column(name = "price")
    @NotEmpty(message = "Вы не указали цену товара.")
    @Min(value = 0, message = "Цена не может быть ниже 0 руб.")
    private double price;

    @Column(name = "quantity")
    @NotEmpty(message = "Вы не указали количество товара.")
    private int quantity;

    @Column(name = "description")
    @NotEmpty(message = "Вы не добавили описание товара.")
    private String description;

    @Column(name = "image")
    private String imageURL;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @NotEmpty(message = "Вы не указали категорию товара.")
    private Category category;

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private List<ItemFeature> features;
}
