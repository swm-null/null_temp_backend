package com.example.memo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.memo.dto.MemoRequest;
import com.example.memo.dto.MemoResponse;
import com.example.memo.model.Memo;
import com.example.memo.repository.MemoRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;

    @Transactional
    public MemoResponse createMemo(MemoRequest request) {
        Memo memo = request.toEntity();
        Memo savedMemo = memoRepository.save(memo);

        return MemoResponse.from(savedMemo);
    }

    public List<MemoResponse> searchMemo(MemoRequest request) {
        List<MemoResponse> list = new ArrayList<>();
        list.add(MemoResponse.from(request.toEntity()));
        return list;
    }
}
