package Patika.VeterinaryManagementSystem.dto.request;

import Patika.VeterinaryManagementSystem.entity.Appointment;
import Patika.VeterinaryManagementSystem.entity.AvailableDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorRequest {

    private String name;
    private String phone;
    private String mail;
    private String address;
    private String city;
    private List<Appointment> appointmentList;
    private List<AvailableDate> availableDateList;

}
