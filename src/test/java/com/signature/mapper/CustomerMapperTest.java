package com.signature.mapper;

import com.signature.domain.CustomerDTO;
import com.signature.model.Customer;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerMapperTest {

  private final CustomerMapper customerMapper = CustomerMapper.INSTANCE;

  @Test
  void customerToCustomerDto() {
    //given
    Customer customer = new Customer(ObjectId.get().toString(), "Atul", "Singh");

    //when
    CustomerDTO customerDTO = customerMapper.customerToCustomerDto(customer);

    //then
    assertEquals("Atul", customerDTO.getFirstName());
    assertEquals("Singh", customerDTO.getLastName());
  }

  @Test
  void customerDtoToCustomer() {
    //given
    CustomerDTO customerDTO = new CustomerDTO("Atul", "Singh");

    //when
    Customer customer = customerMapper.customerDtoToCustomer(customerDTO);

    //then
    assertEquals("Atul", customer.getFirstName());
    assertEquals("Singh", customer.getLastName());
  }
}