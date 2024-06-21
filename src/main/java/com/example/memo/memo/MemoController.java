package com.example.memo.memo;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.memo.memo.models.MemoRequest;
import com.example.memo.memo.models.MemoResponse;
import com.example.memo.memo.service.MemoService;
import com.example.memo.memo.service.models.MemoRequestBridge;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/memos")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @PostMapping
    public ResponseEntity<MemoResponse> createMemo(@RequestBody MemoRequest request) {
        MemoResponse memo = memoService.createMemo(MemoRequestBridge.from(request));
        return ResponseEntity.ok(memo);
    }

    @PostMapping("/search")
    public ResponseEntity<List<MemoResponse>> searchMemos(@RequestBody MemoRequest request) {
        List<MemoResponse> memos = memoService.searchMemo(MemoRequestBridge.from(request));
        return ResponseEntity.ok(memos);
    }
}
