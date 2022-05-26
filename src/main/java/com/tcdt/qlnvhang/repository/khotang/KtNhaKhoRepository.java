package com.tcdt.qlnvhang.repository.khotang;

import com.tcdt.qlnvhang.table.khotang.KtNhaKho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface KtNhaKhoRepository extends CrudRepository<KtNhaKho, Long> {
    final String qr = "SELECT * FROM KT_NHA_KHO WHERE (:ma is null or lower(MA_NHA_KHO) like lower(concat(concat('%', :ma),'%'))) AND (:ten is null or lower(TEN_NHA_KHO) like lower(concat(concat('%', :ten),'%'))) and (:id is null or DIEMKHO_ID = :id)";
    final String qrCount = "SELECT count(1) FROM KT_NHA_KHO WHERE (:ma is null or lower(MA_NHA_KHO) like lower(concat(concat('%', :ma),'%'))) AND (:ten is null or lower(TEN_NHA_KHO) like lower(concat(concat('%', :ten),'%'))) and (:id is null or DIEMKHO_ID = :id)";

    @Query(value = qr, countQuery = qrCount, nativeQuery = true)
    Page<KtNhaKho> selectParams(@Param("ma") String ma, @Param("ten") String ten, @Param("id") Long id, Pageable pageable);
}
