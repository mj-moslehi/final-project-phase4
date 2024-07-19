package ir.moslehi.finalprojectphase4.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder

public class Customer extends Person{

    Long validity;

    Date dateOfSignUp;

    @OneToMany(mappedBy = "customer" )
    List<Orders> orders;

    @OneToMany(mappedBy = "customer" )
    List<Comment> comments;

    @OneToMany(mappedBy = "customer")
    List<CartInfo> cartInfos;

    @Override
    public String toString() {
        return "Customer{" +
                "validity=" + validity +
                ", dateOfSingUp=" + dateOfSignUp +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                '}';
    }
}
