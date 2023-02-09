package com.example.EVproject.EVCHragingStation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/")
public class EVController {

    @Autowired
    EVService evService;



    @GetMapping("/")
    public ResponseEntity<List<EVChargingStation>> getAllChargingStation (@RequestParam(name = "limit",defaultValue = "20", required = false) int limit,
                                                                          @RequestParam(name="sort",defaultValue = "asc", required = false) String sortOrder,
                                                                          @RequestParam(name="param",defaultValue = "id", required = false) String param) {
        List<EVChargingStation> customer = evService.findAllChargingStation(limit,sortOrder,param);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<EVChargingStation> getStationById (@PathVariable(name="id") int id) {
        EVChargingStation evChargingStation = evService.findChargingStationById(id);
        return new ResponseEntity<>(evChargingStation, HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity<EVChargingStation> addChargingStation (@RequestParam(name = "image") MultipartFile image,
                                                                 @RequestParam(name = "stationName") String stationName,
                                                                 @RequestParam(name = "stationPricing") double stationPricing,
                                                                 @RequestParam(name = "stationAddress") String stationAddress) throws IOException {
         EVChargingStation evChargingStationRequest = EVChargingStation.builder()
                 .stationAddress(stationAddress)
                 .stationPricing(stationPricing)
                 .stationName(stationName)
                 .stationImage(image.getBytes())
                 .build();

        EVChargingStation evChargingStation = evService.addNewChargingStation(evChargingStationRequest);
        return new ResponseEntity<>(evChargingStation, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<EVChargingStation> updateChargingStation (@PathVariable(name="id") int id,
                                                                    @RequestParam(name = "image") MultipartFile image,
                                                                    @RequestParam(name = "stationName") String stationName,
                                                                    @RequestParam(name = "stationPricing") double stationPricing,
                                                                    @RequestParam(name = "stationAddress") String stationAddress) throws IOException {

        EVChargingStation evChargingStation = evService.updateExistingChargingStation(id, image,stationName,stationPricing,stationAddress);
        return new ResponseEntity<>(evChargingStation, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStation(@PathVariable(name="id") int id) {
        evService.deleteEVStation(id);
        return new ResponseEntity<>("Success",HttpStatus.OK);
    }
}
