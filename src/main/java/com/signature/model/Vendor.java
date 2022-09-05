package com.signature.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Vendor {

  @Id
  private String id;
  private String name;

  public Vendor(String name) {
    this.name = name;
  }
}