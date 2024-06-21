package com.example.memo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.example.memo.dto.AIResponse;
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
    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public MemoResponse createMemo(MemoRequest request) {
        // String aiUrl = "http://아직 ai파트 미완성";
        // List<String> tags = restTemplate.postForObject(aiUrl, request, List.class);
        Memo memo = request.toEntity(null);
        Memo savedMemo = memoRepository.save(memo);
        return MemoResponse.from(savedMemo);
    }

    public List<MemoResponse> searchMemo(MemoRequest request) {
        String aiUrl = "http://rapisim.mynetgear.com:8000/user_query?query=" + request.content();
        AIResponse aiResponse = restTemplate.getForObject(aiUrl, AIResponse.class);

        List<MemoResponse> memoResponses = new ArrayList<>();
        if (aiResponse.getType() == 1) { // ID 리스트로 메모를 검색
            for (String id : aiResponse.getContent()) {
                memoRepository.findById(id).ifPresent(memo -> memoResponses.add(MemoResponse.from(memo)));
            }
        } else if (aiResponse.getType() == 2) { // 정규식으로 메모를 검색
            String regex = aiResponse.getContent().get(0);
            List<Memo> memos = memoRepository.findByContentRegex(regex);
            for (Memo memo : memos) {
                memoResponses.add(MemoResponse.from(memo));
            }
        } else if (aiResponse.getType() == 3) { // 태그로 메모를 검색
            String tag = aiResponse.getContent().get(0);
            List<Memo> memos = memoRepository.findByTagsContaining(tag);
            for (Memo memo : memos) {
                memoResponses.add(MemoResponse.from(memo));
            }
        } else {
            Memo memo = new Memo("0", "메모를 찾지 못했습니다.", null);
            memoResponses.add(MemoResponse.from(memo));
        }
        return memoResponses;
    }
}
