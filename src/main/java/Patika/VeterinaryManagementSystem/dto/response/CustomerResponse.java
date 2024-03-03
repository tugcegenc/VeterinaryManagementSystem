package Patika.VeterinaryManagementSystem.dto.response;

import Patika.VeterinaryManagementSystem.entity.Animal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private Long id;
    private String name;
    private String phone;
    private String mail;
    private List<Animal> animalList;

}

