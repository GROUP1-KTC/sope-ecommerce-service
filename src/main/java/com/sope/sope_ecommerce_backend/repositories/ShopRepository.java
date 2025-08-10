package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.dto.response.ShopSearchResult;
import com.sope.sope_ecommerce_backend.entities.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShopRepository extends JpaRepository<Shop, UUID> {
    Optional<Shop> findByUser_Id(UUID id);

    @Query("""
       SELECT new com.sope.sope_ecommerce_backend.dto.response.ShopSearchResult(
           s.id,
           s.name,
           s.logoUrl
       )
       FROM Shop s
       WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))
       """)
    List<ShopSearchResult> searchShopsByName(@Param("name") String name);


}