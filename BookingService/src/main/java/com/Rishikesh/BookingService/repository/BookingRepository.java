package com.Rishikesh.BookingService.repository;

import com.Rishikesh.BookingService.modal.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBySalonId(Long salonId);

    List<Booking> findByCustomerId(Long customerId);
}
