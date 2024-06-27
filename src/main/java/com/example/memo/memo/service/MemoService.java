package com.example.memo.memo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.memo.memo.service.models.AiResponse;
import com.example.memo.memo.service.models.Memo;
import com.example.memo.memo.service.models.MemoRequestBridge;
import com.example.memo.memo.service.models.MemoResponseBridge;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final RestTemplateService restTemplateService;

    @Transactional
    public MemoResponseBridge createMemo(MemoRequestBridge memoRequestBridge) {

        Memo memo = MemoRequestBridge.toMemo(memoRequestBridge);
        Memo savedMemo = memoRepository.save(memo);

        // ai 저장 기능 미완성
        // List<String> tags = restTemplateService.getTags(savedMemo.getId(), savedMemo.getContent());
        // memo.updateTags(tags);

        return MemoResponseBridge.from(savedMemo);
    }

    public List<MemoResponseBridge> searchMemo(MemoRequestBridge memoRequestBridge) {
        AiResponse aiResponse = restTemplateService.searchMemo(memoRequestBridge.getContent());
        List<MemoResponseBridge> memoResponseBridgeList = new ArrayList<>();
        switch (aiResponse.getType()) {
            case 1 -> searchMemoByIdList(aiResponse.getContent(), memoResponseBridgeList);
            case 2 -> searchMemoByRegex(aiResponse.getContent().get(0), memoResponseBridgeList);
            case 3 -> searchMemoByTag(aiResponse.getContent().get(0), memoResponseBridgeList);
            default -> throw new RuntimeException("메모를 찾지 못했습니다.");
        }
        return memoResponseBridgeList;
    }

    private void searchMemoByIdList(List<String> ids, List<MemoResponseBridge> memoResponseBridgeList) {
        ids.stream()
            .map(memoRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(MemoResponseBridge::from)
            .forEach(memoResponseBridgeList::add);
    }

    private void searchMemoByRegex(String regex, List<MemoResponseBridge> memoResponseBridgeList) {
        memoRepository.findByContentRegex(regex).stream()
            .map(MemoResponseBridge::from)
            .forEach(memoResponseBridgeList::add);
    }

    private void searchMemoByTag(String tag, List<MemoResponseBridge> memoResponseBridgeList) {
        memoRepository.findByTagsContaining(tag).stream()
            .map(MemoResponseBridge::from)
            .forEach(memoResponseBridgeList::add);
    }
}
