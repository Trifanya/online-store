package ru.devtrifanya.online_store.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
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
}