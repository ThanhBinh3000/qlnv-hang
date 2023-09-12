package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.entities.KtTrangThaiHienThoi;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KtTrangThaiHienThoiRepository extends CrudRepository<KtTrangThaiHienThoi, Long> {
    @Query(value = "SELECT * from KT_TRANGTHAI_HIENTHOI where MA_DON_VI = ?1 AND CLOAI_VTHH = ?2", nativeQuery = true)
    List<KtTrangThaiHienThoi> getHangTrongKho(String maLoKho, String maChungLoaiHang);
}
