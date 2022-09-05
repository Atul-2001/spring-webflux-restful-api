package com.signature.mapper;

import com.signature.domain.CategoryDTO;
import com.signature.model.Category;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryMapperTest {

  private final CategoryMapper categoryMapper = CategoryMapper.INSTANCE;

  @Test
  void categoryToCategoryDTO() {
    //given
    Category category = new Category(ObjectId.get().toString(), "Fruits");

    //when
    CategoryDTO categoryDTO = categoryMapper.categoryToCategoryDTO(category);

    //then
    assertEquals("Fruits", categoryDTO.getName());
  }

  @Test
  void categoryDtoToCategory() {
    //given
    CategoryDTO categoryDTO = new CategoryDTO("Fruits");

    //when
    Category category = categoryMapper.categoryDtoToCategory(categoryDTO);

    //then
    assertEquals("Fruits", category.getName());
  }
}