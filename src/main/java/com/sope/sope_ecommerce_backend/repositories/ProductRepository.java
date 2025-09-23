package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.Product;
import com.sope.sope_ecommerce_backend.enums.StatusProduct;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
	Optional<Product> findBySlug(String slug);

	Optional<Product> findByStatus(StatusProduct status);

	Page<Product> findByCategoryIdIn(Collection<UUID> categoryIds, Pageable pageable);

	Page<Product> findByShop_Id(UUID shopId, Pageable pageable);

	Page<Product> findByStatusAndHidden(StatusProduct status, boolean hidden, Pageable pageable);

	Page<Product> findByShopIdAndStatusAndHidden(
			UUID shopId,
			StatusProduct status,
			boolean hidden,
			Pageable pageable);

	Optional<Product> findByName(String name);

	@Query(value = """
			SELECT * FROM products
			ORDER BY (embedding <=> cast(:queryVector as vector))
			LIMIT :limit
			""", nativeQuery = true)
	List<Product> findMostSimilar(@Param("queryVector") float[] queryVector, @Param("limit") int limit);

	@Query(value = """
			SELECT * FROM products
			WHERE LOWER(name) LIKE ANY(:patterns)
			   OR LOWER(description) LIKE ANY(:patterns)
			LIMIT :limit
			""", nativeQuery = true)
	List<Product> searchByKeywords(@Param("patterns") String[] patterns, @Param("limit") int limit);

	@Query(value = """
			    SELECT p.* FROM products p
			    JOIN product_suggested_for_user psu ON p.product_id = psu.product_id
			    WHERE psu.user_id = :userId
			""", nativeQuery = true)
	List<Product> findSuggestedProductsByUserId(@Param("userId") UUID userId);

	// ===== Lấy 10 sản phẩm ngẫu nhiên (bỏ qua danh sách đã có) =====
	@Query(value = """
			    SELECT * FROM products
			    WHERE product_id NOT IN :excludeIds
			    ORDER BY RANDOM()
			    LIMIT :limit
			""", nativeQuery = true)
	List<Product> findRandomProductsExcluding(@Param("excludeIds") List<UUID> excludeIds, @Param("limit") int limit);

	@Query(value = "SELECT * FROM products ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
	List<Product> findRandomProducts(@Param("limit") int limit);


}