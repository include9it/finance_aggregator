package io.include9it.finance_aggregator.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.include9it.finance_aggregator.db.ApplicationBundleEntity;
import io.include9it.finance_aggregator.db.ApplicationBundleRepository;
import io.include9it.finance_aggregator.db.ApplicationEntity;
import io.include9it.finance_aggregator.db.OfferEntity;
import io.include9it.finance_aggregator.dto.Application;
import io.include9it.finance_aggregator.dto.ApplicationResponse;
import io.include9it.finance_aggregator.dto.ApplicationStatus;
import io.include9it.finance_aggregator.dto.FormRequest;
import io.include9it.finance_aggregator.dto.MaritalStatus;
import io.include9it.finance_aggregator.dto.Offer;
import io.include9it.finance_aggregator.mapper.ApplicationMapper;
import io.include9it.finance_aggregator.mapper.EntityMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Just checked how DiffBlue AI plugin works
 * And it seems it nicely make full coverage in few seconds
 * But tests became too dirty and huge per block
 */
@ContextConfiguration(classes = {FinancingService.class})
@ExtendWith(SpringExtension.class)
class FinancingServiceTest {
    @MockBean
    private ApplicationBundleRepository applicationBundleRepository;

    @MockBean
    private ApplicationMapper applicationMapper;

    @MockBean
    private ApplicationService applicationService;

    @MockBean
    private EntityMapper entityMapper;

    @Autowired
    private FinancingService financingService;

    @Autowired
    private List<ApplicationService> list;

    /**
     * Method under test: {@link FinancingService#formSubmit(FormRequest)}
     */
    @Test
    void testFormSubmit() {
        BigDecimal monthlyPaymentAmount = BigDecimal.valueOf(1L);
        BigDecimal totalRepaymentAmount = BigDecimal.valueOf(1L);
        BigDecimal annualPercentageRate = BigDecimal.valueOf(1L);
        when(applicationService.submitApplication(Mockito.<FormRequest>any())).thenReturn(new Application("Service Name",
                "42", ApplicationStatus.DRAFT,
                new Offer(monthlyPaymentAmount, totalRepaymentAmount, 10, annualPercentageRate, LocalDate.of(1970, 1, 1))));

        ApplicationBundleEntity applicationBundleEntity = new ApplicationBundleEntity();
        applicationBundleEntity.setApplications(new ArrayList<>());
        applicationBundleEntity.setId(UUID.randomUUID());
        when(applicationBundleRepository.save(Mockito.<ApplicationBundleEntity>any()))
                .thenReturn(applicationBundleEntity);
        UUID id = UUID.randomUUID();
        ApplicationResponse applicationResponse = new ApplicationResponse(id, new ArrayList<>());

        when(applicationMapper.map(Mockito.<ApplicationBundleEntity>any())).thenReturn(applicationResponse);

        OfferEntity offer = new OfferEntity();
        offer.setAnnualPercentageRate(BigDecimal.valueOf(1L));
        offer.setFirstRepaymentDate(LocalDate.of(1970, 1, 1));
        offer.setId(1L);
        offer.setMonthlyPaymentAmount(BigDecimal.valueOf(1L));
        offer.setNumberOfPayments(10);
        offer.setTotalRepaymentAmount(BigDecimal.valueOf(1L));

        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setId("42");
        applicationEntity.setOffer(offer);
        applicationEntity.setServiceName("Service Name");
        applicationEntity.setStatus(ApplicationStatus.DRAFT);
        when(entityMapper.map(Mockito.<Application>any())).thenReturn(applicationEntity);
        BigDecimal monthlyIncome = BigDecimal.valueOf(1L);
        BigDecimal monthlyExpenses = BigDecimal.valueOf(1L);
        assertSame(applicationResponse,
                financingService.formSubmit(new FormRequest("Name", "Doe", "6625550144", "jane.doe@example.org",
                        monthlyIncome, monthlyExpenses, 3, MaritalStatus.SINGLE, true, true, BigDecimal.valueOf(1L))));
        verify(applicationService).submitApplication(Mockito.<FormRequest>any());
        verify(applicationBundleRepository).save(Mockito.<ApplicationBundleEntity>any());
        verify(applicationMapper).map(Mockito.<ApplicationBundleEntity>any());
        verify(entityMapper).map(Mockito.<Application>any());
    }

