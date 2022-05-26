package com.tcdt.qlnvhang.repository.khotang;

import com.tcdt.qlnvhang.table.khotang.KtNganKho;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KtNganKhoRepository extends CrudRepository<KtNganKho, Long>, KtNganKhoRepositoryCustom {

}