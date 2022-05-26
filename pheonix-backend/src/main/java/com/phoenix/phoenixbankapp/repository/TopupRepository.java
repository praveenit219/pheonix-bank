package com.phoenix.phoenixbankapp.repository;

import com.phoenix.phoenixbankapp.domain.TopUp;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopupRepository extends CrudRepository<TopUp, Long> {
    TopUp findOneByUserId(String userId);
}

