package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKho;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkhotamgui.NhPhieuNhapKhoTamGui;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBcanKeHangHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuKngiemCluong;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HhBcanKeHangHdrRepository extends  JpaRepository<HhBcanKeHangHdr, Long> {

    @Query(value = "select * from HH_BCAN_KE_HANG_HDR HDR" +
            " where (:namKh IS NULL OR HDR.NAM_KH = TO_NUMBER(:namKh))" +
            " AND (:soQuyetDinhNhap IS NULL OR LOWER(HDR.SO_QUYET_DINH_NHAP) LIKE LOWER(CONCAT(CONCAT('%',:soQuyetDinhNhap),'%')))" +
            " AND (:soBangKeCanHang IS NULL OR LOWER(HDR.SO_BANG_KE_CAN_HANG) LIKE LOWER(CONCAT(CONCAT('%',:soBangKeCanHang),'%')))" +
            " AND (:ngayNhapKhoTu IS NULL OR HDR.NGAY_NKHO >=  TO_DATE(:ngayNhapKhoTu,'yyyy-MM-dd')) " +
            " AND (:ngayNhapKhoDen IS NULL OR HDR.NGAY_NKHO <= TO_DATE(:ngayNhapKhoDen,'yyyy-MM-dd'))" +
            " AND (:trangThai IS NULL OR HDR.TRANG_THAI = :trangThai)" +
            " AND (:maDvi IS NULL OR LOWER(HDR.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    Page<HhBcanKeHangHdr> searchPage(Integer namKh, String soQuyetDinhNhap, String soBangKeCanHang,  String ngayNhapKhoTu, String ngayNhapKhoDen, String trangThai, String maDvi, Pageable pageable);

    List<HhBcanKeHangHdr> findByIdIn(List<Long> id);

    @Transactional
    void deleteAllByIdIn(List<Long> ids);

    List<HhBcanKeHangHdr> findAllByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh);


    HhBcanKeHangHdr findBySoPhieuNhapKho(String soPhieuNhapKho);

    List<HhBcanKeHangHdr> findAllByIdQdGiaoNvNh(Long idQdGiaoNvNh);

    List<HhBcanKeHangHdr> findAllBySoPhieuKtraCluong(String soPhieuKtraCluong);

}
