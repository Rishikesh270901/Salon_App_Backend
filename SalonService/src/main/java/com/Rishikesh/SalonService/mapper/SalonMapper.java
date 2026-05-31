package com.Rishikesh.SalonService.mapper;

import com.Rishikesh.SalonService.modal.Salon;
import com.Rishikesh.SalonService.payload.SalonDTO;

public class SalonMapper {

    public static SalonDTO mapToDTO(Salon salon){
        SalonDTO salonDTO = new SalonDTO();

        salonDTO.setName(salon.getName());
        salonDTO.setAddress(salon.getAddress());
        salonDTO.setCity(salon.getCity());
        salonDTO.setImages(salon.getImages());
        salonDTO.setCloseTime(salon.getCloseTime());
        salonDTO.setOpenTime(salon.getOpenTime());
        salonDTO.setEmail(salon.getEmail());
        salonDTO.setPhone(salon.getPhone());
        salonDTO.setOwnerId(salon.getOwnerId());
        return salonDTO;
    }
}
