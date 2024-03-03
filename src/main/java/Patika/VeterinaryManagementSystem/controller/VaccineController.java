package Patika.VeterinaryManagementSystem.controller;

import Patika.VeterinaryManagementSystem.dto.request.VaccineRequest;
import Patika.VeterinaryManagementSystem.dto.response.VaccineResponse;
import Patika.VeterinaryManagementSystem.entity.Vaccine;
import Patika.VeterinaryManagementSystem.service.VaccineService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/vaccines")
public class VaccineController {

    private final VaccineService vaccineService;

    public VaccineController(VaccineService vaccineService) {
        this.vaccineService = vaccineService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<Vaccine> get(@PathVariable Long id) {
        Vaccine vaccine = vaccineService.get(id);
        if (vaccine != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(vaccine);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Vaccine> save(@RequestBody VaccineRequest vaccineRequest) {
        Vaccine savedVaccine = vaccineService.save(vaccineRequest);
        if (savedVaccine != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVaccine);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<Vaccine> update(@PathVariable Long id, @RequestBody VaccineRequest vaccineRequest) {
        Vaccine updatedVaccine = vaccineService.update(id, vaccineRequest);
        if (updatedVaccine != null) {
            return ResponseEntity.status(HttpStatus.OK).body(updatedVaccine);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return vaccineService.delete(id);
    }

    @GetMapping("/searchByVaccine")
    public ResponseEntity<List<Vaccine>> findAnimalsByVaccineProtectionFinishDate(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        List<Vaccine> vaccineListSearchByVaccineProtectionFinishDate = vaccineService.findAnimalsByVaccineProtectionFinishDate(startDate, endDate);
        return ResponseEntity.ok().body(vaccineListSearchByVaccineProtectionFinishDate);
    }

    @GetMapping("/searchByAnimal")
    public ResponseEntity<List<Vaccine>> findVaccinesByAnimal(@RequestParam Long id) {
        List<Vaccine> vaccineListSearchByAnimal = vaccineService.findVaccinesByAnimal(id);
        return ResponseEntity.ok().body(vaccineListSearchByAnimal);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Vaccine>> findAllVaccines() {
        List<Vaccine> vaccineList = vaccineService.findAllVaccines();

        return ResponseEntity.status(HttpStatus.CREATED).body(vaccineList);
    }

    @GetMapping()
    public Page<VaccineResponse> cursor(Pageable pageable) {
        return vaccineService.cursor(pageable);
    }
}
