package ru.devtrifanya.online_store.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
public class Review {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "stars")
    private int stars;

    @Column(name = "comment")
    private String comment;

    @Column(name = "created_at")
    private LocalDateTime timestamp;

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
    private List<ReviewImage> images;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Review(int id, int stars, String comment) {
        this.id = id;
        this.stars = stars;
        this.comment = comment;
    }
}
