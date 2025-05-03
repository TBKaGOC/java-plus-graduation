package ru.practicum.category.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.dto.category.CategoryDto;
import ru.practicum.api.exception.ConflictException;
import ru.practicum.api.exception.NotFoundException;
import ru.practicum.category.service.CategoryService;
import ru.practicum.category.ui.AdminCategoryInterface;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AdminCategoryController implements AdminCategoryInterface {
    final CategoryService categoryService;

    @Override
    public CategoryDto addCategory(CategoryDto newCategory) throws ConflictException {
        return categoryService.addCategory(newCategory);
    }

    @Override
    public CategoryDto updateCategory(Long catId,
                                      CategoryDto categoryDto) throws ConflictException, NotFoundException {
        return categoryService.updateCategory(catId, categoryDto);
    }

    @Override
    public void deleteCategory(Long catId) throws ConflictException, NotFoundException {
        categoryService.deleteCategory(catId);
    }
}
