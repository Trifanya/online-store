package ru.devtrifanya.online_store.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category_relation")
@Data
//@NoArgsConstructor
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

    public CategoryRelation(Category child, Category parent) {
        this.child = child;
        this.parent = parent;
    }
}
