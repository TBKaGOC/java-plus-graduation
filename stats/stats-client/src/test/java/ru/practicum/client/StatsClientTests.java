package ru.practicum.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.StatsServer;
import ru.practicum.controller.StatsController;
import ru.practicum.dto.StatsRequestDto;
import ru.practicum.dto.StatsResponseDto;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = StatsServer.class)
public class StatsClientTests {
    private MockMvc mvc;
    @Autowired
    private StatsClient statsClient;
    @InjectMocks
    @Autowired
    private StatsController controller;
    @MockBean
    private StatsService service;
    private final ObjectMapper mapper = new ObjectMapper();
    private StatsRequestDto dto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
        dto = new StatsRequestDto("app", "http://server/", "000.000.000.000", LocalDateTime.now());
    }

    @Test
    public void save() {
        when(service.save(any()))
                .thenReturn(dto);

        StatsResponseDto responseDto = statsClient.postStats(dto);

        Assertions.assertEquals(dto.getApp(), responseDto.getApp());
    }
}
