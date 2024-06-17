package com.example.memo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.memo.dto.MemoRequest;
import com.example.memo.dto.MemoResponse;
import com.example.memo.service.MemoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/memos")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @PostMapping
    public ResponseEntity<MemoResponse> createMemo(@RequestBody MemoRequest request) {
        MemoResponse memo = memoService.createMemo(request);
        return ResponseEntity.ok(memo);
    }

    @PostMapping("/search")
    public ResponseEntity<List<MemoResponse>> searchMemos(@RequestBody MemoRequest request) {
        List<MemoResponse> memos = memoService.searchMemo(request);
        return ResponseEntity.ok(memos);
    }
}
