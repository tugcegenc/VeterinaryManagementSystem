package Patika.VeterinaryManagementSystem.controller;

import Patika.VeterinaryManagementSystem.dto.request.CustomerRequest;
import Patika.VeterinaryManagementSystem.dto.response.CustomerResponse;
import Patika.VeterinaryManagementSystem.entity.Customer;
import Patika.VeterinaryManagementSystem.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")

public class CustomerController {

    public final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @PostMapping
    public Customer save(@RequestBody CustomerRequest customerRequest) {
        return customerService.save(customerRequest);
    }

    @PutMapping("/{id}")
    public Customer update(@PathVariable Long id, @RequestBody CustomerRequest customerRequest) {
        return customerService.update(id, customerRequest);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return customerService.delete(id);
    }

    @GetMapping("/{id}")
    public Customer get(@PathVariable Long id) {
        return customerService.get(id);
    }

    @GetMapping("/searchByName")
    public List<Customer> findCustomersByName(@RequestParam String name) {
        return customerService.findCustomersByName(name);
    }

    @GetMapping("/all")
    public List<Customer> findAllCustomers() {
        return customerService.findAllCustomers();
    }


    @GetMapping()
    public Page<CustomerResponse> cursor(Pageable pageable) {
        return customerService.cursor(pageable);
    }
}