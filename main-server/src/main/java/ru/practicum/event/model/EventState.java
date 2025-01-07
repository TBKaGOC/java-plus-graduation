package ru.practicum.event.model;

import java.util.List;

public enum EventState {
    PENDING,
    PUBLISHED,
    CANCELED;

    public static List<String> getAll() {
        return List.of(PENDING.name(), PUBLISHED.name(), CANCELED.name());
    }
}
