package com.signature.service.impl;

import com.signature.exception.ResourceNotFoundException;
import com.signature.model.Customer;
import com.signature.repository.CustomerRepository;
import com.signature.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerRepository;

  public CustomerServiceImpl(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Override
  public Mono<Customer> addCustomer(Customer customer) {
    return customerRepository.save(customer);
  }

  @Override
  public Mono<Customer> updateCustomer(Customer customer) {
    return customerRepository.save(customer);
  }

  @Override
  public Mono<Customer> patchCustomer(Customer customer) {
    return getCustomer(customer.getId())
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Customer with id " + customer.getId() + " not found")))
            .flatMap((Function<Customer, Mono<Customer>>) existingCustomer -> {
              if (customer.getFirstName() != null) {
                existingCustomer.setFirstName(customer.getFirstName());
              }
              if (customer.getLastName() != null) {
                existingCustomer.setLastName(customer.getLastName());
              }
              return customerRepository.save(existingCustomer);
            });
  }

  @Override
  public Mono<Customer> getCustomer(String customerId) {
    return customerRepository.findById(customerId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Customer with id " + customerId + " not found")));
  }

  @Override
  public Flux<Customer> getAllCustomers() {
    return customerRepository.findAll();
  }

  @Override
  public Mono<Void> deleteCustomer(String customerId) {
    return customerRepository.deleteById(customerId);
  }
}