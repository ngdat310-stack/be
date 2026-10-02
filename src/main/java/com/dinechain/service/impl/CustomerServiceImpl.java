package com.dinechain.service.impl;

import com.dinechain.dto.request.CreateCustomerRequest;
import com.dinechain.dto.response.CustomerResponse;
import com.dinechain.entity.CustomerEntity;
import com.dinechain.repository.CustomerRepository;
import com.dinechain.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        if (customerRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new RuntimeException("Phone already registered");
        }

        CustomerEntity customer = new CustomerEntity();
        customer.setFullName(request.getFullName());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        customer.setBirthDate(request.getBirthDate());
        customer.setPoints(0);

        CustomerEntity saved = customerRepository.save(customer);
        log.info("Created customer: {}", saved.getFullName());
        return CustomerResponse.from(saved);
    }

    @Override
    public CustomerResponse getCustomer(Integer customerId) {
        CustomerEntity customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        return CustomerResponse.from(customer);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(Integer customerId, CreateCustomerRequest request) {
        CustomerEntity customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setBirthDate(request.getBirthDate());

        CustomerEntity updated = customerRepository.save(customer);
        return CustomerResponse.from(updated);
    }

    @Override
    @Transactional
    public void deleteCustomer(Integer customerId) {
        CustomerEntity customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        customerRepository.delete(customer);
        log.info("Deleted customer: {}", customer.getFullName());
    }

    @Override
    public List<CustomerResponse> searchCustomers(String fullName) {
        return customerRepository.findByFullNameContainingIgnoreCase(fullName)
            .stream()
            .map(CustomerResponse::from)
            .toList();
    }
}
