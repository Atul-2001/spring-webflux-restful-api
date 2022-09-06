package com.signature.controller.v1;

import com.signature.domain.CustomerDTO;
import com.signature.mapper.CustomerMapper;
import com.signature.model.Customer;
import com.signature.service.CustomerService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class CustomerControllerTest {

  private WebTestClient webTestClient;

  @MockBean
  public CustomerService customerService;

  @BeforeEach
  void setUp() {
    webTestClient = WebTestClient.bindToController(new CustomerController(CustomerMapper.INSTANCE, customerService)).build();
  }

  @Test
  @Order(1)
  void createCustomer() {
    //given
    Customer customer = new Customer(ObjectId.get().toString(), "Atul", "Singh");

    //when
    when(customerService.addCustomer(any(Customer.class))).thenReturn(Mono.just(customer));

    //then
    webTestClient.post()
            .uri("/api/v1/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(new CustomerDTO("Atul", "Singh")), CustomerDTO.class)
            .exchange().expectStatus().isCreated()
            .expectBody(CustomerDTO.class)
            .value(CustomerDTO::getFirstName, equalTo("Atul"))
            .value(CustomerDTO::getLastName, equalTo("Singh"))
            .value(CustomerDTO::getCustomerUrl, equalTo("/api/v1/customers/" + customer.getId()));
  }

  @Test
  @Order(2)
  void getCustomer() {
    //given
    Customer customer = new Customer(ObjectId.get().toString(), "Atul", "Singh");

    //when
    when(customerService.getCustomer(anyString())).thenReturn(Mono.just(customer));

    //then
    webTestClient.get()
            .uri("/api/v1/customers/" + customer.getId())
            .exchange().expectStatus().isOk()
            .expectBody(CustomerDTO.class)
            .value(CustomerDTO::getFirstName, equalTo("Atul"))
            .value(CustomerDTO::getLastName, equalTo("Singh"))
            .value(CustomerDTO::getCustomerUrl, equalTo("/api/v1/customers/" + customer.getId()));
  }

  @Test
  @Order(3)
  void getAllCustomers() {
    //given
    Customer customer1 = new Customer(ObjectId.get().toString(), "Rishu", "Singh");
    Customer customer2 = new Customer(ObjectId.get().toString(), "Atul", "Singh");
    Customer customer3 = new Customer(ObjectId.get().toString(), "Chotu", "Singh");

    //when
    when(customerService.getAllCustomers()).thenReturn(Flux.just(customer1, customer2, customer3));

    //then
    webTestClient.get()
            .uri("/api/v1/customers")
            .exchange().expectStatus().isOk()
            .expectBodyList(CustomerDTO.class)
            .hasSize(3);
  }

  @Test
  @Order(4)
  void updateCustomer() {
    //given
    final String customerId = ObjectId.get().toString();
    Customer current = new Customer(customerId, "Atul", "Singh");
    Customer updated = new Customer(customerId, "Rishu", "Singh");

    //when
    when(customerService.getCustomer(anyString())).thenReturn(Mono.just(current));
    when(customerService.updateCustomer(any(Customer.class))).thenReturn(Mono.just(updated));

    //then
    webTestClient.put()
            .uri("/api/v1/customers/" + customerId)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(new CustomerDTO("Rishu", "Singh")), CustomerDTO.class)
            .exchange().expectStatus().isOk()
            .expectBody(CustomerDTO.class)
            .value(CustomerDTO::getFirstName, equalTo("Rishu"))
            .value(CustomerDTO::getLastName, equalTo("Singh"))
            .value(CustomerDTO::getCustomerUrl, equalTo("/api/v1/customers/" + customerId));
  }

  @Test
  @Order(5)
  void patchCustomer() {
    //given
    final String customerId = ObjectId.get().toString();
    Customer current = new Customer(customerId, "Rishu", "Singh");
    Customer updated = new Customer(customerId, "Atul", "Singh");

    //when
    when(customerService.getCustomer(anyString())).thenReturn(Mono.just(current));
    when(customerService.patchCustomer(any(Customer.class))).thenReturn(Mono.just(updated));

    //then
    final CustomerDTO customerDTO = new CustomerDTO();
    customerDTO.setFirstName("Atul");

    webTestClient.patch().uri("/api/v1/customers/" + customerId)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(customerDTO), CustomerDTO.class)
            .exchange().expectStatus().isOk()
            .expectBody(CustomerDTO.class)
            .value(CustomerDTO::getFirstName, equalTo("Atul"))
            .value(CustomerDTO::getLastName, equalTo("Singh"))
            .value(CustomerDTO::getCustomerUrl, equalTo("/api/v1/customers/" + customerId));
  }

  @Test
  @Order(6)
  void deleteCustomer() {
    //given
    Customer customer = new Customer(ObjectId.get().toString(), "Atul", "Singh");

    //when
    when(customerService.deleteCustomer(anyString())).thenReturn(Mono.empty());

    //then
    webTestClient.delete()
            .uri("/api/v1/customers/" + customer.getId())
            .exchange().expectStatus().isOk();
  }
}