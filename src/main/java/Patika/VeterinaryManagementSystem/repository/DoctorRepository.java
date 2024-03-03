package Patika.VeterinaryManagementSystem.repository;

import Patika.VeterinaryManagementSystem.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByNameAndMail(String name, String mail);

    Optional<Doctor> findById(Long id);

}