    /**
     * Method under test: {@link FinancingService#formSubmit(FormRequest)}
     */
    @Test
    void testFormSubmit2() {
        BigDecimal monthlyPaymentAmount = BigDecimal.valueOf(1L);
        BigDecimal totalRepaymentAmount = BigDecimal.valueOf(1L);
        BigDecimal annualPercentageRate = BigDecimal.valueOf(1L);
        when(applicationService.submitApplication(Mockito.<FormRequest>any())).thenReturn(new Application("Service Name",
                "42", ApplicationStatus.DRAFT,
                new Offer(monthlyPaymentAmount, totalRepaymentAmount, 10, annualPercentageRate, LocalDate.of(1970, 1, 1))));
        when(entityMapper.map(Mockito.<Application>any())).thenThrow(new IllegalArgumentException("foo"));
        BigDecimal monthlyIncome = BigDecimal.valueOf(1L);
        BigDecimal monthlyExpenses = BigDecimal.valueOf(1L);
        assertThrows(IllegalArgumentException.class,
                () -> financingService.formSubmit(new FormRequest("Name", "Doe", "6625550144", "jane.doe@example.org",
                        monthlyIncome, monthlyExpenses, 3, MaritalStatus.SINGLE, true, true, BigDecimal.valueOf(1L))));
        verify(applicationService).submitApplication(Mockito.<FormRequest>any());
        verify(entityMapper).map(Mockito.<Application>any());
    }

    /**
     * Method under test: {@link FinancingService#fetchOffers(UUID)}
     */
    @Test
    void testFetchOffers() {
        ApplicationBundleEntity applicationBundleEntity = new ApplicationBundleEntity();
        applicationBundleEntity.setApplications(new ArrayList<>());
        applicationBundleEntity.setId(UUID.randomUUID());
        Optional<ApplicationBundleEntity> ofResult = Optional.of(applicationBundleEntity);
        when(applicationBundleRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);
        UUID id = UUID.randomUUID();
        ApplicationResponse applicationResponse = new ApplicationResponse(id, new ArrayList<>());

        when(applicationMapper.map(Mockito.<ApplicationBundleEntity>any())).thenReturn(applicationResponse);
        assertSame(applicationResponse, financingService.fetchOffers(UUID.randomUUID()));
        verify(applicationBundleRepository).findById(Mockito.<UUID>any());
        verify(applicationMapper).map(Mockito.<ApplicationBundleEntity>any());
    }

    /**
     * Method under test: {@link FinancingService#fetchOffers(UUID)}
     */
    @Test
    void testFetchOffers2() {
        ApplicationBundleEntity applicationBundleEntity = new ApplicationBundleEntity();
        applicationBundleEntity.setApplications(new ArrayList<>());
        applicationBundleEntity.setId(UUID.randomUUID());
        Optional<ApplicationBundleEntity> ofResult = Optional.of(applicationBundleEntity);
        when(applicationBundleRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);
        when(applicationMapper.map(Mockito.<ApplicationBundleEntity>any()))
                .thenThrow(new IllegalArgumentException("foo"));
        assertThrows(IllegalArgumentException.class, () -> financingService.fetchOffers(UUID.randomUUID()));
        verify(applicationBundleRepository).findById(Mockito.<UUID>any());
        verify(applicationMapper).map(Mockito.<ApplicationBundleEntity>any());
    }

