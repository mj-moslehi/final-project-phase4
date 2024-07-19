package ir.moslehi.finalprojectphase4.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder

public class Suggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Long proposedPrice;

    @Column(nullable = false)
    Date startDate;

    @Column(nullable = false)
    Date finishDate;

    @ManyToOne
    Expert expert;

    @ManyToOne
    Orders orders;

    @Override
    public String toString() {
        return "Suggestion{" +
                "proposedPrice=" + proposedPrice +
                ", startDate=" + startDate +
                ", finishDate=" + finishDate +
                ", id=" + id +
                '}';
    }
}
