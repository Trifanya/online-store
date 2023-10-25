package ru.devtrifanya.online_store.models;

import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "feature")
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

    @OneToMany(mappedBy = "feature", cascade = CascadeType.REMOVE)
    private List<ItemFeature> features;

    @ManyToMany//(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "category_feature",
            joinColumns = @JoinColumn(name = "feature_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    public Feature(int id, String name, String requestParamName, String unit) {
        this.id = id;
        this.name = name;
        this.requestParamName = requestParamName;
        this.unit = unit;
    }
}
