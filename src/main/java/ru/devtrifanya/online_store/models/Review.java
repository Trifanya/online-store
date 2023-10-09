package ru.devtrifanya.online_store.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "review")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "stars")
    @Min(value = 1)
    @Max(value = 5)
    private int stars;

    @Column(name = "comment")
    @Size(min = 10, max = 1000, message = "Максимальная длина комментария - 1000 символов.")
    private String comment;

    @OneToMany
    @JoinTable(
            name = "review_image",
            joinColumns = @JoinColumn(name = "review_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private List<Image> images;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private User user;

    @Column(name = "created_at")
    private LocalDateTime timestamp;
}
