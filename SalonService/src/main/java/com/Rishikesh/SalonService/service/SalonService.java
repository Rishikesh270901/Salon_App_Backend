package com.Rishikesh.SalonService.service;


import com.Rishikesh.SalonService.modal.Salon;
import com.Rishikesh.SalonService.payload.SalonDTO;
import com.Rishikesh.SalonService.payload.UserDTO;

import java.util.List;

public interface SalonService {

    Salon createSalon(SalonDTO salon, UserDTO user);

    Salon updateSalon(SalonDTO salon, UserDTO user, Long SalonId) throws Exception;

    List<Salon> getAllSalons();

    Salon getSalonById(Long SalonId) throws Exception;

    Salon getSalonByOwnerId(Long ownerId);

    List<Salon> searchSalonByCityName(String city);
}
