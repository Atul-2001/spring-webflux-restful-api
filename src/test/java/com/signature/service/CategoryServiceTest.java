package com.signature.service;

import com.signature.model.Category;
import com.signature.repository.CategoryRepository;
import com.signature.service.impl.CategoryServiceImpl;
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

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class CategoryServiceTest {

  private CategoryService categoryService;

  @Mock
  public CategoryRepository categoryRepository;

  @BeforeEach
  void setUp() {
    categoryService = new CategoryServiceImpl(categoryRepository);
  }

  @Test
  @Order(1)
  void getByName() {
    //given
    Category category = new Category(ObjectId.get().toString(), "Fruits");

    when(categoryRepository.findByName(anyString())).thenReturn(Mono.just(category));

    //when
    Category category1 = categoryService.getByName("Fruits").block();

    //then
    verify(categoryRepository, times(1)).findByName("Fruits");
    assertNotNull(category1, "Category should not be null!");
    assertEquals("Fruits", category1.getName());
  }

  @Test
  @Order(2)
  void getById() {
    //given
    String categoryId = ObjectId.get().toString();
    Category category = new Category(categoryId, "Fruits");

    when(categoryRepository.findById(anyString())).thenReturn(Mono.just(category));

    //when
    Category category1 = categoryService.getById(categoryId).block();

    //then
    verify(categoryRepository, times(1)).findById(categoryId);
    assertNotNull(category1, "Category should not be null!");
    assertEquals(categoryId, category1.getId());
    assertEquals("Fruits", category1.getName());
  }

  @Test
  @Order(3)
  void getAll() {
    //given
    Flux<Category> categories = Flux.just(new Category(), new Category(), new Category());

    when(categoryRepository.findAll()).thenReturn(categories);

    //when
    Flux<Category> categoryList = categoryService.getAll();

    //then
    verify(categoryRepository, times(1)).findAll();
    assertEquals(3, Objects.requireNonNull(categoryList.collectList().block()).size());
  }
}