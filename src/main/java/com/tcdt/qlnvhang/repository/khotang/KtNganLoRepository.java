package com.tcdt.qlnvhang.repository.khotang;

import com.tcdt.qlnvhang.table.khotang.KtNganLo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KtNganLoRepository extends CrudRepository<KtNganLo, Long>, KtNganLoRepositoryCustom {

    KtNganLo findFirstByMaNganlo(String maNganLo);
}
