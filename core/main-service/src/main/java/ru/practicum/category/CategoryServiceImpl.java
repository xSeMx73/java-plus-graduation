package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryCreateDto;
import ru.practicum.category.dto.CategoryResponseDto;
import ru.practicum.category.dto.CategoryUpdateDto;
import ru.practicum.category.model.Category;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ParameterConflictException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    @Qualifier("conversionService")
    private final ConversionService converter;

    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    @Override
    public CategoryResponseDto create(CategoryCreateDto createDto) {
        if (categoryRepository.existsByName(createDto.name())) {
            throw new ParameterConflictException("Category", "Категория уже существует");
        }
        Category category = converter.convert(createDto, Category.class);
        Category categoryCreated = categoryRepository.save(Objects.requireNonNull(category));
        return converter.convert(categoryCreated, CategoryResponseDto.class);
    }

    @Override
    public CategoryResponseDto update(CategoryUpdateDto updateDto) {

        Category category = getCategoryIfExist(updateDto.id());

        boolean isNameMatch = category.getName().equals(updateDto.name());
        if (!isNameMatch) {
            if (categoryRepository.existsByName(updateDto.name())) {
                throw new ParameterConflictException("Category", "Категория уже существует");
            }
            category.setName(updateDto.name());
        }

        return converter.convert(category, CategoryResponseDto.class);
    }

    @Override
    public void remove(long categoryId) {
        getCategoryIfExist(categoryId);

        if (!eventRepository.findByCategoryId(categoryId).isEmpty()) {
            throw new ParameterConflictException("Category", "The category is not empty");
        }

        categoryRepository.deleteById(categoryId);
    }

    @Override
    public CategoryResponseDto getById(long categoryId) {
        Category category = getCategoryIfExist(categoryId);
        return converter.convert(category, CategoryResponseDto.class);
    }

    @Override
    public List<CategoryResponseDto> getFromSize(int from, int size) {
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");

        Pageable pageable = PageRequest.of(from, size, sortById);

        return categoryRepository.findAll(pageable).stream()
                .map(c -> converter.convert(c, CategoryResponseDto.class))
                .toList();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    protected Category getCategoryIfExist(long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));
    }
}
