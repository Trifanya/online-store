package ru.devtrifanya.online_store.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "feature")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feature {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "request_name")
    private String requestParamName;

    @Column(name = "unit")
    private String unit;

    @ManyToMany
    @JoinTable(
            name = "category_feature",
            joinColumns = @JoinColumn(name = "feature_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.REMOVE)
    private List<ItemFeature> features;



}
