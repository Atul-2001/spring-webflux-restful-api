package com.signature.service;

import com.signature.model.Customer;
import com.signature.repository.CustomerRepository;
import com.signature.service.impl.CustomerServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class CustomerServiceTest {

  private CustomerService customerService;

  @Mock
  public CustomerRepository customerRepository;

  @BeforeEach
  void setUp() {
    customerService = new CustomerServiceImpl(customerRepository);
  }

  @Test
  @Order(1)
  void addCustomer() {
    //given
    Customer customer = new Customer(ObjectId.get().toString(), "Atul", "Singh");

    //when
    when(customerRepository.save(any(Customer.class))).thenReturn(Mono.just(customer));

    //then
    Customer savedCustomer = customerService.addCustomer(customer).block();

    verify(customerRepository, times(1)).save(any(Customer.class));

    assertNotNull(savedCustomer, "Failed to save customer!");

    assertEquals("Atul", savedCustomer.getFirstName());
    assertEquals("Singh", savedCustomer.getLastName());
  }

  @Test
  @Order(2)
  void updateCustomer() throws Exception {
    //given
    final String customerId = ObjectId.get().toString();

    Customer updated = new Customer(customerId, "Rishu", "Singh");

    //when
    when(customerRepository.save(any(Customer.class))).thenReturn(Mono.just(updated));

    //then
    Customer updatedCustomer = customerService.updateCustomer(updated).block();

    verify(customerRepository, times(1)).save(any(Customer.class));

    assertNotNull(updatedCustomer, "Failed to update customer!");

    assertEquals(customerId, updatedCustomer.getId());
    assertEquals("Rishu", updatedCustomer.getFirstName());
    assertEquals("Singh", updatedCustomer.getLastName());
  }

  @Test
  @Order(3)
  void getCustomer() throws Exception {
    //given
    final String customerId = ObjectId.get().toString();
    Customer customer = new Customer(customerId, "Atul", "Singh");

    //when
    when(customerRepository.findById(anyString())).thenReturn(Mono.just(customer));

    //then
    Customer customer1 = customerService.getCustomer(customerId).block();

    assertNotNull(customer1, "Null customer returned!");

    verify(customerRepository, times(1)).findById(anyString());
  }

  @Test
  @Order(4)
  void getAllCustomers() {
    //given
    Flux<Customer> customers = Flux.just(new Customer(), new Customer(), new Customer());

    //when
    when(customerRepository.findAll()).thenReturn(customers);

    //then
    List<Customer> customerList = customerService.getAllCustomers().collectList().block();

    assertEquals(customers.collectList().block().size(), customerList.size());

    verify(customerRepository, times(1)).findAll();
  }

  @Test
  @Order(5)
  void deleteCustomer() {
    //when
    when(customerRepository.deleteById(anyString())).thenReturn(Mono.empty());

    //then
    customerService.deleteCustomer(ObjectId.get().toString()).block();

    verify(customerRepository, times(1)).deleteById(anyString());
  }
}