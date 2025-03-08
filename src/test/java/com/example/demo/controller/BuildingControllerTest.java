package com.example.demo.controller;

import com.example.demo.dto.building.BuildingZoneAggregationDTO;
import com.example.demo.service.BuildingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BuildingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BuildingService buildingService;

    @InjectMocks
    private BuildingController buildingController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(buildingController).build();
    }

    @Test
    public void getBuildingZoneAggregation_Success_ReturnsData() throws Exception {
        Long buildingId = 1L;
        Integer functionId = 101;
        String functionName = "Sample Function";
        Integer roomCount = 5;
        List<BuildingZoneAggregationDTO.RoomBasicDTO> rooms = List.of(
                new BuildingZoneAggregationDTO.RoomBasicDTO(1L, "101", "Room 101", "1"),
                new BuildingZoneAggregationDTO.RoomBasicDTO(2L, "102", "Room 102", "1")
        );

        BuildingZoneAggregationDTO mockData = new BuildingZoneAggregationDTO(
                functionId,
                functionName,
                roomCount,
                rooms
        );

        when(buildingService.getBuildingZoneAggregation(buildingId, functionId)).thenReturn(mockData);

        mockMvc.perform(get("/api/building/{buildingId}/zones/{functionId}", buildingId, functionId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Building zone aggregation retrieved successfully"))
                .andExpect(jsonPath("$.data.functionId").value(functionId))
                .andExpect(jsonPath("$.data.functionName").value(functionName))
                .andExpect(jsonPath("$.data.roomCount").value(roomCount))
                .andExpect(jsonPath("$.data.rooms[0].roomId").value(1L))
                .andExpect(jsonPath("$.data.rooms[0].roomNumber").value("101"))
                .andExpect(jsonPath("$.data.rooms[0].roomName").value("Room 101"))
                .andExpect(jsonPath("$.data.rooms[0].floorNumber").value("1"))
                .andExpect(jsonPath("$.data.rooms[1].roomId").value(2L))
                .andExpect(jsonPath("$.data.rooms[1].roomNumber").value("102"))
                .andExpect(jsonPath("$.data.rooms[1].roomName").value("Room 102"))
                .andExpect(jsonPath("$.data.rooms[1].floorNumber").value("1"));
    }

    @Test
    public void getBuildingZoneAggregation_Exception_ReturnsError() throws Exception {
        Long buildingId = 1L;
        Integer functionId = 101;
        String errorMessage = "Service exception";

        when(buildingService.getBuildingZoneAggregation(buildingId, functionId)).thenThrow(new RuntimeException(errorMessage));

        mockMvc.perform(get("/api/building/{buildingId}/zones/{functionId}", buildingId, functionId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value("ERROR"))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
