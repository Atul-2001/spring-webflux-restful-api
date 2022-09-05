package com.signature.service.impl;

import com.signature.exception.ResourceNotFoundException;
import com.signature.model.Category;
import com.signature.repository.CategoryRepository;
import com.signature.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryServiceImpl(final CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public Mono<Category> getByName(String name) {
    return categoryRepository.findByName(name)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Category with name " + name + " not found")));
  }

  @Override
  public Mono<Category> getById(String id) {
    return categoryRepository.findById(id)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Category with id " + id + " not found")));
  }

  @Override
  public Flux<Category> getAll() {
    return categoryRepository.findAll();
  }
}