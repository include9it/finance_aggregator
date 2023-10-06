package io.include9it.finance_aggregator.service;

import io.include9it.finance_aggregator.db.ApplicationBundleEntity;
import io.include9it.finance_aggregator.db.ApplicationBundleRepository;
import io.include9it.finance_aggregator.db.ApplicationEntity;
import io.include9it.finance_aggregator.mapper.EntityMapper;
import io.include9it.finance_aggregator.dto.*;
import io.include9it.finance_aggregator.mapper.ApplicationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class FinancingService {

    private final List<ApplicationService> services;
    private final ApplicationBundleRepository repository;
    private final ApplicationMapper applicationMapper;
    private final EntityMapper entityMapper;

    public ApplicationResponse formSubmit(FormRequest formRequest) {
        List<Application> applications = services.stream()
                .map(service -> service.submitApplication(formRequest))
                .filter(Objects::nonNull)
                .toList();

        ApplicationBundleEntity entity = saveApplications(applications);

        return applicationMapper.map(entity);
    }

    public ApplicationResponse fetchOffers(UUID applicationId) {
        ApplicationBundleEntity entity = repository.findById(applicationId)
                .orElseThrow(() -> new IllegalStateException(
                        "Application id: " + applicationId + " doesn't exist!"
                ));

        entity.getApplications().stream()
                .filter(applicationEntity -> applicationEntity.getStatus() == ApplicationStatus.DRAFT)
                .forEach(this::updateApplication);

        return applicationMapper.map(entity);
    }

    private ApplicationBundleEntity saveApplications(List<Application> applications) {
        var entity = new ApplicationBundleEntity();
        entity.setApplications(applications.stream().map(entityMapper::map).toList());

        return repository.save(entity);
    }

    private void updateApplication(ApplicationEntity applicationEntity) {
        String serviceName = applicationEntity.getServiceName();

        ApplicationService applicationService = services.stream()
                .filter(service -> service.getServiceName().equals(serviceName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                                "Service: " + serviceName + " doesn't found!"
                        )
                );

        Application updatedApplication = applicationService.fetchOffer(applicationEntity.getId());
        ApplicationEntity updatedApplicationEntity = entityMapper.map(updatedApplication);

        applicationEntity.setStatus(updatedApplicationEntity.getStatus());
        applicationEntity.setOffer(updatedApplicationEntity.getOffer());
    }
}
