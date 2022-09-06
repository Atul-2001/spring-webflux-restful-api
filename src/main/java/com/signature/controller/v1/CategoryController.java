package com.signature.controller.v1;

import com.signature.domain.CategoryDTO;
import com.signature.mapper.CategoryMapper;
import com.signature.model.Category;
import com.signature.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Category Controller", description = "Category API")
public class CategoryController {

  private final CategoryMapper categoryMapper;
  private final CategoryService categoryService;

  public CategoryController(CategoryMapper categoryMapper,
                            CategoryService categoryService) {
    this.categoryMapper = categoryMapper;
    this.categoryService = categoryService;
  }

  private CategoryDTO categoryToCategoryDto(final Category category) {
    CategoryDTO categoryDTO = categoryMapper.categoryToCategoryDTO(category);
    categoryDTO.setCategoryUrl("/api/v1/categories/" + category.getId());
    return categoryDTO;
  }

  @GetMapping
  @Operation(summary = "Get all categories")
  @ApiResponse(responseCode = "200", description = "Found all categories",
          content = {@Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = CategoryDTO.class)))})
  public ResponseEntity<Flux<CategoryDTO>> getAllCategories() {
    return ResponseEntity.ok(categoryService.getAll().map(this::categoryToCategoryDto));
  }


  private Mono<CategoryDTO> getCategoryById(final String id) {
    return categoryService.getById(id).map(this::categoryToCategoryDto);
  }

  public Mono<CategoryDTO> getCategoryByName(final String name) {
    return categoryService.getByName(name).map(this::categoryToCategoryDto);
  }

  @GetMapping("/{identifier}")
  @Operation(summary = "Get category by id or name")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Found category",
                  content = {@Content(mediaType = "application/json",
                          schema = @Schema(implementation = CategoryDTO.class))}),
          @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                  content = @Content),
          @ApiResponse(responseCode = "404", description = "Category not found",
                  content = @Content)})
  public ResponseEntity<Mono<CategoryDTO>> getCategoryByIdentifier(@PathVariable String identifier) {
    if (ObjectId.isValid(identifier)) {
      return ResponseEntity.ok(getCategoryById(identifier));
    } else {
      return ResponseEntity.ok(getCategoryByName(identifier));
    }
  }
}