package com.Rishikesh.BookingService.service;

import com.Rishikesh.BookingService.domain.BookingStatus;
import com.Rishikesh.BookingService.modal.Booking;
import com.Rishikesh.BookingService.modal.SalonReport;
import com.Rishikesh.BookingService.payload.BookingRequest;
import com.Rishikesh.BookingService.payload.SalonDTO;
import com.Rishikesh.BookingService.payload.ServiceDTO;
import com.Rishikesh.BookingService.payload.UserDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface BookingService {

    Booking createBooking(BookingRequest booking, UserDTO userDTO, SalonDTO salonDTO, Set<ServiceDTO> serviceDTOSet) throws Exception;

    List<Booking> getBookingsByCustomer(Long customerId);

    List<Booking> getBookingsBySalon(Long salonId);

    Booking getBookingById(Long id) throws Exception;

    Booking updateBooking(Long bookingId, BookingStatus status) throws Exception;

    List<Booking> getBookingsByDate(LocalDate date, Long salonId);

    SalonReport getSalonreport(Long salonId);
}
