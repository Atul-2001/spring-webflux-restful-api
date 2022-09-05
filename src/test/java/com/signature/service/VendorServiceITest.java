package com.signature.service;

import com.signature.bootstrap.Bootstrap;
import com.signature.model.Vendor;
import com.signature.repository.CategoryRepository;
import com.signature.repository.CustomerRepository;
import com.signature.repository.VendorRepository;
import com.signature.service.impl.VendorServiceImpl;
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
class VendorServiceITest {

  private VendorService vendorService;

  @Autowired
  public VendorRepository vendorRepository;

  @Autowired
  public CustomerRepository customerRepository;

  @Autowired
  public CategoryRepository categoryRepository;

  private List<String> vendorIds;

  @BeforeEach
  void setUp() throws Exception {
    log.info("Started loading initial data");

    new Bootstrap(categoryRepository, customerRepository, vendorRepository).run();

    log.info("Finished loading initial data");

    vendorService = new VendorServiceImpl(vendorRepository);

    vendorIds = vendorService.getAllVendors().map(Vendor::getId).collectList().block();
  }

  @Test
  @Order(1)
  @DirtiesContext
  void addVendor() {
    Vendor vendor = new Vendor("Pranjal and Atul Singh Foundation");

    Vendor savedVendor = vendorService.addVendor(vendor).block();

    assertNotNull(savedVendor, "Failed to save vendor!");

    assertEquals("Pranjal and Atul Singh Foundation", savedVendor.getName());

    assertEquals(vendorIds.size() + 1, vendorRepository.count().block().intValue());
  }

  @Test
  @Order(2)
  @DirtiesContext
  void updateVendor() throws Exception {
    final String vendorId = vendorIds.get(1);

    Vendor existingVendor = vendorService.getVendor(vendorId).block();

    assertNotNull(existingVendor, "Vendor not found!");

    Vendor vendor = new Vendor(vendorId, "Signature Technologies");

    Vendor updated = vendorService.updateVendor(vendor).block();

    assertNotNull(updated, "Failed to update vendor!");

    assertEquals("Signature Technologies", updated.getName());
    assertThat(updated.getName(), not(equalTo(existingVendor.getName())));
  }

  @Test
  @Order(3)
  @DirtiesContext
  void patchVendor() throws Exception {
    final String vendorId = vendorIds.get(2);

    final Vendor existingVendor = vendorService.getVendor(vendorId).block();

    assertNotNull(existingVendor, "Vendor not found!");

    final String existingName = existingVendor.getName();

    Vendor vendor = new Vendor(vendorId, "Vanilla Technologies");

    Vendor patched = vendorService.patchVendor(vendor).block();

    assertNotNull(patched, "Failed to update vendor!");

    assertThat(patched.getName(), not(equalTo(existingName)));
    assertEquals("Vanilla Technologies", patched.getName());
  }

  @Test
  @Order(4)
  @DirtiesContext
  void getVendor() throws Exception {
    Vendor vendor = vendorService.getVendor(vendorIds.get(0)).block();

    assertNotNull(vendor, "Failed to get vendor!");
  }

  @Test
  @Order(5)
  @DirtiesContext
  void getAllVendors() {
    List<Vendor> vendors = vendorService.getAllVendors().collectList().block();

    assertNotNull(vendors, "Failed to get vendors!");

    assertEquals(6, vendors.size());
  }

  @Test
  @Order(6)
  @DirtiesContext
  void deleteVendor() {
    Long count = vendorRepository.count().block();

    vendorService.deleteVendor(vendorIds.get(0)).block();

    assertEquals(count - 1, vendorRepository.count().block());
  }
}