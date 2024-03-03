package Patika.VeterinaryManagementSystem.dto.request;

import Patika.VeterinaryManagementSystem.entity.Doctor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableDateRequest {

    private LocalDate availableDate;
    private Long doctorId;
    private Doctor doctor;

}
