package ru.practicum.category.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.category.ui.InnerCategoryInterface;

@FeignClient(name = "category-service")
public interface InnerCategoryClient extends InnerCategoryInterface {
}
