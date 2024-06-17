package com.example.memo.repository;

import org.springframework.data.repository.Repository;

import com.example.memo.model.Memo;

public interface MemoRepository extends Repository<Memo, String> {

    Memo save(Memo memo);
}
