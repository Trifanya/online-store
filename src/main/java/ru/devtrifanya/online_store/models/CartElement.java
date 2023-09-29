package ru.devtrifanya.online_store.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_element")
@Data
@NoArgsConstructor
public class CartElement {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    @Column(name = "item_count")
    private int itemCount;

    public CartElement(User user, Item item, int itemCount) {
        this.user = user;
        this.item = item;
        this.itemCount = itemCount;
    }
}
