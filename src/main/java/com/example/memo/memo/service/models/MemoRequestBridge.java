package com.example.memo.memo.service.models;

import com.example.memo.memo.models.MemoRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemoRequestBridge {

	private String content;
	private String ho;

	public MemoRequestBridge(String content) {

		this.content = content;
		this.ho = content;
	}

	public static MemoRequestBridge from(MemoRequest memoRequest) {
		return new MemoRequestBridge(memoRequest.content());
	}

	public static Memo toMemo(MemoRequestBridge memoRequestBridge) {
		return Memo.builder()
			.content(memoRequestBridge.getContent())
			.tags(null)
			.build();
	}
}
