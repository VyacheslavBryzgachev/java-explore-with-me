package ru.practicum.category.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DbCategoryStorage {
    private final CategoryRepository categoryRepository;

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public Category update(Category category) {
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    public Optional<Category> getById(Long id) {
        return categoryRepository.findById(id);
    }

    public Page<Category> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Category findByName(String name, Long catId) {
        return categoryRepository.findByName(name, catId);
    }

    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }
}
