package com.signature.service;

import com.signature.model.Category;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CategoryService {

  Mono<Category> getByName(String name);

  Mono<Category> getById(String id);

  Flux<Category> getAll();
}