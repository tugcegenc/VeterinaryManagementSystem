package Patika.VeterinaryManagementSystem.service;

import Patika.VeterinaryManagementSystem.dto.request.CustomerRequest;
import Patika.VeterinaryManagementSystem.dto.response.CustomerResponse;
import Patika.VeterinaryManagementSystem.entity.Customer;
import Patika.VeterinaryManagementSystem.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public CustomerService(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public Customer save(CustomerRequest customerRequest) {
        customerRepository.findByNameAndMail(customerRequest.getName(), customerRequest.getMail()).ifPresent(s -> {
            throw new RuntimeException("Bu müşteri zaten kaydedilmiş.");
        });

        Customer customer = modelMapper.map(customerRequest, Customer.class);
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer update(Long id, CustomerRequest customerRequest) {
        Customer customerToUpdate = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Müşteri ID ile bulunamadı: " + id));

        customerRepository.findByNameAndMailAndIdNot(customerRequest.getName(), customerRequest.getMail(), id)
                .ifPresent(s -> {
                    throw new RuntimeException("Bu müşteri bilgileri başka bir kayıtta kullanılmaktadır.");
                });

        modelMapper.map(customerRequest, customerToUpdate);
        return customerRepository.save(customerToUpdate);
    }

    @Transactional
    public String delete(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Müşteri ID ile bulunamadı: " + id));

        customerRepository.delete(customer);
        return "Müşteri silindi: " + id;
    }

    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer get(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Müşteri ID ile bulunamadı: " + id));
    }

    public List<Customer> findCustomersByName(String name) {
        return customerRepository.findByNameContaining(name);
    }

    public Page<CustomerResponse> cursor(Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        return customerPage.map(customer -> modelMapper.map(customer, CustomerResponse.class));
    }
}
