package com.signature.controller.v1;

import com.signature.domain.CategoryDTO;
import com.signature.mapper.CategoryMapper;
import com.signature.model.Category;
import com.signature.service.CategoryService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class CategoryControllerTest {

  private WebTestClient webTestClient;

  @MockBean
  public CategoryService categoryService;

  @BeforeEach
  void setUp() {
    webTestClient = WebTestClient.bindToController(new CategoryController(CategoryMapper.INSTANCE, categoryService)).build();
  }

  @Test
  @Order(1)
  void getAllCategories() {
    Category category2 = new Category(ObjectId.get().toString(), "Dried");
    Category category1 = new Category(ObjectId.get().toString(), "Fruits");

    when(categoryService.getAll()).thenReturn(Flux.just(category1, category2));

    webTestClient.get().uri("/api/v1/categories")
            .accept(MediaType.APPLICATION_JSON)
            .exchange().expectStatus().isOk()
            .expectBodyList(CategoryDTO.class)
            .hasSize(2);
  }

  @Test
  @Order(2)
  void getCategoryByName() {
    Category category = new Category(ObjectId.get().toString(), "Fruits");

    when(categoryService.getByName(anyString())).thenReturn(Mono.just(category));

    webTestClient.get().uri("/api/v1/categories/Fruits")
            .accept(MediaType.APPLICATION_JSON)
            .exchange().expectStatus().isOk()
            .expectBody(CategoryDTO.class);
  }
}