package Patika.VeterinaryManagementSystem.service;

import Patika.VeterinaryManagementSystem.dto.request.VaccineRequest;
import Patika.VeterinaryManagementSystem.dto.response.VaccineResponse;
import Patika.VeterinaryManagementSystem.entity.Animal;
import Patika.VeterinaryManagementSystem.entity.Vaccine;
import Patika.VeterinaryManagementSystem.repository.AnimalRepository;
import Patika.VeterinaryManagementSystem.repository.VaccineRepository;
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
public class VaccineService {

    private final VaccineRepository vaccineRepository;
    private final AnimalRepository animalRepository;
    private final ModelMapper modelMapper;

    public Vaccine get(Long id) {
        return vaccineRepository.findById(id).orElseThrow(() -> new RuntimeException("ID ile aşı bulunamadı: " + id));
    }

    public Vaccine save(VaccineRequest vaccineRequest) {
        Animal animal = animalRepository.findById(vaccineRequest.getAnimalId())
                .orElseThrow(() -> new RuntimeException("Belirtilen animal_id değerine sahip hayvan kaydı bulunamadı: " + vaccineRequest.getAnimalId()));

        if (animal == null) {
            throw new RuntimeException("Belirtilen animal_id değerine sahip hayvan kaydı bulunamadı: " + vaccineRequest.getAnimalId());
        }

        Optional<Vaccine> optionalVaccine = vaccineRepository.findByNameAndCodeAndAnimalIdAndProtectionFinishDateGreaterThanEqual(
                vaccineRequest.getName(), vaccineRequest.getCode(), vaccineRequest.getAnimalId(), vaccineRequest.getProtectionStartDate());

        if (optionalVaccine.isPresent()) {
            throw new RuntimeException("Bu hayvan için kaydetmek istediğiniz aşı hala koruyucudur.");
        }

        Vaccine newVaccine = modelMapper.map(vaccineRequest, Vaccine.class);
        if (newVaccine.getProtectionStartDate().isAfter(newVaccine.getProtectionFinishDate())) {
            throw new RuntimeException("Aşı başlangıç tarihi bitiş tarihinden büyük olamaz.");
        }

        return vaccineRepository.save(newVaccine);
    }

    public Vaccine update(Long id, VaccineRequest vaccineRequest) {
        Vaccine existingVaccine = vaccineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID ile aşı bulunamadı: " + id));

        Optional<Vaccine> optionalVaccine = vaccineRepository.findByNameAndCodeAndAnimalIdAndProtectionFinishDateGreaterThanEqual(
                vaccineRequest.getName(), vaccineRequest.getCode(), vaccineRequest.getAnimalId(), vaccineRequest.getProtectionStartDate());

        if (optionalVaccine.isPresent() && !optionalVaccine.get().getId().equals(id)) {
            throw new RuntimeException("Bu aşı zaten kaydedilmiş.");
        }

        existingVaccine.setName(vaccineRequest.getName());
        existingVaccine.setCode(vaccineRequest.getCode());
        existingVaccine.setProtectionStartDate(vaccineRequest.getProtectionStartDate());
        existingVaccine.setProtectionFinishDate(vaccineRequest.getProtectionFinishDate());
        if (vaccineRequest.getAnimalId() != null) {
            Animal animal = animalRepository.findById(vaccineRequest.getAnimalId())
                    .orElseThrow(() -> new RuntimeException("ID: " + vaccineRequest.getAnimalId() + " ile hayvan bulunamadı!"));
            existingVaccine.setAnimal(animal);
        }

        return vaccineRepository.save(existingVaccine);
    }

    public String delete(Long id) {
        Vaccine vaccine = vaccineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bu aşı bulunamadı: " + id));

        vaccineRepository.delete(vaccine);
        return "Id: " + id + " aşı silindi.";
    }

    public List<Vaccine> findVaccinesByAnimal(Long id) {
        return vaccineRepository.findByAnimalId(id);
    }


    public List<Vaccine> findAnimalsByVaccineProtectionFinishDate(LocalDate startDate, LocalDate endDate) {
        return vaccineRepository.findByProtectionFinishDateBetween(startDate, endDate);
    }


    public List<Vaccine> findAllVaccines() {
        return vaccineRepository.findAll();
    }

    public Page<VaccineResponse> cursor(Pageable pageable) {
        Page<Vaccine> vaccinePage = vaccineRepository.findAll(pageable);
        return vaccinePage.map(vaccine -> modelMapper.map(vaccine, VaccineResponse.class));
    }
}
