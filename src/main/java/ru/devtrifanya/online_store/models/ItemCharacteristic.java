package ru.devtrifanya.online_store.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_characteristic")
@Data
//@NoArgsConstructor
public class ItemCharacteristic {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "characteristic_id", referencedColumnName = "id")
    private Characteristic characteristic;

    @Column(name = "value")
    private String value;
}
