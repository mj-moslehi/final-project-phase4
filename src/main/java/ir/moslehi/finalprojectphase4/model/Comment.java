package ir.moslehi.finalprojectphase4.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Double score;

    String comment;

    @ManyToOne
    Customer customer;

    @ManyToOne
    Expert expert;

    @OneToOne
    Orders orders;

    @Override
    public String toString() {
        return "Comment{" +
                "score=" + score +
                ", comment='" + comment + '\'' +
                ", id=" + id +
                '}';
    }
}
