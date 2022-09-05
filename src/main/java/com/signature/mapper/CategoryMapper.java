package com.signature.mapper;

import com.signature.domain.CategoryDTO;
import com.signature.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {

  CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

  @Mapping(target = "categoryUrl", ignore = true)
  CategoryDTO categoryToCategoryDTO(Category category);

  @Mapping(target = "id", ignore = true)
  Category categoryDtoToCategory(CategoryDTO categoryDTO);
}