    /**
     * Method under test: {@link FinancingService#fetchOffers(UUID)}
     */
    @Test
    void testFetchOffers3() {
        BigDecimal monthlyPaymentAmount = BigDecimal.valueOf(1L);
        BigDecimal totalRepaymentAmount = BigDecimal.valueOf(1L);
        BigDecimal annualPercentageRate = BigDecimal.valueOf(1L);
        when(applicationService.fetchOffer(Mockito.<String>any())).thenReturn(new Application("Service Name", "42",
                ApplicationStatus.DRAFT,
                new Offer(monthlyPaymentAmount, totalRepaymentAmount, 10, annualPercentageRate, LocalDate.of(1970, 1, 1))));
        when(applicationService.getServiceName()).thenReturn("Service Name");

        OfferEntity offer = new OfferEntity();
        offer.setAnnualPercentageRate(BigDecimal.valueOf(1L));
        offer.setFirstRepaymentDate(LocalDate.of(1970, 1, 1));
        offer.setId(1L);
        offer.setMonthlyPaymentAmount(BigDecimal.valueOf(1L));
        offer.setNumberOfPayments(10);
        offer.setTotalRepaymentAmount(BigDecimal.valueOf(1L));

        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setId("42");
        applicationEntity.setOffer(offer);
        applicationEntity.setServiceName("Service Name");
        applicationEntity.setStatus(ApplicationStatus.DRAFT);

        ArrayList<ApplicationEntity> applications = new ArrayList<>();
        applications.add(applicationEntity);

        ApplicationBundleEntity applicationBundleEntity = new ApplicationBundleEntity();
        applicationBundleEntity.setApplications(applications);
        applicationBundleEntity.setId(UUID.randomUUID());
        Optional<ApplicationBundleEntity> ofResult = Optional.of(applicationBundleEntity);
        when(applicationBundleRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);
        UUID id = UUID.randomUUID();
        ApplicationResponse applicationResponse = new ApplicationResponse(id, new ArrayList<>());

        when(applicationMapper.map(Mockito.<ApplicationBundleEntity>any())).thenReturn(applicationResponse);

        OfferEntity offer2 = new OfferEntity();
        offer2.setAnnualPercentageRate(BigDecimal.valueOf(1L));
        offer2.setFirstRepaymentDate(LocalDate.of(1970, 1, 1));
        offer2.setId(1L);
        offer2.setMonthlyPaymentAmount(BigDecimal.valueOf(1L));
        offer2.setNumberOfPayments(10);
        offer2.setTotalRepaymentAmount(BigDecimal.valueOf(1L));

        ApplicationEntity applicationEntity2 = new ApplicationEntity();
        applicationEntity2.setId("42");
        applicationEntity2.setOffer(offer2);
        applicationEntity2.setServiceName("Service Name");
        applicationEntity2.setStatus(ApplicationStatus.DRAFT);
        when(entityMapper.map(Mockito.<Application>any())).thenReturn(applicationEntity2);
        assertSame(applicationResponse, financingService.fetchOffers(UUID.randomUUID()));
        verify(applicationService).fetchOffer(Mockito.<String>any());
        verify(applicationService).getServiceName();
        verify(applicationBundleRepository).findById(Mockito.<UUID>any());
        verify(applicationMapper).map(Mockito.<ApplicationBundleEntity>any());
        verify(entityMapper).map(Mockito.<Application>any());
    }

    /**
     * Method under test: {@link FinancingService#fetchOffers(UUID)}
     */
    @Test
    void testFetchOffers4() {
        BigDecimal monthlyPaymentAmount = BigDecimal.valueOf(1L);
        BigDecimal totalRepaymentAmount = BigDecimal.valueOf(1L);
        BigDecimal annualPercentageRate = BigDecimal.valueOf(1L);
        when(applicationService.fetchOffer(Mockito.<String>any())).thenReturn(new Application("Service Name", "42",
                ApplicationStatus.DRAFT,
                new Offer(monthlyPaymentAmount, totalRepaymentAmount, 10, annualPercentageRate, LocalDate.of(1970, 1, 1))));
        when(applicationService.getServiceName()).thenReturn("Service Name");

        OfferEntity offer = new OfferEntity();
        offer.setAnnualPercentageRate(BigDecimal.valueOf(1L));
        offer.setFirstRepaymentDate(LocalDate.of(1970, 1, 1));
        offer.setId(1L);
        offer.setMonthlyPaymentAmount(BigDecimal.valueOf(1L));
        offer.setNumberOfPayments(10);
        offer.setTotalRepaymentAmount(BigDecimal.valueOf(1L));

        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setId("42");
        applicationEntity.setOffer(offer);
        applicationEntity.setServiceName("Service Name");
        applicationEntity.setStatus(ApplicationStatus.DRAFT);

        ArrayList<ApplicationEntity> applications = new ArrayList<>();
        applications.add(applicationEntity);

        ApplicationBundleEntity applicationBundleEntity = new ApplicationBundleEntity();
        applicationBundleEntity.setApplications(applications);
        applicationBundleEntity.setId(UUID.randomUUID());
        Optional<ApplicationBundleEntity> ofResult = Optional.of(applicationBundleEntity);
        when(applicationBundleRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);
        when(entityMapper.map(Mockito.<Application>any())).thenThrow(new IllegalArgumentException("Service Name"));
        assertThrows(IllegalArgumentException.class, () -> financingService.fetchOffers(UUID.randomUUID()));
        verify(applicationService).fetchOffer(Mockito.<String>any());
        verify(applicationService).getServiceName();
        verify(applicationBundleRepository).findById(Mockito.<UUID>any());
        verify(entityMapper).map(Mockito.<Application>any());
    }

