package com.Rishikesh.ServiceOfferingService.service.impl;

import com.Rishikesh.ServiceOfferingService.modal.ServiceOffering;
import com.Rishikesh.ServiceOfferingService.payload.CategoryDTO;
import com.Rishikesh.ServiceOfferingService.payload.SalonDTO;
import com.Rishikesh.ServiceOfferingService.payload.ServiceDTO;
import com.Rishikesh.ServiceOfferingService.repository.ServiceOfferingRepository;
import com.Rishikesh.ServiceOfferingService.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ServiceOfferingServiceImpl implements ServiceOfferingService {

    private final ServiceOfferingRepository serviceOfferingRepository;

    @Override
    public ServiceOffering createService(SalonDTO salonDTO, ServiceDTO serviceDTO, CategoryDTO categoryDTO) {
        ServiceOffering newServiceOffering = new ServiceOffering();
        newServiceOffering.setName(serviceDTO.getName());
        newServiceOffering.setImage(serviceDTO.getImage());
        newServiceOffering.setSalonId(salonDTO.getId());
        newServiceOffering.setDescription(serviceDTO.getDescription());
        newServiceOffering.setCategoryId(categoryDTO.getId());
        newServiceOffering.setPrice(serviceDTO.getPrice());
        newServiceOffering.setDuration(serviceDTO.getDuration());

        return serviceOfferingRepository.save(newServiceOffering);
    }

    @Override
    public ServiceOffering updateService(Long serviceId, ServiceOffering serviceOffering) {
        ServiceOffering existingServiceOffering = serviceOfferingRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service offering not found with id: " + serviceId));

        existingServiceOffering.setName(serviceOffering.getName());
        existingServiceOffering.setImage(serviceOffering.getImage());
        existingServiceOffering.setDescription(serviceOffering.getDescription());
        existingServiceOffering.setPrice(serviceOffering.getPrice());
        existingServiceOffering.setDuration(serviceOffering.getDuration());

        return serviceOfferingRepository.save(existingServiceOffering);
    }

    @Override
    public Set<ServiceOffering> getAllServiceBySalonId(Long salonId, Long categoryId) {
        Set<ServiceOffering> services = serviceOfferingRepository.findBySalonId(salonId);

        if(categoryId!=null){
            services = services.stream().filter(service -> service.getCategoryId().equals(categoryId)).collect(Collectors.toSet());
        }
        return services;
    }

    @Override
    public Set<ServiceOffering> getAllServiceByIds(Set<Long> ids) {
        return new HashSet<>(serviceOfferingRepository.findAllById(ids));
    }

    @Override
    public ServiceOffering getServiceById(Long id) {
        return serviceOfferingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service offering not found with id: " + id));

    }


}
