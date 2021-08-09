package com.visa.fundstransfer.domain.repository;

import com.visa.fundstransfer.domain.model.Account;

import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Integer> {

    public Account findByNumber(Integer intAccountNumber);
}
