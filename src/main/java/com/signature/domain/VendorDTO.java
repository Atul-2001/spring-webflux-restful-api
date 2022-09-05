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
public class VendorDTO {

  private String name;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonProperty("vendor_url")
  private String vendorUrl;

  public VendorDTO(String name) {
    this.name = name;
  }
}