    /**
     * Method under test: {@link FinancingService#fetchOffers(UUID)}
     */
    @Test
    void testFetchOffers5() {
        when(applicationService.getServiceName()).thenReturn("foo");

        OfferEntity offer = new OfferEntity();
        offer.setAnnualPercentageRate(BigDecimal.valueOf(1L));
        offer.setFirstRepaymentDate(LocalDate.of(1970, 1, 1));
        offer.setId(1L);
        offer.setMonthlyPaymentAmount(BigDecimal.valueOf(1L));
        offer.setNumberOfPayments(10);
        offer.setTotalRepaymentAmount(BigDecimal.valueOf(1L));

        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setId("42");
        applicationEntity.setOffer(offer);
        applicationEntity.setServiceName("Service Name");
        applicationEntity.setStatus(ApplicationStatus.DRAFT);

        ArrayList<ApplicationEntity> applications = new ArrayList<>();
        applications.add(applicationEntity);

        ApplicationBundleEntity applicationBundleEntity = new ApplicationBundleEntity();
        applicationBundleEntity.setApplications(applications);
        applicationBundleEntity.setId(UUID.randomUUID());
        Optional<ApplicationBundleEntity> ofResult = Optional.of(applicationBundleEntity);
        when(applicationBundleRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);
        assertThrows(IllegalArgumentException.class, () -> financingService.fetchOffers(UUID.randomUUID()));
        verify(applicationService).getServiceName();
        verify(applicationBundleRepository).findById(Mockito.<UUID>any());
    }

