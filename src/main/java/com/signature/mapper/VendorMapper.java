package com.signature.mapper;

import com.signature.domain.VendorDTO;
import com.signature.model.Vendor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VendorMapper {

  VendorMapper INSTANCE = Mappers.getMapper(VendorMapper.class);

  @Mapping(target = "vendorUrl", ignore = true)
  VendorDTO vendorToVendorDto(Vendor vendor);

  @Mapping(target = "id", ignore = true)
  Vendor vendorDtoToVendor(VendorDTO vendorDTO);
}