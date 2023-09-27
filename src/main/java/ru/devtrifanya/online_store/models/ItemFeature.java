package ru.devtrifanya.online_store.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "itemFeature")
@Data
//@NoArgsConstructor
public class ItemFeature {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "characteristic_id", referencedColumnName = "id")
    private Feature feature;

    @Column(name = "value")
    private String value;
}
