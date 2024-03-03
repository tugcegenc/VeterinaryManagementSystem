package Patika.VeterinaryManagementSystem.controller;

import Patika.VeterinaryManagementSystem.dto.request.AnimalRequest;
import Patika.VeterinaryManagementSystem.dto.response.AnimalResponse;
import Patika.VeterinaryManagementSystem.entity.Animal;
import Patika.VeterinaryManagementSystem.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/animals")
@RequiredArgsConstructor
public class AnimalController {

    public final AnimalService animalService;


    @PostMapping
    public ResponseEntity<Animal> save(@RequestBody AnimalRequest animalRequestDto) {
        Animal savedAnimal = animalService.save(animalRequestDto);
        if (savedAnimal != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAnimal);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Animal> get(@PathVariable Long id) {
        Animal animal = animalService.get(id);
        if (animal != null) {
            return ResponseEntity.ok().body(animal);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Animal> update(@PathVariable Long id, @RequestBody AnimalRequest animalRequestDto) {
        Animal updatedAnimal = animalService.update(id, animalRequestDto);
        if (updatedAnimal != null) {
            return ResponseEntity.status(HttpStatus.OK).body(updatedAnimal);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return animalService.delete(id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Animal>> findAllAnimals() {
        List<Animal> animalList = animalService.findAllAnimals();
        return ResponseEntity.ok().body(animalList);
    }

    @GetMapping("/searchByName")
    public ResponseEntity<List<Animal>> findAnimalsByName(@RequestParam String name) {
        List<Animal> animalList = animalService.findAnimalsByName(name);
        return ResponseEntity.ok().body(animalList);
    }

    @GetMapping("/searchByCustomer")
    public ResponseEntity<List<Animal>> findAnimalsByCustomer(@RequestParam Long id) {
        List<Animal> animalList = animalService.findAnimalsByCustomer(id);
        return ResponseEntity.ok().body(animalList);
    }
    @GetMapping()
    public Page<AnimalResponse> cursor(Pageable pageable) {
        return animalService.cursor(pageable);
    }


}