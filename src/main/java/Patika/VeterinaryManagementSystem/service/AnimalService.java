package Patika.VeterinaryManagementSystem.service;

import Patika.VeterinaryManagementSystem.dto.request.AnimalRequest;
import Patika.VeterinaryManagementSystem.dto.response.AnimalResponse;
import Patika.VeterinaryManagementSystem.entity.Animal;
import Patika.VeterinaryManagementSystem.entity.Customer;
import Patika.VeterinaryManagementSystem.repository.AnimalRepository;
import Patika.VeterinaryManagementSystem.repository.CustomerRepository;
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
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public Animal get(Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID: " + id + " ile hayvan bulunamadı!"));
    }

    public Animal save(AnimalRequest animalRequest) {
        if (animalRequest.getCustomerId() == null) {
            throw new IllegalArgumentException("Hayvanı kaydederken bir müşteri ID'si sağlanmalıdır.");
        }

        Optional<Animal> existingAnimal = animalRepository.findByNameAndSpeciesAndGenderAndDateOfBirth(
                animalRequest.getName(),
                animalRequest.getSpecies(),
                animalRequest.getGender(),
                animalRequest.getDateOfBirth()
        );

        if (existingAnimal.isPresent()) {
            throw new RuntimeException("Bu özelliklere sahip bir hayvan zaten kayıtlı.");
        }

        Customer customer = customerRepository.findById(animalRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("ID: " + animalRequest.getCustomerId() + " ile müşteri bulunamadı!"));

        Animal newAnimal = modelMapper.map(animalRequest, Animal.class);
        newAnimal.setCustomer(customer);
        newAnimal.setId(null);
        return animalRepository.save(newAnimal);
    }

    public Animal update(Long id, AnimalRequest animalRequest) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID: " + id + " ile hayvan bulunamadı!"));

        animal.setName(animalRequest.getName());
        animal.setSpecies(animalRequest.getSpecies());
        animal.setBreed(animalRequest.getBreed());
        animal.setGender(animalRequest.getGender());
        animal.setColour(animalRequest.getColour());
        animal.setDateOfBirth(animalRequest.getDateOfBirth());
        if (animalRequest.getCustomerId() != null) {
            Customer customer = customerRepository.findById(animalRequest.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("ID: " + animalRequest.getCustomerId() + " ile müşteri bulunamadı!"));
            animal.setCustomer(customer);
        }


        return animalRepository.save(animal);
    }

    public String delete(Long id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID: " + id + " ile hayvan bulunamadı!"));

        animalRepository.delete(animal);
        return "id: " + id + " hayvan silindi.";
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
