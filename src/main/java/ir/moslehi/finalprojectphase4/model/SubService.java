package ir.moslehi.finalprojectphase4.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder

public class SubService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String name;

    Long basePrice;

    String description;

    @OneToMany(mappedBy = "subService")
    List<ExpertSubService> expert_subServices;

    @ManyToOne
    Service service;

    @OneToMany(mappedBy = "subService")
    List<Orders> orders;

    @Override
    public String toString() {
        return "SubService{" +
                "name='" + name + '\'' +
                ", basePrice=" + basePrice +
                ", description='" + description + '\'' +
                ", id=" + id +
                '}';
    }

}
