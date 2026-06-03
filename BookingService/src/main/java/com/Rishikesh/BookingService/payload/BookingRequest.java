package com.Rishikesh.BookingService.payload;

import com.Rishikesh.BookingService.domain.BookingStatus;
import jakarta.persistence.ElementCollection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class BookingRequest {

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Set<Long> serviceIds;
}
