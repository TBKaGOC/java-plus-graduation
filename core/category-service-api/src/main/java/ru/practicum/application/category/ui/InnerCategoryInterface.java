package ru.practicum.application.category.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.application.api.dto.category.CategoryDto;

import java.util.List;
import java.util.Set;

@RequestMapping("/inner/category")
public interface InnerCategoryInterface {
    @GetMapping("/exist/{categoryId}")
    boolean existById(@PathVariable Long categoryId);

    @GetMapping("/ids")
    List<CategoryDto> getCategoriesByIds(@RequestBody Set<Long> ids);
}
