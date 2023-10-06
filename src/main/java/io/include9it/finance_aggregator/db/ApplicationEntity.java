package io.include9it.finance_aggregator.db;

import io.include9it.finance_aggregator.dto.ApplicationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "application")
public class ApplicationEntity {

    @Id
    @NotNull
    @Column(nullable = false)
    private String id;

    @NotNull
    @Column(nullable = false)
    private ApplicationStatus status;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "offer_id")
    private OfferEntity offer;

    @NotNull
    @Column(nullable = false)
    private String serviceName;
}
