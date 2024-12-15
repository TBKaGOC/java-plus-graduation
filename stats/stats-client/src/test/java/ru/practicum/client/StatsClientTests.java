package ru.practicum.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.controller.StatsController;
import ru.practicum.dto.StatsRequestDto;
import ru.practicum.dto.StatsResponseDto;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatsClientTests {
    private final StatsClient statsClient;
    @InjectMocks
    private final StatsController statsController;
    @Mock
    private final StatsService service;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private StatsRequestDto dto;

    @Autowired
    public StatsClientTests(StatsClient statsClient, StatsController statsController, StatsService statsServer) {
        this.statsClient = statsClient;
        this.statsController = statsController;
        this.service = statsServer;
    }

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(statsClient)
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
