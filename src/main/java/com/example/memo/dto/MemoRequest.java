package com.example.memo.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.util.List;

import com.example.memo.model.Memo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record MemoRequest(
    @Schema(description = "내용", example = "text", requiredMode = REQUIRED)
    @NotBlank(message = "내용은 비워둘 수 없습니다.")
    String content

) {
    public Memo toEntity(List<String> tags) {
        return Memo.builder()
            .content(content)
            .tags(tags)
            .build();
    }
}
