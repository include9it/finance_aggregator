package io.include9it.finance_aggregator.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface ApplicationBundleRepository extends JpaRepository<ApplicationBundleEntity, UUID> {
}
