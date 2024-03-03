package Patika.VeterinaryManagementSystem.service;

import Patika.VeterinaryManagementSystem.dto.request.AnimalRequest;
import Patika.VeterinaryManagementSystem.dto.response.AnimalResponse;
import Patika.VeterinaryManagementSystem.entity.Animal;
import Patika.VeterinaryManagementSystem.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final ModelMapper modelMapper;


    public Animal get(Long id) {
        return animalRepository.findById(id).orElseThrow(() -> new RuntimeException("ID: " + id + " ile hayvan bulunamadı!"));
    }

    public Animal save(AnimalRequest animalRequest) {
        Optional<Animal> animal = animalRepository.findByNameAndSpeciesAndGenderAndDateOfBirth(
                animalRequest.getName(),
                animalRequest.getSpecies(),
                animalRequest.getGender(),
                animalRequest.getDateOfBirth()
        );

        if (animal.isPresent()) {
            throw new RuntimeException("Bu özelliklere sahip bir hayvan zaten kayıtlı.");
        }

        Animal newAnimal = modelMapper.map(animalRequest, Animal.class);
        return animalRepository.save(newAnimal);
    }

    public Animal update(Long id, AnimalRequest animalRequest) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID: " + id + " ile hayvan bulunamadı!"));

       /* if (!animal.getCustomer().getId().equals(animalRequest.getCustomerId())) {
            throw new RuntimeException("Hayvanın sahibi değiştirilemez.");
        }*/

        modelMapper.map(animalRequest, animal);
        return animalRepository.save(animal);
    }

    public String delete(Long id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID: " + id + " ile hayvan bulunamadı!"));

        animalRepository.delete(animal);
        return "Hayvan silindi.";
    }

    public List<Animal> findAllAnimals() {
        return animalRepository.findAll();
    }

    public List<Animal> findAnimalsByName(String name) {
        return animalRepository.findByNameContaining(name);
    }

    public List<Animal> findAnimalsByCustomer(Long customerId) {
        return animalRepository.findByCustomerId(customerId);
    }

    public Page<AnimalResponse> cursor(Pageable pageable) {
        Page<Animal> animals = animalRepository.findAll(pageable);
        return animals.map(animal -> modelMapper.map(animal, AnimalResponse.class));
    }
}
