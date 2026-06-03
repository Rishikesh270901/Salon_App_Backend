package com.Rishikesh.BookingService.controller;


import com.Rishikesh.BookingService.domain.BookingStatus;
import com.Rishikesh.BookingService.mapper.BookingMapper;
import com.Rishikesh.BookingService.modal.Booking;
import com.Rishikesh.BookingService.payload.*;
import com.Rishikesh.BookingService.service.BookingService;
import com.Rishikesh.BookingService.service.impl.BookingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestParam Long salonId, @RequestBody BookingRequest bookingRequest) throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setId(salonId);

        Set<ServiceDTO> serviceDTOSet = new HashSet<>();

        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setId(1L);
        serviceDTO.setPrice(399);
        serviceDTO.setDuration(45);
        serviceDTO.setName("Haircut");


        serviceDTOSet.add(serviceDTO);

        Booking booking = bookingService.createBooking(bookingRequest, userDTO, salonDTO, serviceDTOSet);

        return ResponseEntity.ok(booking);

    }

    @GetMapping("/customer")
    public ResponseEntity<Set<BookingDTO>> getBookingsByCustomer(){

        List<Booking> bookings = bookingService.getBookingsByCustomer(1L);

        return ResponseEntity.ok(getBookingDTOs(bookings));
    }

    @GetMapping("/salon")
    public ResponseEntity<Set<BookingDTO>> getBookingsBySalon(){

        List<Booking> bookings = bookingService.getBookingsBySalon(1L);

        return ResponseEntity.ok(getBookingDTOs(bookings));
    }


    private Set<BookingDTO> getBookingDTOs(List<Booking> bookings) {
        return bookings.stream().map(BookingMapper::toDTO).collect(Collectors.toSet());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingsById(@PathVariable Long id) throws Exception {

        Booking bookings = bookingService.getBookingById(id);

        return ResponseEntity.ok(BookingMapper.toDTO(bookings));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<BookingDTO> updateBookingStatus(@PathVariable Long id,
                                                               @RequestParam BookingStatus status) throws Exception {

        Booking bookings = bookingService.updateBooking(id, status);

        return ResponseEntity.ok(BookingMapper.toDTO(bookings));
    }

    @GetMapping("/slots/salon/{salonId}/date/{date}")
    public ResponseEntity<Set<BookingDTO>> getBookedSlot(@RequestParam LocalDate date, @PathVariable Long salonId) throws Exception {

        List<Booking> bookings = bookingService.getBookingsByDate(date, salonId);

        return ResponseEntity.ok(getBookingDTOs(bookings));
    }
}
