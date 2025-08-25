// package com.sope.sope_ecommerce_backend.mapper;

// import java.util.List;

// import org.mapstruct.Mapper;
// import org.mapstruct.Mapping;

// import com.sope.sope_ecommerce_backend.dto.request.ReviewCreateDTO;
// import com.sope.sope_ecommerce_backend.dto.response.ReviewDTO;
// import com.sope.sope_ecommerce_backend.entities.ReviewEntity;

// @Mapper(componentModel = "spring", uses = { ImageMapper.class,
// MultipartFileMapper.class })
// public interface ReviewMapper {
// // // @Mapping(source = "appUser.id", target = "user.id")
// // // @Mapping(source = "appUser.username", target = "user.username")
// // // @Mapping(source = "productVariant", target = "productVariant")
// // ReviewDTO toDto(ReviewEntity review); // GET

// // List<ReviewDTO> toDtoList(List<ReviewEntity> reviewEntities);

// // // @Mapping(target = "createdAt", ignore = true)
// // // @Mapping(target = "updatedAt", ignore = true)
// // // @Mapping(target = "imagesList", ignore = true) // File xử lý trong
// service
// // ReviewEntity toEntity(ReviewCreateDTO reviewCreateDTO); // POST
// }
