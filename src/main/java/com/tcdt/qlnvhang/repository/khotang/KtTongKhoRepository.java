package com.tcdt.qlnvhang.repository.khotang;

import com.tcdt.qlnvhang.table.khotang.KtTongKho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface KtTongKhoRepository extends CrudRepository<KtTongKho, Long> {
    final String qr = "SELECT * FROM KT_TONG_KHO WHERE (:ma is null or lower(MA_TONG_KHO) like lower(concat(concat('%', :ma),'%'))) AND (:ten is null or lower(TEN_TONG_KHO) like lower(concat(concat('%', :ten),'%'))) and (:id is null or DTQGKV_ID = :id)";
    final String qrCount = "SELECT count(1) FROM KT_TONG_KHO WHERE (:ma is null or lower(MA_TONG_KHO) like lower(concat(concat('%', :ma),'%'))) AND (:ten is null or lower(TEN_TONG_KHO) like lower(concat(concat('%', :ten),'%'))) and (:id is null or DTQGKV_ID = :id)";

    @Query(value = qr, countQuery = qrCount, nativeQuery = true)
    Page<KtTongKho> selectParams(@Param("ma") String ma, @Param("ten") String ten, @Param("id") Long id, Pageable pageable);

    Optional<KtTongKho> findByMaTongKho(String maTongKho);
}
