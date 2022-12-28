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
            " AND (:loaiVthh IS NULL OR QD.LOAI_VTHH = :loaiVthh)" +
            " AND (:cloaiVthh IS NULL OR QD.CLOAI_VTHH = :cloaiVthh)" +
            " AND (:trichYeu IS NULL OR LOWER(QD.TRICH_YEU) LIKE(CONCAT(CONCAT('%',:trichYeu),'%')) )" +
            " AND (:ngayQdTu IS NULL OR QD.NGAY_QD >=  TO_DATE(:ngayQdTu,'yyyy-MM-dd')) " +
            " AND (:ngayQdDen IS NULL OR QD.NGAY_QD <= TO_DATE(:ngayQdDen,'yyyy-MM-dd'))" +
            " AND (:trangThai IS NULL OR QD.TRANG_THAI = :trangThai)" +
            " AND (:maDvi IS NULL OR LOWER(QD.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))",
    nativeQuery = true)
    Page<HhQdGiaoNvNhapHang> searchPageCuc(Integer namNhap, String soQd, String loaiVthh, String cloaiVthh, String trichYeu, String ngayQdTu, String ngayQdDen, String trangThai, String maDvi, Pageable pageable);

    @Query(value = "select * from HH_QD_GIAO_NV_NHAP_HANG QD " +
            " LEFT JOIN HH_QD_GIAO_NV_NHAP_HANG_DTL QD_DTL ON QD.ID = QD_DTL.ID_QD_HDR " +
            " where (:namNhap IS NULL OR QD.NAM_NHAP = TO_NUMBER(:namNhap))" +
            " AND (:soQd IS NULL OR LOWER(QD.SO_QD) LIKE LOWER(CONCAT(CONCAT('%',:soQd),'%')))" +
            " AND (:loaiVthh IS NULL OR QD.LOAI_VTHH = :loaiVthh)" +
            " AND (:cloaiVthh IS NULL OR QD.CLOAI_VTHH = :cloaiVthh)" +
            " AND (:trichYeu IS NULL OR LOWER(QD.TRICH_YEU) LIKE(CONCAT(CONCAT('%',:trichYeu),'%')) )" +
            " AND (:ngayQdTu IS NULL OR QD.NGAY_QD >=  TO_DATE(:ngayQdTu,'yyyy-MM-dd')) " +
            " AND (:ngayQdDen IS NULL OR QD.NGAY_QD <= TO_DATE(:ngayQdDen,'yyyy-MM-dd'))" +
            " AND (:trangThai IS NULL OR QD.TRANG_THAI = :trangThai)" +
            " AND (:maDvi IS NULL OR LOWER(QD_DTL.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))",
            nativeQuery = true)
    Page<HhQdGiaoNvNhapHang> searchPageChiCuc(Integer namNhap, String soQd, String loaiVthh, String cloaiVthh, String trichYeu, String ngayQdTu, String ngayQdDen, String trangThai, String maDvi, Pageable pageable);

    Optional<HhQdGiaoNvNhapHang> findAllBySoQd(String soQd);

    List<HhQdGiaoNvNhapHang> findAllByIdIn(List<Long> ids);

}