    /**
     * Method under test: {@link FinancingService#fetchOffers(UUID)}
     */
    @Test
    void testFetchOffers6() {
        BigDecimal monthlyPaymentAmount = BigDecimal.valueOf(1L);
        BigDecimal totalRepaymentAmount = BigDecimal.valueOf(1L);
        BigDecimal annualPercentageRate = BigDecimal.valueOf(1L);
        when(applicationService.fetchOffer(Mockito.<String>any())).thenReturn(new Application("Service Name", "42",
                ApplicationStatus.DRAFT,
                new Offer(monthlyPaymentAmount, totalRepaymentAmount, 10, annualPercentageRate, LocalDate.of(1970, 1, 1))));
        when(applicationService.getServiceName()).thenReturn("Service Name");

        OfferEntity offer = new OfferEntity();
        offer.setAnnualPercentageRate(BigDecimal.valueOf(1L));
        offer.setFirstRepaymentDate(LocalDate.of(1970, 1, 1));
        offer.setId(1L);
        offer.setMonthlyPaymentAmount(BigDecimal.valueOf(1L));
        offer.setNumberOfPayments(10);
        offer.setTotalRepaymentAmount(BigDecimal.valueOf(1L));

        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setId("42");
        applicationEntity.setOffer(offer);
        applicationEntity.setServiceName("Service Name");
        applicationEntity.setStatus(ApplicationStatus.DRAFT);

        OfferEntity offer2 = new OfferEntity();
        offer2.setAnnualPercentageRate(BigDecimal.valueOf(1L));
        offer2.setFirstRepaymentDate(LocalDate.of(1970, 1, 1));
        offer2.setId(2L);
        offer2.setMonthlyPaymentAmount(BigDecimal.valueOf(1L));
        offer2.setNumberOfPayments(1);
        offer2.setTotalRepaymentAmount(BigDecimal.valueOf(1L));

        ApplicationEntity applicationEntity2 = new ApplicationEntity();
        applicationEntity2.setId("Service Name");
        applicationEntity2.setOffer(offer2);
        applicationEntity2.setServiceName(
                String.join("", "io.", System.getProperty("user.name"), ".finance_aggregator.db.ApplicationEntity"));
        applicationEntity2.setStatus(ApplicationStatus.PROCESSED);

        ArrayList<ApplicationEntity> applications = new ArrayList<>();
        applications.add(applicationEntity2);
        applications.add(applicationEntity);

        ApplicationBundleEntity applicationBundleEntity = new ApplicationBundleEntity();
        applicationBundleEntity.setApplications(applications);
        applicationBundleEntity.setId(UUID.randomUUID());
        Optional<ApplicationBundleEntity> ofResult = Optional.of(applicationBundleEntity);
        when(applicationBundleRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);
        UUID id = UUID.randomUUID();
        ApplicationResponse applicationResponse = new ApplicationResponse(id, new ArrayList<>());

        when(applicationMapper.map(Mockito.<ApplicationBundleEntity>any())).thenReturn(applicationResponse);

        OfferEntity offer3 = new OfferEntity();
        offer3.setAnnualPercentageRate(BigDecimal.valueOf(1L));
        offer3.setFirstRepaymentDate(LocalDate.of(1970, 1, 1));
        offer3.setId(1L);
        offer3.setMonthlyPaymentAmount(BigDecimal.valueOf(1L));
        offer3.setNumberOfPayments(10);
        offer3.setTotalRepaymentAmount(BigDecimal.valueOf(1L));

        ApplicationEntity applicationEntity3 = new ApplicationEntity();
        applicationEntity3.setId("42");
        applicationEntity3.setOffer(offer3);
        applicationEntity3.setServiceName("Service Name");
        applicationEntity3.setStatus(ApplicationStatus.DRAFT);
        when(entityMapper.map(Mockito.<Application>any())).thenReturn(applicationEntity3);
        assertSame(applicationResponse, financingService.fetchOffers(UUID.randomUUID()));
        verify(applicationService).fetchOffer(Mockito.<String>any());
        verify(applicationService).getServiceName();
        verify(applicationBundleRepository).findById(Mockito.<UUID>any());
        verify(applicationMapper).map(Mockito.<ApplicationBundleEntity>any());
        verify(entityMapper).map(Mockito.<Application>any());
    }

