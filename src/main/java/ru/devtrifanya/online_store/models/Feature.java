package ru.devtrifanya.online_store.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "feature")
@Data
//@NoArgsConstructor
public class Feature {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
}
