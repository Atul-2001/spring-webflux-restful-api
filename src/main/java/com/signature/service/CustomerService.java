package com.signature.service;

import com.signature.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CustomerService {

  Mono<Customer> addCustomer(Customer customer);

  Mono<Customer> updateCustomer(Customer customer);

  Mono<Customer> patchCustomer(Customer customer);

  Mono<Customer> getCustomer(String customerId);

  Flux<Customer> getAllCustomers();

  Mono<Void> deleteCustomer(String customerId);
}
