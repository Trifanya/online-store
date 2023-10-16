package ru.devtrifanya.online_store.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "category_relation")
public class CategoryRelation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "child_id", referencedColumnName = "id")
    private Category child;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Category parent;
}

