package Patika.VeterinaryManagementSystem.service;

import Patika.VeterinaryManagementSystem.dto.request.AppointmentRequest;
import Patika.VeterinaryManagementSystem.dto.response.AppointmentResponse;
import Patika.VeterinaryManagementSystem.entity.Animal;
import Patika.VeterinaryManagementSystem.entity.Appointment;
import Patika.VeterinaryManagementSystem.entity.AvailableDate;
import Patika.VeterinaryManagementSystem.entity.Doctor;
import Patika.VeterinaryManagementSystem.repository.AnimalRepository;
import Patika.VeterinaryManagementSystem.repository.AppointmentRepository;
import Patika.VeterinaryManagementSystem.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AnimalRepository animalRepository;
    private final DoctorRepository doctorRepository;
    private final AvailableDateService availableDateService;
    private final ModelMapper modelMapper;

    public Appointment get(Long id) {
        return appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Randevu bulunamadı. ID: " + id));
    }

    @Transactional
    public Appointment save(AppointmentRequest appointmentRequest) {
        Doctor doctor = findDoctorById(appointmentRequest.getDoctorId());
        Animal animal = findAnimalById(appointmentRequest.getAnimalId());

        Appointment appointment = modelMapper.map(appointmentRequest, Appointment.class);
        appointment.setDoctor(doctor);
        appointment.setAnimal(animal);

        LocalDateTime newAppointmentStart = appointmentRequest.getDate();
        LocalDateTime newAppointmentEnd = newAppointmentStart.plusMinutes(59).plusSeconds(59);

        List<Appointment> existingAppointments = appointmentRepository.findByDoctorId(appointmentRequest.getDoctorId());

        for (Appointment existingAppointment : existingAppointments) {
            LocalDateTime existingAppointmentStart = existingAppointment.getDate();
            LocalDateTime existingAppointmentEnd = existingAppointmentStart.plusMinutes(59).plusSeconds(59);
            if (!newAppointmentStart.isAfter(existingAppointmentEnd) && !newAppointmentEnd.isBefore(existingAppointmentStart)) {
                throw new RuntimeException("Doktorun şu anda başka bir randevusu var.");
            }
        }

        Optional<AvailableDate> existsAvailableDateByDoctorIdAndDate =
                availableDateService.findByDoctorIdAndDate(
                        appointmentRequest.getDoctorId(), appointmentRequest.getDate().toLocalDate());

        if (existsAvailableDateByDoctorIdAndDate.isEmpty()) {
            throw new RuntimeException("Doktor bu gün çalışmıyor.");
        }
        if (appointmentRequest.getAnimalId() == null) {
            throw new RuntimeException("Randevu için bir hayvan seçilmemiş.");
        }

        return appointmentRepository.save(appointment);
    }

    private Doctor findDoctorById(Long doctorId) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        return optionalDoctor.orElseThrow(() -> new RuntimeException("Doktor bulunamadı. ID: " + doctorId));
    }

    private Animal findAnimalById(Long animalId) {
        Optional<Animal> optionalAnimal = animalRepository.findById(animalId);
        return optionalAnimal.orElseThrow(() -> new RuntimeException("Hayvan bulunamadı. ID: " + animalId));
    }

    @Transactional
    public Appointment update(Long id, AppointmentRequest appointmentRequest) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı. ID: " + id));
        LocalDateTime newAppointmentStart = appointmentRequest.getDate();
        LocalDateTime newAppointmentEnd = newAppointmentStart.plusMinutes(59).plusSeconds(59);

        List<Appointment> existingAppointments = appointmentRepository.findByDoctorId(appointmentRequest.getDoctorId());

        for (Appointment existingAppointment : existingAppointments) {
            LocalDateTime existingAppointmentStart = existingAppointment.getDate();
            LocalDateTime existingAppointmentEnd = existingAppointmentStart.plusMinutes(59).plusSeconds(59);
            if (!newAppointmentStart.isAfter(existingAppointmentEnd) && !newAppointmentEnd.isBefore(existingAppointmentStart)) {
                throw new RuntimeException("Doktorun şu anda başka bir randevusu var.");
            }
        }
        Optional<Appointment> optionalAppointment = appointmentRepository.findByDateAndDoctorIdAndAnimalId(
                appointmentRequest.getDate(), appointmentRequest.getDoctorId(), appointmentRequest.getAnimalId());

        if (optionalAppointment.isPresent() && !optionalAppointment.get().getId().equals(id)) {
            throw new RuntimeException("Bu randevu zaten kaydedilmiş.");
        }

        Optional<AvailableDate> optionalAvailableDate = availableDateService.findByDoctorIdAndDate(
                appointmentRequest.getDoctorId(), appointmentRequest.getDate().toLocalDate());

        if (optionalAvailableDate.isEmpty()) {
            throw new RuntimeException("Doktor bu gün çalışmıyor.");
        }

        appointment.setDate(appointmentRequest.getDate());
        if (appointmentRequest.getDoctorId() != null && appointmentRequest.getAnimalId() != null) {
            Doctor doctor = doctorRepository.findById(appointmentRequest.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("ID: " + appointmentRequest.getDoctorId() + " ile doktor bulunamadı!"));
            appointment.setDoctor(doctor);

            Animal animal = animalRepository.findById(appointmentRequest.getAnimalId())
                    .orElseThrow(() -> new RuntimeException("ID: " + appointmentRequest.getAnimalId() + " ile hayvan bulunamadı!"));
            appointment.setAnimal(animal);
        }
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public String delete(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bu randevu bulunamadı."));

        appointmentRepository.delete(appointment);
        return "Id: " + id + " randevu silindi.";
    }

    public List<Appointment> findAppointmentByDoctorIdAndDate(Long doctorId, LocalDate startDate, LocalDate endDate) {
        if (doctorId == null) {
            return appointmentRepository.findByDateBetween(startDate.atStartOfDay(), endDate.atStartOfDay());
        }
        return appointmentRepository.findByDoctorIdAndDateBetween(doctorId, startDate.atStartOfDay(), endDate.atStartOfDay());
    }

    public List<Appointment> findAppointmentByAnimalIdAndDate(Long animalId, LocalDate startDate, LocalDate endDate) {
        if (animalId == null) {
            return appointmentRepository.findByDateBetween(startDate.atStartOfDay(), endDate.atStartOfDay());
        }
        return appointmentRepository.findByAnimalIdAndDateBetween(animalId, startDate.atStartOfDay(), endDate.atStartOfDay());
    }

    public List<Appointment> findAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Page<AppointmentResponse> cursor(Pageable pageable) {
        Page<Appointment> appointments = appointmentRepository.findAll(pageable);
        return appointments.map(appointment -> modelMapper.map(appointment, AppointmentResponse.class));
    }
}
