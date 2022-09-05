package com.signature.mapper;

import com.signature.domain.CustomerDTO;
import com.signature.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {

  CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

  @Mapping(target = "customerUrl", ignore = true)
  CustomerDTO customerToCustomerDto(Customer customer);

  @Mapping(target = "id", ignore = true)
  Customer customerDtoToCustomer(CustomerDTO customerDTO);
}