package com.example.memo.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.util.List;

import com.example.memo.model.Memo;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemoResponse(
    @Schema(description = "메모 고유 ID", example = "1", requiredMode = REQUIRED)
    String id,
    @Schema(description = "내용", example = "text", requiredMode = REQUIRED)
    String content,
    @Schema(description = "태그", example = "text", requiredMode = REQUIRED)
    List<String> tag
) {
    public static MemoResponse from(Memo memo) {
        return new MemoResponse(
            memo.getId(),
            memo.getContent(),
            memo.getTag()
        );
    }
}
