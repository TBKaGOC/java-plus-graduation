package ru.practicum.category.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.api.dto.category.CategoryDto;
import ru.practicum.category.service.CategoryService;
import ru.practicum.category.ui.InnerCategoryInterface;

import java.util.List;
import java.util.Set;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class InnerCategoryController implements InnerCategoryInterface {

    final CategoryService categoryService;

    @Override
    public boolean existById(Long categoryId) {
        return categoryService.existById(categoryId);
    }

    @Override
    public List<CategoryDto> getCategoriesByIds(Set<Long> ids) {
        return categoryService.getCategoriesByIds(ids);
    }
}
