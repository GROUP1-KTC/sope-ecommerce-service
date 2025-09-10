package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.response.MessageResponse;
import com.sope.sope_ecommerce_backend.entities.Message;
import com.sope.sope_ecommerce_backend.entities.message_entity.FileMessage;
import com.sope.sope_ecommerce_backend.entities.message_entity.ImageMessage;
import com.sope.sope_ecommerce_backend.entities.message_entity.TextMessage;
import org.mapstruct.*;

import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Named("formatDateTime")
    default String formatDateTime(java.time.LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }
    @Mapping(target = "senderId", source = "sender")
    @Mapping(target = "type", expression = "java(getType(message))")
    @Mapping(target = "sentAt", source = "sentAt", qualifiedByName = "formatDateTime")
    @Mapping(target = "content", expression = "java(getContent(message))")
    @Mapping(target = "imageUrl", expression = "java(getImageUrl(message))")
    @Mapping(target = "width", expression = "java(getWidth(message))")
    @Mapping(target = "height", expression = "java(getHeight(message))")
    @Mapping(target = "fileUrl", expression = "java(getFileUrl(message))")
    @Mapping(target = "fileName", expression = "java(getFileName(message))")
    @Mapping(target = "fileType", expression = "java(getFileType(message))")
    @Mapping(target = "fileSize", expression = "java(getFileSize(message))")
    MessageResponse toDto(Message message);

    // Helper methods để lấy trường theo từng subclass
    default String getType(Message message) {
        if (message instanceof TextMessage) return "text";
        if (message instanceof ImageMessage) return "image";
        if (message instanceof FileMessage) return "file";
        return null;
    }

    default String getContent(Message message) {
        if (message instanceof TextMessage tm) return tm.getContent();
        return null;
    }

    default String getImageUrl(Message message) {
        if (message instanceof ImageMessage im) return im.getImageUrl();
        return null;
    }

    default Integer getWidth(Message message) {
        if (message instanceof ImageMessage im) return im.getWidth();
        return null;
    }

    default Integer getHeight(Message message) {
        if (message instanceof ImageMessage im) return im.getHeight();
        return null;
    }

    default String getFileUrl(Message message) {
        if (message instanceof FileMessage fm) return fm.getFileUrl();
        return null;
    }

    default String getFileName(Message message) {
        if (message instanceof FileMessage fm) return fm.getFileName();
        return null;
    }

    default String getFileType(Message message) {
        if (message instanceof FileMessage fm) return fm.getFileType();
        return null;
    }

    default Long getFileSize(Message message) {
        if (message instanceof FileMessage fm) return fm.getFileSize();
        return null;
    }
}
