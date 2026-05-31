package com.Rishikesh.SalonService.service.Impl;

import com.Rishikesh.SalonService.modal.Salon;
import com.Rishikesh.SalonService.payload.SalonDTO;
import com.Rishikesh.SalonService.payload.UserDTO;
import com.Rishikesh.SalonService.repository.SalonRepository;
import com.Rishikesh.SalonService.service.SalonService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalonServiceImpl implements SalonService {

    private final SalonRepository salonRepository;

    @Override
    public Salon createSalon(SalonDTO salon, UserDTO user) {
        Salon newSalon = new Salon();
        newSalon.setName(salon.getName());
        newSalon.setAddress(salon.getAddress());
        newSalon.setPhone(salon.getPhone());
        newSalon.setEmail(salon.getEmail());
        newSalon.setCity(salon.getCity());
        newSalon.setOpenTime(salon.getOpenTime());
        newSalon.setCloseTime(salon.getCloseTime());
        newSalon.setImages(salon.getImages());
        newSalon.setOwnerId(salon.getOwnerId());

        return salonRepository.save(newSalon);
    }

    @Override
    public Salon updateSalon(SalonDTO salon, UserDTO user, Long SalonId) throws Exception {
        Salon existingSalon = salonRepository.findById(SalonId).orElseThrow(() -> new Exception("Salon not found with id: " + SalonId));

        if(salon.getOwnerId().equals(user.getId())) {
            existingSalon.setCity(salon.getCity());
            existingSalon.setName(salon.getName());
            existingSalon.setAddress(salon.getAddress());
            existingSalon.setPhone(salon.getPhone());
            existingSalon.setEmail(salon.getEmail());
            existingSalon.setOpenTime(salon.getOpenTime());
            existingSalon.setCloseTime(salon.getCloseTime());
            existingSalon.setImages(salon.getImages());
            existingSalon.setOwnerId(user.getId());
            return salonRepository.save(existingSalon);
        }
        throw new Exception("Salon not found with id: " + SalonId);
    }

    @Override
    public List<Salon> getAllSalons() {
        return salonRepository.findAll();
    }

    @Override
    public Salon getSalonById(Long SalonId) throws Exception {
        Salon salon = salonRepository.findById(SalonId).orElse(null);
        if(salon==null){
            throw new Exception("Salon not exist");
        }
        return salon;
    }

    @Override
    public Salon getSalonByOwnerId(Long ownerId) {
        return salonRepository.findByOwnerId(ownerId);
    }

    @Override
    public List<Salon> searchSalonByCityName(String city) {
        return salonRepository.searchSalon(city);
    }
}
