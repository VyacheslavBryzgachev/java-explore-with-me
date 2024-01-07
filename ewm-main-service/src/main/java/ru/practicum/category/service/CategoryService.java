package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(CategoryDto categoryDto);

    CategoryDto update(CategoryDto categoryDto, Long id);

    void delete(Long id);

    List<CategoryDto> getAll(Integer from, Integer size);

    CategoryDto getById(Long catId);
}
