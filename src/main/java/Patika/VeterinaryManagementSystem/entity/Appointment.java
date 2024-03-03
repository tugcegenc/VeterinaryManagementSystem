package Patika.VeterinaryManagementSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "serial",nullable = false)
    private Long id;

    @Column(name = "appointment_date")
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.EAGER)
    //@JsonIgnore
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctor;

    @ManyToOne (fetch = FetchType.EAGER)
    //@JsonIgnore
    @JoinColumn (name = "animal_id")
    private Animal animal;


}
