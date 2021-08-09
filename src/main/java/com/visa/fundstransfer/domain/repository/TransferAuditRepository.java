package com.visa.fundstransfer.domain.repository;

import com.visa.fundstransfer.domain.model.TransferTrack;

import org.springframework.data.repository.CrudRepository;

public interface TransferAuditRepository extends CrudRepository<TransferTrack, Long> {
}
