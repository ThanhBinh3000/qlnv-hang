package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBcanKeHangHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBienBanDayKhoHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface HhBienBanDayKhoHdrRepository extends JpaRepository <HhBienBanDayKhoHdr, Long> {

    @Query(value = "select * from HH_BIEN_BAN_DAY_KHO_HDR HDR" +
            " where (:namKh IS NULL OR HDR.NAM_KH = TO_NUMBER(:namKh))" +
            " AND (:soQuyetDinhNhap IS NULL OR LOWER(HDR.SO_QUYET_DINH_NHAP) LIKE LOWER(CONCAT(CONCAT('%',:soQuyetDinhNhap),'%')))" +
            " AND (:soBbNhapDayKho IS NULL OR LOWER(HDR.SO_BB_NHAP_DAY_KHO) LIKE LOWER(CONCAT(CONCAT('%',:soBbNhapDayKho),'%')))" +
            " AND (:ngayBdauNhapTu IS NULL OR HDR.NGAY_BDAU_NHAP >=  TO_DATE(:ngayBdauNhapTu,'yyyy-MM-dd')) " +
            " AND (:ngayBdauNhapDen IS NULL OR HDR.NGAY_BDAU_NHAP <= TO_DATE(:ngayBdauNhapDen,'yyyy-MM-dd'))" +
            " AND (:ngayKthucNhapTu IS NULL OR HDR.NGAY_KTHUC_NHAP >=  TO_DATE(:ngayKthucNhapTu,'yyyy-MM-dd')) " +
            " AND (:ngayKthucNhapDen IS NULL OR HDR.NGAY_KTHUC_NHAP <= TO_DATE(:ngayKthucNhapDen,'yyyy-MM-dd'))" +
            " AND (:ngayLapBbanTu IS NULL OR HDR.NGAY_LAP_BBAN >=  TO_DATE(:ngayLapBbanTu,'yyyy-MM-dd')) " +
            " AND (:ngayLapBbanDen IS NULL OR HDR.NGAY_LAP_BBAN <= TO_DATE(:ngayLapBbanDen,'yyyy-MM-dd'))" +
            " AND (:trangThai IS NULL OR HDR.TRANG_THAI = :trangThai)" +
            " AND (:maDvi IS NULL OR LOWER(HDR.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    Page<HhBienBanDayKhoHdr> searchPage(Integer namKh, String soQuyetDinhNhap, String soBbNhapDayKho, String ngayBdauNhapTu, String ngayBdauNhapDen, String ngayKthucNhapTu , String ngayKthucNhapDen, String ngayLapBbanTu, String ngayLapBbanDen , String trangThai, String maDvi, Pageable pageable);

    List<HhBienBanDayKhoHdr> findByIdIn(List<Long> id);

    @Transactional
    void deleteAllByIdIn(List<Long> ids);

    List<HhBienBanDayKhoHdr> findAllByIdQdGiaoNvNh(Long idQdGiaoNvNh);
    List<HhBienBanDayKhoHdr> findAllByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh);

    Optional<HhBienBanDayKhoHdr> findByMaLoKho(String maLoKho);


}
