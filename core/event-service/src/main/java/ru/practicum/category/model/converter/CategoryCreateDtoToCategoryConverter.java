package ru.practicum.category.model.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.category.model.Category;
import ru.practicum.dto.event.category.CategoryCreateDto;

@Component
public class CategoryCreateDtoToCategoryConverter implements Converter<CategoryCreateDto, Category> {

    @Override
    public Category convert(CategoryCreateDto source) {
        Category category = new Category();
        category.setId(source.id());
        category.setName(source.name());
        return category;
    }
}
