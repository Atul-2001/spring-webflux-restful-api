package com.signature.service;

import com.signature.bootstrap.Bootstrap;
import com.signature.model.Customer;
import com.signature.repository.CategoryRepository;
import com.signature.repository.CustomerRepository;
import com.signature.repository.VendorRepository;
import com.signature.service.impl.CustomerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@DataMongoTest
@TestMethodOrder(OrderAnnotation.class)
class CustomerServiceITest {

  private CustomerService customerService;

  @Autowired
  public CategoryRepository categoryRepository;

  @Autowired
  public CustomerRepository customerRepository;

  @Autowired
  public VendorRepository vendorRepository;

  private List<String> customerIds;

  @BeforeEach
  public void setUp() throws Exception {
    log.info("Started loading initial data");

    new Bootstrap(categoryRepository, customerRepository, vendorRepository).run();

    log.info("Finished loading initial data");

    customerService = new CustomerServiceImpl(customerRepository);

    customerIds = customerService.getAllCustomers().map(Customer::getId).collectList().block();
  }

  @Test
  @Order(1)
  @DirtiesContext
  void addCustomer() {
    Customer customer = new Customer("Pranjal", "Singh");

    Customer savedCustomer = customerService.addCustomer(customer).block();

    assertNotNull(savedCustomer, "Failed to save customer!");

    assertEquals("Pranjal", savedCustomer.getFirstName());
    assertEquals("Singh", savedCustomer.getLastName());
  }

  @Test
  @Order(2)
  @DirtiesContext
  void updateCustomer() throws Exception {
    final String customerId = customerIds.get(1);

    Customer customer = new Customer(customerId, "Pranjal", "Singh");

    Customer updated = customerService.updateCustomer(customer).block();

    assertNotNull(updated, "Failed to update customer!");

    assertEquals(customerId, updated.getId());
    assertEquals("Pranjal", updated.getFirstName());
    assertEquals("Singh", updated.getLastName());
  }

  @Test
  @Order(3)
  @DirtiesContext
  public void patchCustomerUpdateFirstName() throws Exception {
    final String customerId = customerIds.get(1);

    final String updatedFirstName = "Pranjal";

    Customer originalCustomer = customerService.getCustomer(customerId).block();

    assertNotNull(originalCustomer, "Failed to get customer!");

    //save original first name
    String originalFirstName = originalCustomer.getFirstName();
    String originalLastName = originalCustomer.getLastName();

    Customer customer = new Customer();
    customer.setId(customerId);
    customer.setFirstName(updatedFirstName);

    Customer updatedCustomer = customerService.patchCustomer(customer).block();

    assertNotNull(updatedCustomer, "Failed to patch customer!");

    assertEquals(updatedFirstName, updatedCustomer.getFirstName());
    assertThat(originalFirstName, not(equalTo(updatedCustomer.getFirstName())));
    assertThat(originalLastName, equalTo(updatedCustomer.getLastName()));
  }

  @Test
  @Order(4)
  @DirtiesContext
  public void patchCustomerUpdateLastName() throws Exception {
    final String customerId = customerIds.get(1);

    final String updatedLastName = "Suryavanshi";

    Customer originalCustomer = customerService.getCustomer(customerId).block();

    assertNotNull(originalCustomer, "Failed to get customer!");

    //save original last name
    String originalFirstName = originalCustomer.getFirstName();
    String originalLastName = originalCustomer.getLastName();

    Customer customer = new Customer();
    customer.setId(customerId);
    customer.setLastName(updatedLastName);

    Customer updatedCustomer = customerService.patchCustomer(customer).block();

    assertNotNull(updatedCustomer, "Failed to patch customer!");

    assertEquals(updatedLastName, updatedCustomer.getLastName());
    assertThat(originalFirstName, equalTo(updatedCustomer.getFirstName()));
    assertThat(originalLastName, not(equalTo(updatedCustomer.getLastName())));
  }

  @Test
  @Order(5)
  @DirtiesContext
  void getCustomer() throws Exception {
    Customer customer = customerService.getCustomer(customerIds.get(0)).block();

    assertNotNull(customer, "Null customer returned!");
  }

  @Test
  @Order(6)
  @DirtiesContext
  void getAllCustomers() {
    List<Customer> customers = customerService.getAllCustomers().collectList().block();

    assertNotNull(customers, "Null customers returned!");
  }

  @Test
  @Order(7)
  @DirtiesContext
  void deleteCustomer() {
    Long count = customerRepository.count().block();

    customerService.deleteCustomer(customerIds.get(0)).block();

    assertEquals(count - 1, customerRepository.count().block());
  }
}