package io.include9it.finance_aggregator.db;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "application_bundle")
public class ApplicationBundleEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "application_bundle_id")
    private List<ApplicationEntity> applications;
}
