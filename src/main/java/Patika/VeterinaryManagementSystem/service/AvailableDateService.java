package Patika.VeterinaryManagementSystem.service;

import Patika.VeterinaryManagementSystem.dto.request.AvailableDateRequest;
import Patika.VeterinaryManagementSystem.dto.response.AvailableDateResponse;
import Patika.VeterinaryManagementSystem.entity.AvailableDate;
import Patika.VeterinaryManagementSystem.entity.Doctor;
import Patika.VeterinaryManagementSystem.repository.AvailableDateRepository;
import Patika.VeterinaryManagementSystem.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AvailableDateService {
    private final AvailableDateRepository availableDateRepository;
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;

    public List<AvailableDate> findAllAvailableDates() {
        return availableDateRepository.findAll();
    }

    public AvailableDate get(Long id) {
        return availableDateRepository.findById(id).orElseThrow(() -> new RuntimeException(id + " id ile ilgili müsait tarih bulunamadı!"));
    }

    public AvailableDate save(AvailableDateRequest availableDateRequest) {
        Doctor doctor = doctorRepository.findById(availableDateRequest.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doktor bulunamadı ID: " + availableDateRequest.getDoctorId()));

        availableDateRepository.findByDoctorIdAndAvailableDate(availableDateRequest.getDoctorId(), availableDateRequest.getAvailableDate())
                .ifPresent(a -> {
                    throw new RuntimeException("Bu tarih için doktorun zaten müsait gün kaydı var.");
                });

        AvailableDate newAvailableDate = new AvailableDate();
        newAvailableDate.setAvailableDate(availableDateRequest.getAvailableDate());
        newAvailableDate.setDoctor(doctor);

        return availableDateRepository.save(newAvailableDate);
    }

    public AvailableDate update(Long id, AvailableDateRequest availableDateRequest) {
        AvailableDate existingAvailableDate = availableDateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(id + " id ile ilgili müsait tarih bulunamadı!"));

        Optional<AvailableDate> existingDate = availableDateRepository.findByDoctorIdAndAvailableDate(
                availableDateRequest.getDoctorId(), availableDateRequest.getAvailableDate());

        if (existingDate.isPresent() && !existingDate.get().getId().equals(id)) {
            throw new RuntimeException("Bu müsait tarih zaten kayıtlı.");
        }

        existingAvailableDate.setAvailableDate(availableDateRequest.getAvailableDate());
        if (availableDateRequest.getDoctorId() != null) {
            Doctor doctor = doctorRepository.findById(availableDateRequest.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("ID: " + availableDateRequest.getDoctorId() + " ile doktor bulunamadı!"));
            existingAvailableDate.setDoctor(doctor);
        }

        return availableDateRepository.save(existingAvailableDate);
    }


    public String delete(Long id) {
        AvailableDate availableDate = availableDateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(id + " id ile ilgili müsait tarih bulunamadı."));

        availableDateRepository.delete(availableDate);
        return "Id: " + id + " müsait tarih silindi.";
    }

    public Page<AvailableDateResponse> cursor(Pageable pageable) {
        Page<AvailableDate> availableDates = availableDateRepository.findAll(pageable);
        return availableDates.map(availableDate -> modelMapper.map(availableDate, AvailableDateResponse.class));
    }

    public Optional<AvailableDate> findByDoctorIdAndDate(Long doctorId, LocalDate date) {
        return availableDateRepository.findByDoctorIdAndAvailableDate(doctorId, date);
    }
}
