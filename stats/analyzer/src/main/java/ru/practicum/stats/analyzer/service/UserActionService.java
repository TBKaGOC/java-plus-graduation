package ru.practicum.stats.analyzer.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.stats.analyzer.mapper.UserActionMapper;
import ru.practicum.stats.analyzer.repository.UserActionRepository;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class UserActionService {
    final UserActionRepository repository;

    public void save(UserActionAvro avro) {
        log.info("Сохранинеия дайстви {} пользователя {} для события {}", avro.getActionType(),
                avro.getUserId(), avro.getEventId());
        repository.deleteById(UserActionMapper.mapAvroToKey(avro));
        repository.save(UserActionMapper.mapAvroToEntity(avro));
    }
}
