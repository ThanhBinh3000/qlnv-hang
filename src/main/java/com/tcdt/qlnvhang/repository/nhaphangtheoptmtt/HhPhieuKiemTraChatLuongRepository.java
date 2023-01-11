package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuktracl.NhPhieuKtChatLuong;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuKiemTraChatLuong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface HhPhieuKiemTraChatLuongRepository extends JpaRepository<HhPhieuKiemTraChatLuong,Long> {

    @Query(value = "select * from HH_PHIEU_KT_CHAT_LUONG PKT where (:namKh IS NULL OR PKT.NAM_KH=TO_NUMBER(:namKh))" +
            " AND(:soQd IS NULL OR LOWER(PKT.SO_QD_GIAO_NV_NH) LIKE LOWER(CONCAT(CONCAT('%',:soQd),'%')))" +
            " AND(:soPhieu IS NULL OR LOWER(PKT.SO_PHIEU) LIKE LOWER(CONCAT(CONCAT('%',:soPhieu),'%')))" +
            " AND (:ngayLphieuTu IS NULL OR PKT.NGAY_TAO >=  TO_DATE(:ngayLphieuTu,'yyyy-MM-dd')) " +
            " AND (:ngayLphieuDen IS NULL OR PKT.NGAY_TAO <= TO_DATE(:ngayLphieuDen,'yyyy-MM-dd'))" +
            " AND (:ngayGdinhTu IS NULL OR PKT.NGAY_GDINH >=  TO_DATE(:ngayGdinhTu,'yyyy-MM-dd')) " +
            " AND (:ngayGdinhDen IS NULL OR PKT.NGAY_GDINH <= TO_DATE(:ngayGdinhDen,'yyyy-MM-dd'))" +
            " AND(:ketQua IS NULL OR LOWER(PKT.KQ_DANH_GIA) LIKE LOWER(CONCAT(CONCAT('%',:ketQua),'%')))" +
            " AND (:trangThai IS NULL OR PKT.TRANG_THAI = :trangThai) " +
            " AND (:maDvi IS NULL OR LOWER(PKT.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    Page<HhPhieuKiemTraChatLuong> searchPage(Integer namKh, String soQd, String soPhieu, String ngayLphieuTu,String ngayLphieuDen, String ngayGdinhTu, String ngayGdinhDen, String ketQua, String trangThai, String maDvi, Pageable pageable);

    Optional<HhPhieuKiemTraChatLuong> findAllBySoPhieu(String soPhieu);
    List<HhPhieuKiemTraChatLuong> findAllByIdIn(List<Long> ids);

    List<HhPhieuKiemTraChatLuong> findByIdDdiemGiaoNvNhOrderById(Long idDdiemGiaoNvNh);

    @Query(value = " SELECT NVL(SUM(KT.SO_LUONG_NHAP_KHO),0) as C FROM HH_PHIEU_KT_CHAT_LUONG KT " +
            " WHERE KT.ID_DDIEM_GIAO_NV_NH = :idDdiemNhap AND KT.TRANG_THAI = :trangThai",nativeQuery = true)
    BigDecimal soLuongNhapKho(Long idDdiemNhap, String trangThai);
}
