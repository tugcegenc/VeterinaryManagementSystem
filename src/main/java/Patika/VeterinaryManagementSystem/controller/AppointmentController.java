package Patika.VeterinaryManagementSystem.controller;

import Patika.VeterinaryManagementSystem.dto.request.AppointmentRequest;
import Patika.VeterinaryManagementSystem.dto.response.AppointmentResponse;
import Patika.VeterinaryManagementSystem.entity.Appointment;
import Patika.VeterinaryManagementSystem.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;


    @GetMapping("/{id}")
    public ResponseEntity<Appointment> get(@PathVariable Long id) {
        Appointment appointment = appointmentService.get(id);
        if (appointment != null) {
            return ResponseEntity.ok().body(appointment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    public ResponseEntity<Appointment> save(@RequestBody AppointmentRequest appointmentRequest) {
        Appointment savedAppointment = appointmentService.save(appointmentRequest);
        if (savedAppointment != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAppointment);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> update(@PathVariable Long id, @RequestBody AppointmentRequest appointmentRequest) {
        Appointment updatedAppointment = appointmentService.update(id, appointmentRequest);
        if (updatedAppointment != null) {
            return ResponseEntity.status(HttpStatus.OK).body(updatedAppointment);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return appointmentService.delete(id);
    }

    @GetMapping("/searchByDoctorAndDate")
    public ResponseEntity<List<Appointment>> findAppointmentByDoctorIdAndDate(@RequestParam(value = "id", required = false) Long id, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        List<Appointment> appointments = appointmentService.findAppointmentByDoctorIdAndDate(id, startDate, endDate);
        if (appointments != null) {
            return ResponseEntity.ok().body(appointments);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/searchByAnimalAndDate")
    public ResponseEntity<List<Appointment>> findAppointmentByAnimalIdAndDate(
            @RequestParam(value = "id", required = false) Long id, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        List<Appointment> appointments = appointmentService.findAppointmentByAnimalIdAndDate(id, startDate, endDate);
        if (appointments != null) {
            return ResponseEntity.ok().body(appointments);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Appointment>> findAllAppointments() {
        List<Appointment> appointmentList = appointmentService.findAllAppointments();

        return ResponseEntity.ok().body(appointmentList);
    }

    @GetMapping()
    public Page<AppointmentResponse> cursor(Pageable pageable) {
        return appointmentService.cursor(pageable);
    }


}