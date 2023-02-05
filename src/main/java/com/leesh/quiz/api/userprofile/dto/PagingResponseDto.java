package com.leesh.quiz.api.userprofile.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PagingResponseDto<T>(List<T> content,
                                   long totalElements, int totalPages,
                                   boolean last, boolean first,
                                   boolean empty) {

    public PagingResponseDto(Page<T> page) {
        this(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast(),
                page.isFirst(),
                page.isEmpty()
        );
    }
}
