package Patika.VeterinaryManagementSystem.controller;

import Patika.VeterinaryManagementSystem.dto.request.DoctorRequest;
import Patika.VeterinaryManagementSystem.dto.response.DoctorResponse;
import Patika.VeterinaryManagementSystem.entity.Doctor;
import Patika.VeterinaryManagementSystem.service.DoctorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/{id}")
    public Doctor get (@PathVariable Long id){
        return doctorService.get(id);
    }

    @PostMapping
    public Doctor save (@RequestBody DoctorRequest doctorRequest){
        return doctorService.save(doctorRequest);
    }

    @PutMapping("/{id}")
    public Doctor update (@PathVariable Long id, @RequestBody DoctorRequest doctorRequest){
        return doctorService.update(id,doctorRequest);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id){
        return doctorService.delete(id);
    }
    @GetMapping("/all")
    public List<Doctor> findAllDoctors (){
        return doctorService.findAllDoctors();
    }
    @GetMapping()
    public Page<DoctorResponse> cursor(Pageable pageable) {
        return doctorService.cursor(pageable);
    }
}
