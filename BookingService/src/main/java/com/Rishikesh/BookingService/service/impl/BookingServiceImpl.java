package com.Rishikesh.BookingService.service.impl;

import com.Rishikesh.BookingService.domain.BookingStatus;
import com.Rishikesh.BookingService.modal.Booking;
import com.Rishikesh.BookingService.modal.SalonReport;
import com.Rishikesh.BookingService.payload.BookingRequest;
import com.Rishikesh.BookingService.payload.SalonDTO;
import com.Rishikesh.BookingService.payload.ServiceDTO;
import com.Rishikesh.BookingService.payload.UserDTO;
import com.Rishikesh.BookingService.repository.BookingRepository;
import com.Rishikesh.BookingService.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    public boolean isTimeSlotAvailable(SalonDTO salonDTO, LocalDateTime bookingStartTime, LocalDateTime bookingEndTime) throws Exception {

        List<Booking> existingBookings = getBookingsBySalon(salonDTO.getId());

        LocalDateTime salonOpenTime = salonDTO.getOpenTime().atDate(bookingStartTime.toLocalDate());
        LocalDateTime salonCloseTime = salonDTO.getCloseTime().atDate(bookingStartTime.toLocalDate());

        if(bookingStartTime.isBefore(salonOpenTime) || bookingEndTime.isAfter(salonCloseTime)){
            throw new Exception("Booking time must be within salon's working hours");
        }

        for(Booking existingBooking : existingBookings){
            LocalDateTime existingBookingStartTime = existingBooking.getStartTime();
            LocalDateTime existingBookingEndTime = existingBooking.getEndTime();

            if(bookingStartTime.isBefore(existingBookingEndTime) && bookingEndTime.isAfter(existingBookingStartTime)){
                throw new Exception("Time slot is not available");
            }

            if(bookingStartTime.isEqual(existingBookingStartTime) || bookingEndTime.isEqual(existingBookingEndTime)){
                throw new Exception("Time slot is not available");
            }
        }

        return true;
    }

    @Override
    public Booking createBooking(BookingRequest booking, UserDTO userDTO, SalonDTO salonDTO, Set<ServiceDTO> serviceDTOSet) throws Exception {
        int totalDuration = serviceDTOSet.stream().mapToInt(ServiceDTO::getDuration).sum();

        LocalDateTime bookingStartTime = booking.getStartTime();
        LocalDateTime bookingEndTime = bookingStartTime.plusMinutes(totalDuration);

        boolean isSlotAvailable = isTimeSlotAvailable(salonDTO, bookingStartTime, bookingEndTime);

        int totalPrice = serviceDTOSet.stream().mapToInt(ServiceDTO::getPrice).sum();

        Set<Long> idList = serviceDTOSet.stream().map(ServiceDTO::getId).collect(Collectors.toSet());

        Booking newBooking = new Booking();

        newBooking.setCustomerId(userDTO.getId());
        newBooking.setSalonId(salonDTO.getId());
        newBooking.setStartTime(bookingStartTime);
        newBooking.setEndTime(bookingEndTime);
        newBooking.setServiceIds(idList);
        newBooking.setTotalPrice(totalPrice);
        newBooking.setStatus(BookingStatus.PENDING);

        return bookingRepository.save(newBooking);
    }

    @Override
    public List<Booking> getBookingsByCustomer(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Booking> getBookingsBySalon(Long salonId) {
        return bookingRepository.findBySalonId(salonId);
    }

    @Override
    public Booking getBookingById(Long id) throws Exception {
        Booking booking =  bookingRepository.findById(id).orElse(null);
        if(booking==null){
            throw new Exception("Booking not found.");
        }

        return booking;
    }

    @Override
    public Booking updateBooking(Long bookingId, BookingStatus status) throws Exception {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if(booking==null){
            throw new Exception("Booking not found.");
        }

        booking.setStatus(status);

        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingsByDate(LocalDate date, Long salonId) {
        List<Booking> bookings = bookingRepository.findBySalonId(salonId);

        if(date==null){
            return bookings;
        }

        return bookings.stream()
                .filter(booking -> isSamedate(booking.getStartTime(), date) ||
                        isSamedate(booking.getEndTime(), date))
                .collect(Collectors.toList());
    }

    private boolean isSamedate(LocalDateTime startTime, LocalDate date) {
        return startTime.toLocalDate().isEqual(date);
    }

    @Override
    public SalonReport getSalonreport(Long salonId) {

        List<Booking> bookings = bookingRepository.findBySalonId(salonId);

        int totalEarnings = bookings.stream().mapToInt(Booking::getTotalPrice).sum();

        Integer totalBooking = bookings.size();

        List<Booking> cancelledBookings = bookings.stream()
                .filter(booking -> booking.getStatus() == BookingStatus.CANCELLED)
                .toList();

        Double totalRefund = cancelledBookings.stream().mapToDouble(Booking::getTotalPrice).sum();

        SalonReport salonReport = new SalonReport();

        salonReport.setSalonId(salonId);
//        salonReport.setSalonName();
        salonReport.setCancelledBookings(cancelledBookings.size());
        salonReport.setTotalBookings(totalBooking);
        salonReport.setTotalEarnings(totalEarnings);
        salonReport.setTotalRefund(totalRefund);
        salonReport.setTotalBookings(totalBooking);


        return salonReport;
    }
}
