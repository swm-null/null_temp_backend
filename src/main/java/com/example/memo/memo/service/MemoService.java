package com.example.memo.memo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.example.memo.memo.service.models.AIResponse;
import com.example.memo.memo.service.models.Memo;
import com.example.memo.memo.service.models.MemoRequestBridge;
import com.example.memo.memo.service.models.MemoResponseBridge;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemoService {

	private final MemoRepository memoRepository;
	private final RestTemplate restTemplate = new RestTemplate();

	@Transactional
	public MemoResponseBridge createMemo(MemoRequestBridge memoRequestBridge) {
		// String aiUrl = "http://아직 ai파트 미완성";
		// List<String> tags = restTemplate.postForObject(aiUrl, request, List.class);
		Memo memo = MemoRequestBridge.toMemo(memoRequestBridge);
		Memo savedMemo = memoRepository.save(memo);
		return MemoResponseBridge.from(savedMemo);
	}

	public List<MemoResponseBridge> searchMemo(MemoRequestBridge memoRequestBridge) {
		String aiUrl = "http://rapisim.mynetgear.com:8000/user_query?query=" + memoRequestBridge.getContent();

		// todo move into RestTemplate
		ResponseEntity<AIResponse> resp = restTemplate.getForEntity(aiUrl, AIResponse.class);

		if (resp.getStatusCode().is4xxClientError()) {

		}

		AIResponse aiResponse = resp.getBody();

		List<MemoResponseBridge> memoResponseBridgeList = new ArrayList<>();

		// todo modern switch
		if (aiResponse.getType() == 1) { // ID 리스트로 메모를 검색
			for (String id : aiResponse.getContent()) {
				getMemo(id).ifPresent(memo -> memoResponseBridgeList.add(MemoResponseBridge.from(memo)));
			}
		} else if (aiResponse.getType() == 2) { // 정규식으로 메모를 검색
			String regex = aiResponse.getContent().get(0);
			// todo 개별 method 로 분리
			List<Memo> memos = memoRepository.findByContentRegex(regex);
			for (Memo memo : memos) {
				memoResponseBridgeList.add(MemoResponseBridge.from(memo));
			}
		} else if (aiResponse.getType() == 3) { // 태그로 메모를 검색
			String tag = aiResponse.getContent().get(0);
			List<Memo> memos = memoRepository.findByTagsContaining(tag);
			for (Memo memo : memos) {
				memoResponseBridgeList.add(MemoResponseBridge.from(memo));
			}
		} else {
			Memo memo = new Memo("0", "메모를 찾지 못했습니다.", null);
			memoResponseBridgeList.add(MemoResponseBridge.from(memo));
		}
		return memoResponseBridgeList;
	}

	Optional<Memo> getMemo(String id) {
		// todo 없을 경우 예외 처리
		return memoRepository.findById(id);
	}

}
