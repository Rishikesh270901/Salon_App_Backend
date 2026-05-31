package com.Rishikesh.SalonService.payload;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;


@Data
public class SalonDTO {
    private Long Id;

    private String name;

    private List<String> images;

    private String address;

    private String phone;

    private String email;

    private String city;

    private Long ownerId;

    private LocalTime openTime;

    private LocalTime closeTime;
}
