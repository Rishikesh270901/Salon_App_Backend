package com.Rishikesh.ServiceOfferingService.service;

import com.Rishikesh.ServiceOfferingService.modal.ServiceOffering;
import com.Rishikesh.ServiceOfferingService.payload.SalonDTO;
import com.Rishikesh.ServiceOfferingService.payload.CategoryDTO;
import com.Rishikesh.ServiceOfferingService.payload.ServiceDTO;

import java.util.List;
import java.util.Set;

public interface ServiceOfferingService {

    ServiceOffering createService(SalonDTO salonDTO, ServiceDTO serviceDTO, CategoryDTO categoryDTO);

    ServiceOffering updateService(Long serviceId, ServiceOffering serviceOffering);

    Set<ServiceOffering> getAllServiceBySalonId(Long salonId, Long categoryId);

    Set<ServiceOffering> getAllServiceByIds(Set<Long> ids);

    ServiceOffering getServiceById(Long id);
}
