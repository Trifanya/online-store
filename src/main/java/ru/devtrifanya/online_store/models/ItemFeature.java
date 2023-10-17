package ru.devtrifanya.online_store.models;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "itemFeature")
public class ItemFeature {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "string_value")
    private String stringValue;

    @Column(name = "numeric_value")
    private Double numericValue;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "feature_id", referencedColumnName = "id")
    private Feature feature;

}
