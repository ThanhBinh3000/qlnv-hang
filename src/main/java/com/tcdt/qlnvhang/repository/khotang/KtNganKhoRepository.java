package com.tcdt.qlnvhang.repository.khotang;

import com.tcdt.qlnvhang.table.khotang.KtNganKho;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface KtNganKhoRepository extends CrudRepository<KtNganKho, Long>, KtNganKhoRepositoryCustom {
	List<KtNganKho> findByMaNgankhoIn(Collection<String> maNganKhoList);

	KtNganKho findByMaNgankho(String ma);

}