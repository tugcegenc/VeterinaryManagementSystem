package Patika.VeterinaryManagementSystem.dto.request;

import Patika.VeterinaryManagementSystem.entity.Appointment;
import Patika.VeterinaryManagementSystem.entity.Vaccine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalRequest {

    private String name;
    private String species;
    private String breed;
    private String gender;
    private String colour;
    private Long customerId;
    private LocalDate dateOfBirth;
    private List<Vaccine> vaccineList;
    private List<Appointment> appointmentList;


}
