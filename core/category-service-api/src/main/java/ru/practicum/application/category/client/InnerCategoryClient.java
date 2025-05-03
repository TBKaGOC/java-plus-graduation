package ru.practicum.application.category.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.application.category.ui.InnerCategoryInterface;

@FeignClient(name = "category-service")
public interface InnerCategoryClient extends InnerCategoryInterface {
}
