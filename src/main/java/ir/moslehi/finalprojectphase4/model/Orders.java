package ir.moslehi.finalprojectphase4.model;

import ir.moslehi.finalprojectphase4.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder

public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long proposedPrice;

    String description;

    Date dateOfOrder;

    String address;

    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;

    @ManyToOne
    SubService subService;

    @ManyToOne
    Customer customer;

    @ManyToOne
    Expert expert;

    @OneToMany(mappedBy = "orders")
    List<Suggestion> suggestions;

    @OneToOne(mappedBy = "orders")
    Comment comment;

    @Override
    public String toString() {
        return "Orders{" +
                "proposedPrice=" + proposedPrice +
                ", description='" + description + '\'' +
                ", dateOfOrder=" + dateOfOrder +
                ", address='" + address + '\'' +
                ", orderStatus=" + orderStatus +
                ", id=" + id +
                '}';
    }
}
