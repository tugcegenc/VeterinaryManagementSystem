package Patika.VeterinaryManagementSystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "animal")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id" , columnDefinition = "serial")
    private Long id;

    @Column(name = "name" )
    private String name;

    @Column(name = "species")
    private String species;

    @Column(name = "breed" )
    private String breed;

    @Column(name = "gender" )
    private String gender;

    @Column(name = "colour")
    private String colour;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @OneToMany(mappedBy = "animal", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Vaccine> vaccineList;

    @OneToMany(mappedBy = "animal", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    // @JsonIgnore
    private List<Appointment> appointmentList;
}
