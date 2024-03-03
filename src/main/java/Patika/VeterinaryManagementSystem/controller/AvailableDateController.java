package Patika.VeterinaryManagementSystem.controller;

import Patika.VeterinaryManagementSystem.dto.request.AvailableDateRequest;
import Patika.VeterinaryManagementSystem.dto.response.AvailableDateResponse;
import Patika.VeterinaryManagementSystem.entity.AvailableDate;
import Patika.VeterinaryManagementSystem.service.AvailableDateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/available_dates")
public class AvailableDateController {

    public final AvailableDateService availableDateService;

    public AvailableDateController(AvailableDateService availableDateService) {
        this.availableDateService = availableDateService;
    }

    @GetMapping("/all")
    public List<AvailableDate> findAllAvailableDates() {
        return availableDateService.findAllAvailableDates();
    }

    @GetMapping("/{id}")
    public AvailableDate get(@PathVariable Long id) {
        return availableDateService.get(id);
    }

    @GetMapping()
    public Page<AvailableDateResponse> cursor(Pageable pageable) {
        return availableDateService.cursor(pageable);
    }

    @PostMapping
    public AvailableDate save(@RequestBody AvailableDateRequest availableDateRequest) {
        return availableDateService.save(availableDateRequest);
    }

    @PutMapping("/{id}")
    public AvailableDate update(@PathVariable Long id, @RequestBody AvailableDateRequest availableDateRequest) {
        return availableDateService.update(id, availableDateRequest);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return availableDateService.delete(id);
    }
}