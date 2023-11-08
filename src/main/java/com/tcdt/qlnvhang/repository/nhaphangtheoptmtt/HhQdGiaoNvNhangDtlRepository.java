package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdGiaoNvNhangDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HhQdGiaoNvNhangDtlRepository extends JpaRepository<HhQdGiaoNvNhangDtl,Long> {
    List<HhQdGiaoNvNhangDtl> findAllByIdQdHdr(Long ids);
    HhQdGiaoNvNhangDtl findByIdQdHdrAndMaDvi(Long ids, String maDvi);
    List<HhQdGiaoNvNhangDtl> findAllByIdQdHdrIn(List<Long> ids);

    @Transactional()
    @Modifying
    @Query(value = "UPDATE HH_QD_GIAO_NV_NHAP_HANG_DTL SET TRANG_THAI =:trangThai WHERE ID = :id", nativeQuery = true)
    void updateTrangThai(Long id, String trangThai);

}
