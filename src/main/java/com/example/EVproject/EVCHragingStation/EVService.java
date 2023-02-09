package com.example.EVproject.EVCHragingStation;

import com.example.EVproject.Exception.StationNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class EVService {

    private final EVRepository evRepository;

    public EVService(EVRepository evRepository) {
        this.evRepository = evRepository;
    }

    public List<EVChargingStation> findAllChargingStation(int limit,String sortOrder,String param) {
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(param).ascending()
                : Sort.by(param).descending();
        Pageable pageable = PageRequest.of(0, limit,sort);
        return evRepository.findAll(pageable).getContent();
    }

    public EVChargingStation findChargingStationById(int id) {
        return evRepository.findById(id).orElseThrow(() -> new StationNotFoundException("station id: " + id + " was not found"));
    }

    public EVChargingStation addNewChargingStation(EVChargingStation evChargingStationRequest) {
        return evRepository.save(evChargingStationRequest);
    }

    public EVChargingStation updateExistingChargingStation(int id, MultipartFile image, String stationName,double stationPricing,String stationAddress) throws IOException {
        EVChargingStation station = findChargingStationById(id);
        station.setStationName(stationName);
        station.setStationAddress(stationAddress);
        station.setStationPricing(stationPricing);
        station.setStationImage(image.getBytes());
        return evRepository.save(station);
    }

    public void deleteEVStation(int id) {
        findChargingStationById(id);
         evRepository.deleteById(id);
    }



}
