package Patika.VeterinaryManagementSystem.service;

import Patika.VeterinaryManagementSystem.dto.request.DoctorRequest;
import Patika.VeterinaryManagementSystem.dto.response.DoctorResponse;
import Patika.VeterinaryManagementSystem.entity.Doctor;
import Patika.VeterinaryManagementSystem.repository.DoctorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;

    public DoctorService(DoctorRepository doctorRepository, ModelMapper modelMapper) {
        this.doctorRepository = doctorRepository;
        this.modelMapper = modelMapper;
    }


    public Doctor get(Long id) {
        return doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Doktor bulunamadı. ID: " + id));
    }

    public Doctor save(DoctorRequest doctorRequest) {
        Optional<Doctor> savedDoctor = doctorRepository.findByNameAndMail(doctorRequest.getName(), doctorRequest.getMail());

        if (savedDoctor.isPresent()) {
            throw new RuntimeException("Doktor zaten kaydedilmiş.");
        }
        Doctor doctor = modelMapper.map(doctorRequest, Doctor.class);
        return doctorRepository.save(doctor);
    }

    public Doctor update(Long id, DoctorRequest doctorRequest) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        Optional<Doctor> doctorUpdate = doctorRepository.findByNameAndMail(doctorRequest.getName(), doctorRequest.getMail());

        if (doctor.isEmpty()) {
            throw new RuntimeException("Doktor bulunamadı. ID: " + id);
        }

        if (doctorUpdate.isPresent() && !doctorUpdate.get().getId().equals(id)) {
            throw new RuntimeException("Bu doktor zaten kayıtlı.");
        }

        Doctor updatedDoctor = doctor.get();
        modelMapper.map(doctorRequest, updatedDoctor);
        return doctorRepository.save(updatedDoctor);
    }

    public String delete(Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isEmpty()) {
            throw new RuntimeException("Bu doktor bulunamadı.id: " + id);
        } else {
            doctorRepository.delete(doctor.get());
            return "Doktor silindi.";
        }
    }

    public List<Doctor> findAllDoctors() {
        return doctorRepository.findAll();
    }

    public Page<DoctorResponse> cursor(Pageable pageable) {
        Page<Doctor> doctorPage = doctorRepository.findAll(pageable);
        return doctorPage.map(doctor -> modelMapper.map(doctor, DoctorResponse.class));
    }
}
