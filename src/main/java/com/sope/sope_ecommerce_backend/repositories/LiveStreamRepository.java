package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.LiveStream;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LiveStreamRepository extends JpaRepository<LiveStream, Long> {
    List<LiveStream> findByIsLiveTrue();

    Optional<LiveStream> findByShop_Id(UUID shopId);
}
