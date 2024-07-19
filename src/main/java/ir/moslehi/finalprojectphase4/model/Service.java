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

public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String name;

    @OneToMany(mappedBy = "service")
    List<SubService> subServices;

    @Override
    public String toString() {
        return "Service{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
