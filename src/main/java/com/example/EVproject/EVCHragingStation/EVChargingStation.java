package com.example.EVproject.EVCHragingStation;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "EVChargingStation")
public class EVChargingStation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "station_name")
    private String stationName;

    @Lob
    @Column(name = "station_image")
    private byte[] stationImage;

    @Column(name = "station_pricing")
    private Double stationPricing;

    @Column(name = "station_address")
    private String stationAddress;

}
