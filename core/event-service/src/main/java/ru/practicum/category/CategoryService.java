package ru.practicum.category;

import ru.practicum.dto.event.category.CategoryCreateDto;
import ru.practicum.dto.event.category.CategoryResponseDto;
import ru.practicum.dto.event.category.CategoryUpdateDto;

import java.util.List;

public interface CategoryService {

    CategoryResponseDto create(CategoryCreateDto createDto);

    CategoryResponseDto update(CategoryUpdateDto updateDto);

    void remove(long categoryId);

    CategoryResponseDto getById(long categoryId);

    List<CategoryResponseDto> getFromSize(int from, int size);
}
