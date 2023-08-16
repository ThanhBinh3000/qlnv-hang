package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdGiaoNvNhapHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HhQdGiaoNvNhapHangRepository extends JpaRepository<HhQdGiaoNvNhapHang,Long> {

    @Query(value = "select * from HH_QD_GIAO_NV_NHAP_HANG QD where (:namNhap IS NULL OR QD.NAM_NHAP = TO_NUMBER(:namNhap))" +
            " AND (:soQd IS NULL OR LOWER(QD.SO_QD) LIKE LOWER(CONCAT(CONCAT('%',:soQd),'%')))" +
            " AND (:loaiQd IS NULL OR QD.LOAI_QD = :loaiQd)" +
            " AND (:loaiVthh IS NULL OR QD.LOAI_VTHH = :loaiVthh)" +
            " AND (:cloaiVthh IS NULL OR QD.CLOAI_VTHH = :cloaiVthh)" +
            " AND (:trichYeu IS NULL OR LOWER(QD.TRICH_YEU) LIKE(CONCAT(CONCAT('%',:trichYeu),'%')) )" +
            " AND (:ngayQdTu IS NULL OR QD.NGAY_QD >=  TO_DATE(:ngayQdTu,'yyyy-MM-dd')) " +
            " AND (:ngayQdDen IS NULL OR QD.NGAY_QD <= TO_DATE(:ngayQdDen,'yyyy-MM-dd'))" +
            " AND (:trangThai IS NULL OR QD.TRANG_THAI = :trangThai)" +
            " AND (:maDvi IS NULL OR LOWER(QD.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))",
    nativeQuery = true)
    Page<HhQdGiaoNvNhapHang> searchPageCuc(Integer namNhap, String soQd, String loaiVthh, String cloaiVthh, String trichYeu, String ngayQdTu, String ngayQdDen, String trangThai, String maDvi, String loaiQd, Pageable pageable);

    @Query(value = "select DISTINCT QD.* from HH_QD_GIAO_NV_NHAP_HANG QD, HH_QD_GIAO_NV_NHAP_HANG_DTL QD_DTL, HH_HD_MTT_HDR HD " +
            " where QD.ID = QD_DTL.ID_QD_HDR" +
            " AND HD.ID_QD_GIAO_NV_NH = QD.ID" +
            " AND (:namNhap IS NULL OR QD.NAM_NHAP = TO_NUMBER(:namNhap))" +
            " AND (:soQd IS NULL OR LOWER(QD.SO_QD) LIKE LOWER(CONCAT(CONCAT('%',:soQd),'%')))" +
            " AND (:loaiVthh IS NULL OR QD.LOAI_VTHH = :loaiVthh)" +
            " AND (:loaiQd IS NULL OR QD.LOAI_QD = :loaiQd)" +
            " AND (:cloaiVthh IS NULL OR QD.CLOAI_VTHH = :cloaiVthh)" +
            " AND (:trichYeu IS NULL OR LOWER(QD.TRICH_YEU) LIKE(CONCAT(CONCAT('%',:trichYeu),'%')) )" +
            " AND (:ngayQdTu IS NULL OR QD.NGAY_QD >=  TO_DATE(:ngayQdTu,'yyyy-MM-dd')) " +
            " AND (:ngayQdDen IS NULL OR QD.NGAY_QD <= TO_DATE(:ngayQdDen,'yyyy-MM-dd'))" +
            " AND (:tuNgayKy IS NULL OR HD.NGAY_PDUYET >=  TO_DATE(:tuNgayKy,'YYYY-MM-DD HH24:MI:SS')) " +
            " AND (:denNgayKy IS NULL OR HD.NGAY_PDUYET <= TO_DATE(:denNgayKy,'YYYY-MM-DD HH24:MI:SS'))" +
            " AND (:trangThai IS NULL OR QD.TRANG_THAI = :trangThai)" +
            " AND (:maDvi IS NULL OR LOWER(QD_DTL.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))",
//            " AND (:maDvi IS NULL OR QD_DTL.MA_DVI = :maDvi)" ,
            nativeQuery = true)
    Page<HhQdGiaoNvNhapHang> searchPageChiCuc(Integer namNhap, String soQd, String loaiVthh, String cloaiVthh, String trichYeu, String ngayQdTu, String ngayQdDen, String tuNgayKy, String denNgayKy, String trangThai, String maDvi, String loaiQd, Pageable pageable);

    Optional<HhQdGiaoNvNhapHang> findAllBySoQd(String soQd);

    List<HhQdGiaoNvNhapHang> findAllByIdIn(List<Long> ids);

}
