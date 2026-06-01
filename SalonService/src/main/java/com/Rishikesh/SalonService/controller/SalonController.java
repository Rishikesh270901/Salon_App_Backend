package com.Rishikesh.SalonService.controller;

import com.Rishikesh.SalonService.mapper.SalonMapper;
import com.Rishikesh.SalonService.modal.Salon;
import com.Rishikesh.SalonService.payload.SalonDTO;
import com.Rishikesh.SalonService.payload.UserDTO;
import com.Rishikesh.SalonService.service.SalonService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/salons")
public class SalonController {

    private final SalonService salonService;

    @PostMapping
    public ResponseEntity<SalonDTO> createSalon(@RequestBody SalonDTO salonDTO) {
        UserDTO user = new UserDTO(); // This should be replaced with actual user retrieval logic
        user.setId(1L); // Example user ID, replace with actual user ID
        Salon newSalon = salonService.createSalon(salonDTO, user);
        SalonDTO responseSalonDTO = SalonMapper.mapToDTO(newSalon);
        return ResponseEntity.ok(responseSalonDTO);
    }

    @PatchMapping("/{salonId}")
    public ResponseEntity<SalonDTO> updateSalon(@RequestBody SalonDTO salonDTO,
                                                @PathVariable Long salonId) throws Exception {
        UserDTO user = new UserDTO(); // This should be replaced with actual user retrieval logic
        user.setId(1L); // Example user ID, replace with actual user ID
        Salon updateSalon = salonService.updateSalon(salonDTO, user, salonId);
        SalonDTO responseSalonDTO = SalonMapper.mapToDTO(updateSalon);
        return ResponseEntity.ok(responseSalonDTO);
    }

    @GetMapping
    public ResponseEntity<List<SalonDTO>> getSalons(){
        List<Salon> salons = salonService.getAllSalons();
        List<SalonDTO> salonDTOs = salons.stream().map(SalonMapper::mapToDTO
                ).toList();
        return ResponseEntity.ok(salonDTOs);
    }

    @GetMapping("/{salonId}")
    public ResponseEntity<SalonDTO> getSalonById(@PathVariable Long salonId) throws Exception {
        Salon salon = salonService.getSalonById(salonId);
        SalonDTO responseSalonDTO = SalonMapper.mapToDTO(salon);
        return ResponseEntity.ok(responseSalonDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SalonDTO>> searchSalon(@RequestParam("city") String city) throws Exception {
        List<Salon> salons = salonService.searchSalonByCityName(city);
        List<SalonDTO> salonDTOs = salons.stream().map(SalonMapper::mapToDTO
        ).toList();
        return ResponseEntity.ok(salonDTOs);
    }

    @GetMapping("/owner")
    public ResponseEntity<SalonDTO> getSalonByOwnerId() throws Exception {
        UserDTO user = new UserDTO(); // This should be replaced with actual user retrieval logic
        user.setId(1L); // Example user ID, replace with actual user ID
        Salon salon = salonService.getSalonByOwnerId(user.getId());
        SalonDTO responseSalonDTO = SalonMapper.mapToDTO(salon);
        return ResponseEntity.ok(responseSalonDTO);
    }


}
