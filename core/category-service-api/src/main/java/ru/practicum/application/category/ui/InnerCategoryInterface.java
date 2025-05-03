package ru.practicum.application.category.ui;

import org.springframework.web.bind.annotation.*;
import ru.practicum.application.api.dto.category.CategoryDto;

import java.util.List;
import java.util.Set;

@RequestMapping("/inner/category")
@RestController
public interface InnerCategoryInterface {
    @GetMapping("/exist/{categoryId}")
    boolean existById(@PathVariable Long categoryId);

    @GetMapping("/ids")
    List<CategoryDto> getCategoriesByIds(@RequestBody Set<Long> ids);
}
