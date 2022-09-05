package com.signature.mapper;

import com.signature.domain.VendorDTO;
import com.signature.model.Vendor;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VendorMapperTest {

  private final VendorMapper vendorMapper = VendorMapper.INSTANCE;

  @Test
  void vendorToVendorDto() {
    //given
    Vendor vendor = new Vendor(ObjectId.get().toString(), "Signature Technologies");

    //when
    VendorDTO vendorDTO = vendorMapper.vendorToVendorDto(vendor);

    //then
    assertEquals("Signature Technologies", vendorDTO.getName());
  }

  @Test
  void vendorDtoToVendor() {
    //given
    VendorDTO vendorDTO = new VendorDTO("Signature Technologies");

    //when
    Vendor vendor = vendorMapper.vendorDtoToVendor(vendorDTO);

    //then
    assertEquals("Signature Technologies", vendor.getName());
  }
}