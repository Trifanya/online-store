package ru.devtrifanya.online_store.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "categories")
    private List<Feature> features;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Item> items;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<CategoryRelation> relationsWithChildren;

    @OneToMany(mappedBy = "child", cascade = CascadeType.REMOVE)
    private List<CategoryRelation> relationsWithParents;
}
