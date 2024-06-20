package com.example.memo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.example.memo.model.Memo;

public interface MemoRepository extends Repository<Memo, String> {

    Memo save(Memo memo);

    Optional<Memo> findById(String id);

    List<Memo> findByContentRegex(String regex);

    List<Memo> findByTagsContaining(String tag);
}
