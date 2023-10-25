package ru.devtrifanya.online_store.models;

import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "image_for_review")
public class ReviewImage {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "review_id", referencedColumnName = "id")
    private Review review;

    public ReviewImage(int id, String url) {
        this.id = id;
        this.url = url;
    }
}