package com.signature.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

  private String firstName;

  private String lastName;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonProperty("customer_url")
  private String customerUrl;

  public CustomerDTO(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }
}