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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/salons")
public class SalonController {

    private final SalonService salonService;

    @PostMapping("/")
    public ResponseEntity<SalonDTO> createSalon(@RequestBody SalonDTO salonDTO) {
        UserDTO user = new UserDTO(); // This should be replaced with actual user retrieval logic
        user.setId(1L); // Example user ID, replace with actual user ID
        Salon newSalon = salonService.createSalon(salonDTO, user);
        SalonDTO responseSalonDTO = SalonMapper.mapToDTO(newSalon);
        return ResponseEntity.ok(responseSalonDTO);
    }
}
