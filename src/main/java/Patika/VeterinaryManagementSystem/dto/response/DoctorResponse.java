package Patika.VeterinaryManagementSystem.dto.response;

import Patika.VeterinaryManagementSystem.entity.Appointment;
import Patika.VeterinaryManagementSystem.entity.AvailableDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponse {
    private Long id;
    private String name;
    private String phone;
    private String mail;
    private String address;
    private String city;
    private List<Appointment> appointmentList;
    private List<AvailableDate> availableDateList;
}
