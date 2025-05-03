package ru.practicum.application.category.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.application.category.ui.PublicCategoryInterface;

@FeignClient(name = "category-service")
public interface PublicCategoryClient extends PublicCategoryInterface {
}