    /**
     * Method under test: {@link FinancingService#fetchOffers(UUID)}
     */
    @Test
    void testFetchOffers7() {
        BigDecimal monthlyPaymentAmount = BigDecimal.valueOf(1L);
        BigDecimal totalRepaymentAmount = BigDecimal.valueOf(1L);
        BigDecimal annualPercentageRate = BigDecimal.valueOf(1L);
        when(applicationService.fetchOffer(Mockito.<String>any())).thenReturn(new Application("Service Name", "42",
                ApplicationStatus.DRAFT,
                new Offer(monthlyPaymentAmount, totalRepaymentAmount, 10, annualPercentageRate, LocalDate.of(1970, 1, 1))));
        when(applicationService.getServiceName()).thenReturn("Service Name");

        OfferEntity offer = new OfferEntity();
        offer.setAnnualPercentageRate(BigDecimal.valueOf(1L));
        offer.setFirstRepaymentDate(LocalDate.of(1970, 1, 1));
        offer.setId(1L);
        offer.setMonthlyPaymentAmount(BigDecimal.valueOf(1L));
        offer.setNumberOfPayments(10);
        offer.setTotalRepaymentAmount(BigDecimal.valueOf(1L));
        ApplicationEntity applicationEntity = mock(ApplicationEntity.class);
        when(applicationEntity.getId()).thenReturn("42");
        when(applicationEntity.getServiceName()).thenReturn("Service Name");
        when(applicationEntity.getStatus()).thenReturn(ApplicationStatus.DRAFT);
        doNothing().when(applicationEntity).setId(Mockito.<String>any());
        doNothing().when(applicationEntity).setOffer(Mockito.<OfferEntity>any());
        doNothing().when(applicationEntity).setServiceName(Mockito.<String>any());
        doNothing().when(applicationEntity).setStatus(Mockito.<ApplicationStatus>any());
        applicationEntity.setId("42");
        applicationEntity.setOffer(offer);
        applicationEntity.setServiceName("Service Name");
        applicationEntity.setStatus(ApplicationStatus.DRAFT);

        ArrayList<ApplicationEntity> applications = new ArrayList<>();
        applications.add(applicationEntity);

        ApplicationBundleEntity applicationBundleEntity = new ApplicationBundleEntity();
        applicationBundleEntity.setApplications(applications);
        applicationBundleEntity.setId(UUID.randomUUID());
        Optional<ApplicationBundleEntity> ofResult = Optional.of(applicationBundleEntity);
        when(applicationBundleRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);
        UUID id = UUID.randomUUID();
        ApplicationResponse applicationResponse = new ApplicationResponse(id, new ArrayList<>());

        when(applicationMapper.map(Mockito.<ApplicationBundleEntity>any())).thenReturn(applicationResponse);

        OfferEntity offer2 = new OfferEntity();
        offer2.setAnnualPercentageRate(BigDecimal.valueOf(1L));
        offer2.setFirstRepaymentDate(LocalDate.of(1970, 1, 1));
        offer2.setId(1L);
        offer2.setMonthlyPaymentAmount(BigDecimal.valueOf(1L));
        offer2.setNumberOfPayments(10);
        offer2.setTotalRepaymentAmount(BigDecimal.valueOf(1L));

        ApplicationEntity applicationEntity2 = new ApplicationEntity();
        applicationEntity2.setId("42");
        applicationEntity2.setOffer(offer2);
        applicationEntity2.setServiceName("Service Name");
        applicationEntity2.setStatus(ApplicationStatus.DRAFT);
        when(entityMapper.map(Mockito.<Application>any())).thenReturn(applicationEntity2);
        assertSame(applicationResponse, financingService.fetchOffers(UUID.randomUUID()));
        verify(applicationService).fetchOffer(Mockito.<String>any());
        verify(applicationService).getServiceName();
        verify(applicationBundleRepository).findById(Mockito.<UUID>any());
        verify(applicationEntity).getStatus();
        verify(applicationEntity).getId();
        verify(applicationEntity).getServiceName();
        verify(applicationEntity).setId(Mockito.<String>any());
        verify(applicationEntity, atLeast(1)).setOffer(Mockito.<OfferEntity>any());
        verify(applicationEntity).setServiceName(Mockito.<String>any());
        verify(applicationEntity, atLeast(1)).setStatus(Mockito.<ApplicationStatus>any());
        verify(applicationMapper).map(Mockito.<ApplicationBundleEntity>any());
        verify(entityMapper).map(Mockito.<Application>any());
    }

