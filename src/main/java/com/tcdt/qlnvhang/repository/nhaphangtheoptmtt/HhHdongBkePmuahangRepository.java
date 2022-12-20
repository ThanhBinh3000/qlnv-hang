package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhHdongBkePmuahangHdr;
import com.tcdt.qlnvhang.util.Contains;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface HhHdongBkePmuahangRepository extends JpaRepository<HhHdongBkePmuahangHdr,Long> {

    List<HhHdongBkePmuahangHdr> findByIdIn(List<Long> ids);
    Optional<HhHdongBkePmuahangHdr> findBySoHd(String soHd);

    @Transactional
    void deleteAllByIdIn(List<Long> ids);

    @Query(
            value = "SELECT * " +
                    "FROM HH_HDONG_BKE_PMUAHANG_HDR  HDR" +
                    " WHERE (:loaiVthh IS NULL OR HDR.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%')) " +
                    "  AND (:soHd IS NULL OR HDR.SO_HD LIKE CONCAT(CONCAT('%',:soHd),'%')) " +
                    "  AND (:tenHd IS NULL OR LOWER(HDR.TEN_HD) LIKE LOWER(CONCAT(CONCAT('%',:tenHd),'%'))) " +
                    "  AND (:nhaCcap IS NULL OR HDR.SO_HD = :nhaCcap) " +
                    "  AND (:namHd IS NULL OR HDR.NAM_HD = :namHd) " +
                    "  AND (:tuNgayKy IS NULL OR HDR.NGAY_KY >= TO_DATE(:tuNgayKy, 'yyyy-MM-dd')) " +
                    "  AND (:denNgayKy IS NULL OR HDR.NGAY_KY <= TO_DATE(:denNgayKy, 'yyyy-MM-dd')) " +
                    "  AND (:maDvi IS NULL OR HDR.MA_DVI = :maDvi) " +
                    "  AND (:trangThai IS NULL OR HDR.TRANG_THAI = :trangThai) ",
            nativeQuery = true)
    Page<HhHdongBkePmuahangHdr> selectAll(String loaiVthh, String soHd, String tenHd, String nhaCcap,  String namHd, String tuNgayKy, String denNgayKy,  String maDvi, String trangThai, Pageable pageable);



}
