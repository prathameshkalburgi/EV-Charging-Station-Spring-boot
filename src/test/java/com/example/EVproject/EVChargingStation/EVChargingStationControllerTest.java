package com.example.EVproject.EVChargingStation;


import com.example.EVproject.EVCHragingStation.EVChargingStation;
import com.example.EVproject.EVCHragingStation.EVController;
import com.example.EVproject.EVCHragingStation.EVRepository;
import com.example.EVproject.EVCHragingStation.EVService;
import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@WebAppConfiguration
@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
public class EVChargingStationControllerTest {

    @MockBean
    private EVRepository evRepository;


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EVService evService;

    private InputStream is;
    @Spy
    @InjectMocks
    private EVController controller = new EVController();

    public EVChargingStationControllerTest() throws IOException {
    }

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        is = controller.getClass().getClassLoader().getResourceAsStream("As-Electric-Vehicles-Grow-In-Popularity-So-Does-Austins-Need-For-Charging-Stations.jpg");
    }


    MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "As-Electric-Vehicles-Grow-In-Popularity-So-Does-Austins-Need-For-Charging-Stations.jpg", "multipart/form-data", is);

    @Test
    public void getAllChargingStationTest() throws Exception {
        List<EVChargingStation> evChargingStationList = List.of(
                EVChargingStation.builder()
                        .stationImage(mockMultipartFile.getBytes())
                        .stationName("Test1")
                        .stationPricing(31.0)
                        .stationAddress("Testing Address2").build(),
                EVChargingStation.builder()
                        .stationImage(mockMultipartFile.getBytes())
                        .stationName("Test2")
                        .stationPricing(31.0)
                        .stationAddress("Testing Address2").build()
        );
        evRepository.saveAll(evChargingStationList);
        when(evRepository.findAll()).thenReturn(evChargingStationList);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
        Assert.assertEquals(200,result.getResponse().getStatus());

    }

    @Test
    public void getAllChargingStationWithLimitTest() throws Exception {
        List<EVChargingStation> evChargingStationList = List.of(
                EVChargingStation.builder()
                        .stationImage(mockMultipartFile.getBytes())
                        .stationName("Test1")
                        .stationPricing(31.0)
                        .stationAddress("Testing Address2").build()
        );
        when(evService.findAllChargingStation(Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(evChargingStationList);


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("limit","1"))
                .andExpect(MockMvcResultMatchers.status().is(200)).andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1)).andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        JSONArray jsonObject= new JSONArray(actualResponseBody);
        Assert.assertEquals(200,result.getResponse().getStatus());
        Assert.assertEquals(1,jsonObject.length());
    }




    @Test
    public void addNewEVChargingStationTest() throws Exception {
        EVChargingStation evChargingStation =EVChargingStation.builder()
                        .stationImage(mockMultipartFile.getBytes())
                        .stationName("Test3")
                        .stationPricing(32.0)
                        .stationAddress("Testing Address3").build();

        when(evRepository.save(Mockito.any())).thenReturn(evChargingStation);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/")
                        .file("image",mockMultipartFile.getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("stationName","Austin")
                        .param("stationPricing","50")
                        .param("stationAddress","Austin"))
                .andExpect(MockMvcResultMatchers.status().is(201)).andReturn();
        Assert.assertEquals(201, result.getResponse().getStatus());
    }

    @Test
    public void updateEVChargingStationTest() throws Exception {
        EVChargingStation evChargingStation =EVChargingStation.builder()
                .stationImage(mockMultipartFile.getBytes())
                .stationName("Test3")
                .stationPricing(32.0)
                .stationAddress("Testing Address3").build();

        EVChargingStation evChargingStationUpdate =EVChargingStation.builder()
                .stationImage(mockMultipartFile.getBytes())
                .stationName("AustinEdit")
                .stationPricing(50.0)
                .stationAddress("AustinEdit").build();

        when(evRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(evChargingStation));
        when(evRepository.save(Mockito.any())).thenReturn(evChargingStationUpdate);

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/{id}/edit",1);
        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });
        MvcResult result = mockMvc.perform(builder
                        .file("image",mockMultipartFile.getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("stationName","AustinEdit")
                        .param("stationPricing","50")
                        .param("stationAddress","AustinEdit"))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());
    }
}