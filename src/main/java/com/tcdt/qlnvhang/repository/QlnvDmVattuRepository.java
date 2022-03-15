package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface QlnvDmVattuRepository extends CrudRepository<QlnvDmVattu, Long> {
	Set<QlnvDmVattu> findByMaIn(Collection<String> maVatTus);
	QlnvDmVattu findByMa(String maVatTu);
}
