package com.signature.controller.v1;

import com.signature.domain.VendorDTO;
import com.signature.mapper.VendorMapper;
import com.signature.model.Vendor;
import com.signature.service.VendorService;
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
class VendorControllerTest {

  private WebTestClient webTestClient;

  @MockBean
  public VendorService vendorService;

  @BeforeEach
  void setUp() {
    webTestClient = WebTestClient.bindToController(new VendorController(VendorMapper.INSTANCE, vendorService)).build();
  }

  @Test
  @Order(1)
  void createVendor() {
    //given
    Vendor vendor = new Vendor(ObjectId.get().toString(), "Signature Technologies");

    //when
    when(vendorService.addVendor(any(Vendor.class))).thenReturn(Mono.just(vendor));

    //then
    webTestClient.post().uri("/api/v1/vendors")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(new VendorDTO("Signature Technologies")), VendorDTO.class)
            .exchange().expectStatus().isCreated()
            .expectBody(VendorDTO.class)
            .value(VendorDTO::getName, equalTo("Signature Technologies"))
            .value(VendorDTO::getVendorUrl, equalTo("/api/v1/vendors/" + vendor.getId()));
  }

  @Test
  @Order(2)
  void getVendor() {
    //given
    Vendor vendor = new Vendor(ObjectId.get().toString(), "Signature Technologies");

    //when
    when(vendorService.getVendor(anyString())).thenReturn(Mono.just(vendor));

    //then
    webTestClient.get().uri("/api/v1/vendors/" + vendor.getId())
            .exchange().expectStatus().isOk()
            .expectBody(VendorDTO.class)
            .value(VendorDTO::getName, equalTo("Signature Technologies"))
            .value(VendorDTO::getVendorUrl, equalTo("/api/v1/vendors/" + vendor.getId()));
  }

  @Test
  @Order(3)
  void getAllVendors() {
    //given
    Vendor vendor1 = new Vendor(ObjectId.get().toString(), "Signature Technologies");
    Vendor vendor2 = new Vendor(ObjectId.get().toString(), "Microsoft Technologies");
    Vendor vendor3 = new Vendor(ObjectId.get().toString(), "Amazon Technologies");

    //when
    when(vendorService.getAllVendors()).thenReturn(Flux.just(vendor1, vendor2, vendor3));

    //then
    webTestClient.get().uri("/api/v1/vendors")
            .exchange().expectStatus().isOk()
            .expectBodyList(VendorDTO.class)
            .hasSize(3);
  }

  @Test
  @Order(4)
  void updateVendor() {
    //given
    final String vendorId = ObjectId.get().toString();
    Vendor current = new Vendor(vendorId, "Microsoft Technologies");
    Vendor updated = new Vendor(vendorId, "Signature Technologies");

    //when
    when(vendorService.getVendor(anyString())).thenReturn(Mono.just(current));
    when(vendorService.updateVendor(any(Vendor.class))).thenReturn(Mono.just(updated));

    //then
    webTestClient.put().uri("/api/v1/vendors/" + vendorId)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(new VendorDTO("Signature Technologies")), VendorDTO.class)
            .exchange().expectStatus().isOk()
            .expectBody(VendorDTO.class)
            .value(VendorDTO::getName, equalTo("Signature Technologies"))
            .value(VendorDTO::getVendorUrl, equalTo("/api/v1/vendors/" + vendorId));
  }

  @Test
  @Order(5)
  void patchVendor() {
    //given
    final String vendorId = ObjectId.get().toString();
    Vendor current = new Vendor(vendorId, "Signature Technologies");
    Vendor updated = new Vendor(vendorId, "Microsoft Technologies");

    //when
    when(vendorService.getVendor(anyString())).thenReturn(Mono.just(current));
    when(vendorService.patchVendor(any(Vendor.class))).thenReturn(Mono.just(updated));

    //then
    VendorDTO vendorDTO = new VendorDTO();
    vendorDTO.setName("Microsoft Technologies");

    webTestClient.patch().uri("/api/v1/vendors/" + vendorId)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(vendorDTO), VendorDTO.class)
            .exchange().expectStatus().isOk()
            .expectBody(VendorDTO.class)
            .value(VendorDTO::getName, equalTo("Microsoft Technologies"))
            .value(VendorDTO::getVendorUrl, equalTo("/api/v1/vendors/" + vendorId));
  }

  @Test
  @Order(6)
  void deleteVendor() {
    //given
    Vendor vendor = new Vendor(ObjectId.get().toString(), "Signature Technologies");

    //when
    when(vendorService.deleteVendor(anyString())).thenReturn(Mono.empty());

    //then
    webTestClient.delete().uri("/api/v1/vendors/" + vendor.getId())
            .exchange().expectStatus().isOk();
  }
}