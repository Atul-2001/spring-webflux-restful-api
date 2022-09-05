package com.signature.service;

import com.signature.model.Vendor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface VendorService {

  Mono<Vendor> addVendor(Vendor vendor);

  Mono<Vendor> updateVendor(Vendor vendor);

  Mono<Vendor> patchVendor(Vendor vendor);

  Mono<Vendor> getVendor(String vendorId);

  Flux<Vendor> getAllVendors();

  Mono<Void> deleteVendor(String vendorId);
}