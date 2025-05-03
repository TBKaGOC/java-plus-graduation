package ru.practicum.application.category.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.application.category.ui.AdminCategoryInterface;

@FeignClient(name = "category-service")
public interface AdminCategoryClient extends AdminCategoryInterface {
}
