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
public class CategoryDTO {

  private String name;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonProperty("category_url")
  private String categoryUrl;

  public CategoryDTO(String name) {
    this.name = name;
  }
}