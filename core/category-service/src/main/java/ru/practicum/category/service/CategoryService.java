package ru.practicum.category.service;

import ru.practicum.api.dto.category.CategoryDto;
import ru.practicum.api.exception.ConflictException;
import ru.practicum.api.exception.NotFoundException;

import java.util.List;
import java.util.Set;

public interface CategoryService {
    CategoryDto addCategory(CategoryDto categoryDto) throws ConflictException;

    CategoryDto updateCategory(Long catId, CategoryDto categoryDto) throws NotFoundException, ConflictException;

    CategoryDto getCategoryById(Long catId) throws NotFoundException;

    List<CategoryDto> getAllCategories(Integer from, Integer size);

    void deleteCategory(Long catId) throws ConflictException, NotFoundException;

    List<CategoryDto> getCategoriesByIds(Set<Long> ids);

    boolean existById(Long categoryId);

}
