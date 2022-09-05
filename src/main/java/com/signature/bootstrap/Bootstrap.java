package com.signature.bootstrap;

import com.signature.model.Category;
import com.signature.model.Customer;
import com.signature.model.Vendor;
import com.signature.repository.CategoryRepository;
import com.signature.repository.CustomerRepository;
import com.signature.repository.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

  private final CategoryRepository categoryRepository;
  private final CustomerRepository customerRepository;
  private final VendorRepository vendorRepository;

  public Bootstrap(final CategoryRepository categoryRepository,
                   final CustomerRepository customerRepository,
                   final VendorRepository vendorRepository) {
    this.categoryRepository = categoryRepository;
    this.customerRepository = customerRepository;
    this.vendorRepository = vendorRepository;
  }

  private void loadCategories() throws Exception {
    final Long count = categoryRepository.count().block();

    if (Objects.equals(count, 0L)) {
      log.debug("### Loading Categories on Bootstrap ###");

      categoryRepository.save(new Category("Fruits")).block();
      categoryRepository.save(new Category("Dried")).block();
      categoryRepository.save(new Category("Fresh")).block();
      categoryRepository.save(new Category("Exotic")).block();
      categoryRepository.save(new Category("Nuts")).block();

      log.info("No. of categories added : {}", categoryRepository.count().block());
    } else {
      log.info("No. of categories already present : {}", count);
    }
  }

  private void loadCustomers() throws Exception {
    final Long count = customerRepository.count().block();

    if (Objects.equals(count, 0L)) {
      log.debug("### Loading Customers on Bootstrap ###");

      customerRepository.save(new Customer("Rishu", "Singh")).block();
      customerRepository.save(new Customer("Atul", "Singh")).block();
      customerRepository.save(new Customer("Chotu", "Singh")).block();
      customerRepository.save(new Customer("Abhishek", "Singh")).block();
      customerRepository.save(new Customer("Shivang", "Verma")).block();
      customerRepository.save(new Customer("Vivek", "Pandey")).block();
      customerRepository.save(new Customer("Saumil", "Thripathi")).block();

      log.info("No. of customers added : {}", customerRepository.count().block());
    } else {
      log.info("No. of customers already present : {}", count);
    }
  }

  private void loadVendors() throws Exception {
    final Long count = vendorRepository.count().block();

    if (Objects.equals(count, 0L)) {
      log.debug("### Loading Vendors on Bootstrap ###");

      vendorRepository.save(new Vendor("Signature Technologies Ltd.")).block();
      vendorRepository.save(new Vendor("Vandela Technologies Ltd.")).block();
      vendorRepository.save(new Vendor("Apple Technologies Ltd.")).block();
      vendorRepository.save(new Vendor("Microsoft Technologies Ltd.")).block();
      vendorRepository.save(new Vendor("Google Technologies Ltd.")).block();
      vendorRepository.save(new Vendor("Facebook Technologies Ltd.")).block();

      log.info("No. of vendors added : {}", vendorRepository.count().block());
    } else {
      log.info("No. of vendors already present : {}", count);
    }
  }

  @Override
  public void run(String... args) throws Exception {
    loadCategories();
    loadCustomers();
    loadVendors();
  }
}