package com.tcdt.qlnvhang.repository.khotang;

import com.tcdt.qlnvhang.table.khotang.KtNganKho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface KtNganKhoRepositoryCustom {

    Page<KtNganKho> selectParams(@Param("ma") String ma, @Param("ten") String ten, @Param("id") Long id, Pageable pageable);
}
