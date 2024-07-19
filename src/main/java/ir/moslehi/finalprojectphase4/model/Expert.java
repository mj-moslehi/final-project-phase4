package ir.moslehi.finalprojectphase4.model;

import ir.moslehi.finalprojectphase4.model.enums.ExpertStatus;
import jakarta.persistence.*;
import lombok.*;
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

public class Expert extends Person {

    @Lob
    byte[] image;

    Double score;

    @Enumerated(EnumType.STRING)
    ExpertStatus expertStatus;

    Long validity;

    Date dateOfSignUp;

    @OneToMany(mappedBy = "expert")
    List<Orders> orders;

    @OneToMany(mappedBy = "expert" )
    List<ExpertSubService> expert_subServices;

    @OneToMany(mappedBy = "expert")
    List<Comment> comments;

    @OneToMany(mappedBy = "expert")
    List<Suggestion> suggestions;

    @Override
    public String toString() {
        return "Expert{" +
                "score=" + score +
                ", expertStatus=" + expertStatus +
                ", validity=" + validity +
                ", dateOfSingUp=" + dateOfSignUp +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                '}';
    }
}
