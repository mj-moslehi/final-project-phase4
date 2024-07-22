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

public class CartInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String cartNumber;

    @Column(unique = true)
    String cvv2;

    String password;

    Date expirationDate;

    String captchaAnswer;

    String hiddenCaptcha;

    @Lob
    byte[] captchaImage;

    @ManyToOne
    Customer customer;

    @Override
    public String toString() {
        return "CartInfo{" +
                "cartNumber='" + cartNumber + '\'' +
                ", cvv2='" + cvv2 + '\'' +
                ", password='" + password + '\'' +
                ", expirationDate=" + expirationDate +
                ", id=" + id +
                '}';
    }
}
