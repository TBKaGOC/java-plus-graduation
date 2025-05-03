package ru.practicum.category.ui;

import org.springframework.web.bind.annotation.*;
import ru.practicum.api.dto.category.CategoryDto;
import ru.practicum.api.exception.NotFoundException;

import java.util.List;
import java.util.Set;

@RequestMapping("/categories")
public interface PublicCategoryInterface {
    @GetMapping
    List<CategoryDto> getAllCategories(@RequestParam(required = false, defaultValue = "0") Integer from,
                                       @RequestParam(required = false, defaultValue = "10") Integer size);

    @GetMapping("/{catId}")
    CategoryDto getCategoryById(@PathVariable Long catId) throws NotFoundException;
}
