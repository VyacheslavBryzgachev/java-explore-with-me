package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.dao.DbCategoryStorage;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.dao.DbEventStorage;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.WrongRequestException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final DbCategoryStorage dbCategoryStorage;
    private final DbEventStorage dbEventStorage;
    private final CategoryMapper categoryMapper = new CategoryMapper();

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        if (dbCategoryStorage.findByName(categoryDto.getName()) != null) {
            throw new WrongRequestException("Категория с таким именем уже существует");
        }
        Category category = categoryMapper.toCategory(categoryDto);
        return categoryMapper.toCategoryDto(dbCategoryStorage.create(category));
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, Long id) {
        CategoryDto catDto = dbCategoryStorage.getById(id).stream()
                .map(categoryMapper::toCategoryDto)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));
        if (dbCategoryStorage.findByName(categoryDto.getName(), id) != null) {
            throw new WrongRequestException("Категория с таким именем уже существует");
        }
        catDto.setId(categoryDto.getId());
        catDto.setName(categoryDto.getName());
        Category category = categoryMapper.toCategory(catDto);
        return categoryMapper.toCategoryDto(dbCategoryStorage.update(category));
    }

    @Override
    public void delete(Long id) {
        if (dbEventStorage.getEventByCatId(id) != null) {
            throw new WrongRequestException("Нельзя удалить категорию к которой привязаны события");
        }
        dbCategoryStorage.delete(id);
    }

    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        Pageable pageWithFromAndSize = PageRequest.of(from, size);
        return dbCategoryStorage.getAll(pageWithFromAndSize).stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Long catId) {
        return dbCategoryStorage.getById(catId).stream()
                .map(categoryMapper::toCategoryDto)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));
    }
}
