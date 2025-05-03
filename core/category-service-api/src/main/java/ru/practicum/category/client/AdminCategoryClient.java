package ru.practicum.category.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.category.ui.AdminCategoryInterface;

@FeignClient(name = "category-service")
public interface AdminCategoryClient extends AdminCategoryInterface {
}
