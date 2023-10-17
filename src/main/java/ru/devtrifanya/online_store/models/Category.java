package ru.devtrifanya.online_store.models;

import lombok.Data;
import jakarta.persistence.*;

import java.util.List;

@Data
@Entity
@Table(name = "category")
public class Category {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Item> items;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<CategoryRelation> relationsWithChildren;

    @OneToMany(mappedBy = "child", cascade = CascadeType.REMOVE)
    private List<CategoryRelation> relationsWithParents;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "category_feature",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    private List<Feature> features;
}