    /**
     * Method under test: {@link FinancingService#fetchOffers(UUID)}
     */
    @Test
    void testFetchOffers8() {
        when(applicationService.getServiceName()).thenReturn("Service Name");

        OfferEntity offer = new OfferEntity();
        offer.setAnnualPercentageRate(BigDecimal.valueOf(1L));
        offer.setFirstRepaymentDate(LocalDate.of(1970, 1, 1));
        offer.setId(1L);
        offer.setMonthlyPaymentAmount(BigDecimal.valueOf(1L));
        offer.setNumberOfPayments(10);
        offer.setTotalRepaymentAmount(BigDecimal.valueOf(1L));
        ApplicationEntity applicationEntity = mock(ApplicationEntity.class);
        when(applicationEntity.getId()).thenThrow(new IllegalArgumentException("Service Name"));
        when(applicationEntity.getServiceName()).thenReturn("Service Name");
        when(applicationEntity.getStatus()).thenReturn(ApplicationStatus.DRAFT);
        doNothing().when(applicationEntity).setId(Mockito.<String>any());
        doNothing().when(applicationEntity).setOffer(Mockito.<OfferEntity>any());
        doNothing().when(applicationEntity).setServiceName(Mockito.<String>any());
        doNothing().when(applicationEntity).setStatus(Mockito.<ApplicationStatus>any());
        applicationEntity.setId("42");
        applicationEntity.setOffer(offer);
        applicationEntity.setServiceName("Service Name");
        applicationEntity.setStatus(ApplicationStatus.DRAFT);

        ArrayList<ApplicationEntity> applications = new ArrayList<>();
        applications.add(applicationEntity);

        ApplicationBundleEntity applicationBundleEntity = new ApplicationBundleEntity();
        applicationBundleEntity.setApplications(applications);
        applicationBundleEntity.setId(UUID.randomUUID());
        Optional<ApplicationBundleEntity> ofResult = Optional.of(applicationBundleEntity);
        when(applicationBundleRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);
        assertThrows(IllegalArgumentException.class, () -> financingService.fetchOffers(UUID.randomUUID()));
        verify(applicationService).getServiceName();
        verify(applicationBundleRepository).findById(Mockito.<UUID>any());
        verify(applicationEntity).getStatus();
        verify(applicationEntity).getId();
        verify(applicationEntity).getServiceName();
        verify(applicationEntity).setId(Mockito.<String>any());
        verify(applicationEntity).setOffer(Mockito.<OfferEntity>any());
        verify(applicationEntity).setServiceName(Mockito.<String>any());
        verify(applicationEntity).setStatus(Mockito.<ApplicationStatus>any());
    }

    /**
     * Method under test: {@link FinancingService#fetchOffers(UUID)}
     */
    @Test
    void testFetchOffers9() {
        Optional<ApplicationBundleEntity> emptyResult = Optional.empty();
        when(applicationBundleRepository.findById(Mockito.<UUID>any())).thenReturn(emptyResult);
        assertThrows(IllegalStateException.class, () -> financingService.fetchOffers(UUID.randomUUID()));
        verify(applicationBundleRepository).findById(Mockito.<UUID>any());
    }

