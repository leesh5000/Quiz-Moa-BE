package com.leesh.quiz.api.userprofile.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PagingResponseDto(List<?> content,
                                   long totalElements, int totalPages,
                                   boolean last, boolean first,
                                   boolean empty) {

    public static PagingResponseDto from(Page<?> page) {
        return new PagingResponseDto(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast(),
                page.isFirst(),
                page.isEmpty()
        );
    }

}
