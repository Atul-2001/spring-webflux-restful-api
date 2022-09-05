package com.signature.service.impl;

import com.signature.exception.ResourceNotFoundException;
import com.signature.model.Vendor;
import com.signature.repository.VendorRepository;
import com.signature.service.VendorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class VendorServiceImpl implements VendorService {

  private final VendorRepository vendorRepository;

  public VendorServiceImpl(VendorRepository vendorRepository) {
    this.vendorRepository = vendorRepository;
  }

  @Override
  public Mono<Vendor> addVendor(Vendor vendor) {
    return vendorRepository.save(vendor);
  }

  @Override
  public Mono<Vendor> updateVendor(Vendor vendor) {
    return vendorRepository.save(vendor);
  }

  @Override
  public Mono<Vendor> patchVendor(Vendor vendor) {
    return getVendor(vendor.getId())
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Vendor with id " + vendor.getId() + " not found")))
            .flatMap(existingVendor -> {
              if (vendor.getName() != null)
                existingVendor.setName(vendor.getName());
              return vendorRepository.save(existingVendor);
            });
  }

  @Override
  public Mono<Vendor> getVendor(String vendorId) {
    return vendorRepository.findById(vendorId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Vendor with id " + vendorId + " not found")));
  }

  @Override
  public Flux<Vendor> getAllVendors() {
    return vendorRepository.findAll();
  }

  @Override
  public Mono<Void> deleteVendor(String vendorId) {
    return vendorRepository.deleteById(vendorId);
  }
}