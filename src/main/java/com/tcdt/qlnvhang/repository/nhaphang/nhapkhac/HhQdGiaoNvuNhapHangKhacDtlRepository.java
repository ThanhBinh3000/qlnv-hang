package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNhapxuatDtl;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HhQdGiaoNvuNhapHangKhacDtlRepository extends BaseRepository<NhQdGiaoNvuNhapxuatDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);
    List<NhQdGiaoNvuNhapxuatDtl> findAllByIdHdr(Long idHdr);

    @Transactional()
    @Modifying
    @Query(value = "UPDATE NH_QD_GIAO_NVU_NHAPXUAT_CT SET TRANG_THAI =:trangThai WHERE ID = :id", nativeQuery = true)
    void updateTrangThai(Long id, String trangThai);
}
