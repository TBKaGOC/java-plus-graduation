package ru.practicum.application.category.ui;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.application.api.dto.category.CategoryDto;
import ru.practicum.application.api.exception.ConflictException;
import ru.practicum.application.api.exception.NotFoundException;

@RequestMapping("/admin/categories")
public interface AdminCategoryInterface {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CategoryDto addCategory(@Valid @RequestBody CategoryDto newCategory) throws ConflictException;

    @PatchMapping("/{catId}")
    CategoryDto updateCategory(@PathVariable Long catId,
                               @Valid @RequestBody CategoryDto categoryDto) throws ConflictException, NotFoundException;

    @DeleteMapping("/{catId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    void deleteCategory(@PathVariable Long catId) throws ConflictException, NotFoundException;
}
