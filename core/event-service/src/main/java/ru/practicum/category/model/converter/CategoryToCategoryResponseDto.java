package ru.practicum.category.model.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.dto.event.category.CategoryResponseDto;
import ru.practicum.category.model.Category;

@Component
public class CategoryToCategoryResponseDto implements Converter<Category, CategoryResponseDto> {

    @Override
    public CategoryResponseDto convert(Category source) {
        return CategoryResponseDto.builder()
          .id(source.getId())
          .name(source.getName())
          .build();

    }
}
