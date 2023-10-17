package ru.devtrifanya.online_store.models;

import lombok.Data;
import jakarta.persistence.*;

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

