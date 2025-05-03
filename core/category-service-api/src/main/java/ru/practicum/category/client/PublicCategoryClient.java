package ru.practicum.category.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.category.ui.PublicCategoryInterface;

@FeignClient(name = "category-service")
public interface PublicCategoryClient extends PublicCategoryInterface {
}
