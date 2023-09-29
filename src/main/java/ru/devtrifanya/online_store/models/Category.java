package ru.devtrifanya.online_store.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
public class Category implements Searchable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Feature> features;

    @OneToMany(mappedBy = "category")
    private List<Item> items;

    public Category(String name) {
        this.name = name;
    }
}
