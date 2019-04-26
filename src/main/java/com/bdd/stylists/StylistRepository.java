package com.bdd.stylists;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface StylistRepository extends JpaRepository<Stylist, Long> {
    List<Stylist> findStylistsByIdNotIn(Set<Long> stylists);
}