    /**
     * Method under test: {@link FinancingService#fetchOffers(UUID)}
     */
    @Test
    void testFetchOffers10() {
        BigDecimal monthlyPaymentAmount = BigDecimal.valueOf(1L);
        BigDecimal totalRepaymentAmount = BigDecimal.valueOf(1L);
        BigDecimal annualPercentageRate = BigDecimal.valueOf(1L);
        when(applicationService.fetchOffer(Mockito.<String>any())).thenReturn(new Application("Service Name", "42",
                ApplicationStatus.DRAFT,
                new Offer(monthlyPaymentAmount, totalRepaymentAmount, 10, annualPercentageRate, LocalDate.of(1970, 1, 1))));
        when(applicationService.getServiceName()).thenReturn("Service Name");

        OfferEntity offer = new OfferEntity();
        offer.setAnnualPercentageRate(BigDecimal.valueOf(1L));
        offer.setFirstRepaymentDate(LocalDate.of(1970, 1, 1));
        offer.setId(1L);
        offer.setMonthlyPaymentAmount(BigDecimal.valueOf(1L));
        offer.setNumberOfPayments(10);
        offer.setTotalRepaymentAmount(BigDecimal.valueOf(1L));
        ApplicationEntity applicationEntity = mock(ApplicationEntity.class);
        when(applicationEntity.getId()).thenReturn("42");
        when(applicationEntity.getServiceName()).thenReturn("Service Name");
        when(applicationEntity.getStatus()).thenReturn(ApplicationStatus.DRAFT);
        doNothing().when(applicationEntity).setId(Mockito.<String>any());
        doNothing().when(applicationEntity).setOffer(Mockito.<OfferEntity>any());
        doNothing().when(applicationEntity).setServiceName(Mockito.<String>any());
        doNothing().when(applicationEntity).setStatus(Mockito.<ApplicationStatus>any());
        applicationEntity.setId("42");
        applicationEntity.setOffer(offer);
        applicationEntity.setServiceName("Service Name");
        applicationEntity.setStatus(ApplicationStatus.DRAFT);

        ArrayList<ApplicationEntity> applications = new ArrayList<>();
        applications.add(applicationEntity);

        ApplicationBundleEntity applicationBundleEntity = new ApplicationBundleEntity();
        applicationBundleEntity.setApplications(applications);
        applicationBundleEntity.setId(UUID.randomUUID());
        Optional<ApplicationBundleEntity> ofResult = Optional.of(applicationBundleEntity);
        when(applicationBundleRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);
        UUID id = UUID.randomUUID();
        ApplicationResponse applicationResponse = new ApplicationResponse(id, new ArrayList<>());

        when(applicationMapper.map(Mockito.<ApplicationBundleEntity>any())).thenReturn(applicationResponse);

        OfferEntity offer2 = new OfferEntity();
        offer2.setAnnualPercentageRate(BigDecimal.valueOf(1L));
        offer2.setFirstRepaymentDate(LocalDate.of(1970, 1, 1));
        offer2.setId(1L);
        offer2.setMonthlyPaymentAmount(BigDecimal.valueOf(1L));
        offer2.setNumberOfPayments(10);
        offer2.setTotalRepaymentAmount(BigDecimal.valueOf(1L));

        OfferEntity offerEntity = new OfferEntity();
        offerEntity.setAnnualPercentageRate(BigDecimal.valueOf(1L));
        offerEntity.setFirstRepaymentDate(LocalDate.of(1970, 1, 1));
        offerEntity.setId(1L);
        offerEntity.setMonthlyPaymentAmount(BigDecimal.valueOf(1L));
        offerEntity.setNumberOfPayments(10);
        offerEntity.setTotalRepaymentAmount(BigDecimal.valueOf(1L));
        ApplicationEntity applicationEntity2 = mock(ApplicationEntity.class);
        when(applicationEntity2.getOffer()).thenReturn(offerEntity);
        when(applicationEntity2.getStatus()).thenReturn(ApplicationStatus.DRAFT);
        doNothing().when(applicationEntity2).setId(Mockito.<String>any());
        doNothing().when(applicationEntity2).setOffer(Mockito.<OfferEntity>any());
        doNothing().when(applicationEntity2).setServiceName(Mockito.<String>any());
        doNothing().when(applicationEntity2).setStatus(Mockito.<ApplicationStatus>any());
        applicationEntity2.setId("42");
        applicationEntity2.setOffer(offer2);
        applicationEntity2.setServiceName("Service Name");
        applicationEntity2.setStatus(ApplicationStatus.DRAFT);
        when(entityMapper.map(Mockito.<Application>any())).thenReturn(applicationEntity2);
        assertSame(applicationResponse, financingService.fetchOffers(UUID.randomUUID()));
        verify(applicationService).fetchOffer(Mockito.<String>any());
        verify(applicationService).getServiceName();
        verify(applicationBundleRepository).findById(Mockito.<UUID>any());
        verify(applicationEntity).getStatus();
        verify(applicationEntity).getId();
        verify(applicationEntity).getServiceName();
        verify(applicationEntity).setId(Mockito.<String>any());
        verify(applicationEntity, atLeast(1)).setOffer(Mockito.<OfferEntity>any());
        verify(applicationEntity).setServiceName(Mockito.<String>any());
        verify(applicationEntity, atLeast(1)).setStatus(Mockito.<ApplicationStatus>any());
        verify(applicationMapper).map(Mockito.<ApplicationBundleEntity>any());
        verify(entityMapper).map(Mockito.<Application>any());
        verify(applicationEntity2).getOffer();
        verify(applicationEntity2).getStatus();
        verify(applicationEntity2).setId(Mockito.<String>any());
        verify(applicationEntity2).setOffer(Mockito.<OfferEntity>any());
        verify(applicationEntity2).setServiceName(Mockito.<String>any());
        verify(applicationEntity2).setStatus(Mockito.<ApplicationStatus>any());
    }
}

