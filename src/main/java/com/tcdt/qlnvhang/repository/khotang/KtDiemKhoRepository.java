package com.tcdt.qlnvhang.repository.khotang;

import com.tcdt.qlnvhang.table.khotang.KtDiemKho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface KtDiemKhoRepository extends CrudRepository<KtDiemKho, Long> {
    final String qr = "SELECT * FROM KT_DIEM_KHO WHERE (:ma is null or lower(MA_DIEM_KHO) like lower(concat(concat('%', :ma),'%'))) AND (:ten is null or lower(TEN_DIEM_KHO) like lower(concat(concat('%', :ten),'%'))) and (:id is null or TONGKHO_ID = :id)";
    final String qrCount = "SELECT count(1) FROM KT_DIEM_KHO WHERE (:ma is null or lower(MA_DIEM_KHO) like lower(concat(concat('%', :ma),'%'))) AND (:ten is null or lower(TEN_DIEM_KHO) like lower(concat(concat('%', :ten),'%'))) and (:id is null or TONGKHO_ID = :id)";

    @Query(value = qr, countQuery = qrCount, nativeQuery = true)
    Page<KtDiemKho> selectParams(@Param("ma") String ma, @Param("ten") String ten, @Param("id") Long id, Pageable pageable);

    List<KtDiemKho> findByMaDiemkhoIn(Collection<String> mas);
}
