package com.signature.service;

import com.signature.model.Vendor;
import com.signature.repository.VendorRepository;
import com.signature.service.impl.VendorServiceImpl;
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
class VendorServiceTest {

  private VendorService vendorService;

  @Mock
  public VendorRepository vendorRepository;

  @BeforeEach
  void setUp() {
    vendorService = new VendorServiceImpl(vendorRepository);
  }

  @Test
  @Order(1)
  void addVendor() {
    //given
    Vendor vendor = new Vendor(ObjectId.get().toString(), "Signature Technologies");

    //when
    when(vendorRepository.save(any(Vendor.class))).thenReturn(Mono.just(vendor));

    //then
    Vendor savedVendor = vendorService.addVendor(vendor).block();

    verify(vendorRepository, times(1)).save(any(Vendor.class));

    assertNotNull(savedVendor, "Failed to save vendor!");

    assertEquals("Signature Technologies", savedVendor.getName());
  }

  @Test
  @Order(2)
  void updateVendor() throws Exception {
    //given
    final String vendorId = ObjectId.get().toString();
    Vendor vendor = new Vendor(vendorId, "Signature Technologies Ltd.");

    //when
    when(vendorRepository.save(any(Vendor.class))).thenReturn(Mono.just(vendor));

    //then
    Vendor savedVendor = vendorService.updateVendor(vendor).block();

    verify(vendorRepository, times(1)).save(any(Vendor.class));

    assertNotNull(savedVendor, "Failed to update vendor!");

    assertEquals(vendorId, savedVendor.getId());
    assertEquals("Signature Technologies Ltd.", savedVendor.getName());
  }

  @Test
  @Order(3)
  void patchVendor() throws Exception {
    //given
    final String vendorId = ObjectId.get().toString();
    Vendor vendor = new Vendor(vendorId, "Signature Technologies");
    Vendor updated = new Vendor(vendorId, "Signature Technologies Ltd.");

    //when
    when(vendorRepository.findById(anyString())).thenReturn(Mono.just(vendor));
    when(vendorRepository.save(any(Vendor.class))).thenReturn(Mono.just(updated));

    //then
    Vendor savedVendor = vendorService.patchVendor(updated).block();

    verify(vendorRepository, times(1)).save(any(Vendor.class));

    assertNotNull(savedVendor, "Failed to patch vendor!");

    assertEquals(vendorId, savedVendor.getId());
    assertEquals("Signature Technologies Ltd.", savedVendor.getName());
  }

  @Test
  @Order(4)
  void getVendor() throws Exception {
    //given
    final String vendorId = ObjectId.get().toString();
    Vendor vendor = new Vendor(vendorId, "Signature Technologies");

    //when
    when(vendorRepository.findById(anyString())).thenReturn(Mono.just(vendor));

    //then
    Vendor vendor1 = vendorService.getVendor(vendorId).block();

    verify(vendorRepository, times(1)).findById(vendorId);

    assertNotNull(vendor1, "Failed to get vendor!");

    assertEquals(vendorId, vendor1.getId());
    assertEquals("Signature Technologies", vendor1.getName());
  }

  @Test
  @Order(5)
  void getAllVendors() {
    //given
    Flux<Vendor> vendors = Flux.just(new Vendor(), new Vendor(), new Vendor());

    //when
    when(vendorRepository.findAll()).thenReturn(vendors);

    //then
    List<Vendor> allVendors = vendorService.getAllVendors().collectList().block();

    verify(vendorRepository, times(1)).findAll();

    assertEquals(3, allVendors.size());
  }

  @Test
  @Order(6)
  void deleteVendor() {
    //when
    when(vendorRepository.deleteById(anyString())).thenReturn(Mono.empty());

    vendorService.deleteVendor(anyString()).block();

    verify(vendorRepository, times(1)).deleteById(anyString());
  }
}