package com.permatabank.insertbatchfile.repository;

import com.permatabank.insertbatchfile.entity.SwiftBank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SwiftBankRepository extends CrudRepository<SwiftBank,Long> {
}
