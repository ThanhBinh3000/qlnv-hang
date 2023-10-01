package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKho;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkhotamgui.NhPhieuNhapKhoTamGui;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBcanKeHangHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HhPhieuNhapKhoHdrRepository  extends JpaRepository<HhPhieuNhapKhoHdr, Long> {

    @Query(value = "select * from HH_PHIEU_NHAP_KHO_HDR HDR where " +
            "(:namKh IS NULL OR HDR.NAM_KH = TO_NUMBER(:namKh)) " +
            "AND (:soQuyetDinhNhap IS NULL OR LOWER(HDR.SO_QUYET_DINH_NHAP) LIKE LOWER(CONCAT(CONCAT('%',:soQuyetDinhNhap),'%' ) ) )" +
            "AND (:soPhieuNhapKho IS NULL OR LOWER(HDR.SO_PHIEU_NHAP_KHO) LIKE LOWER(CONCAT(CONCAT('%',:soPhieuNhapKho),'%' ) ) )" +
            "AND (:ngayNhapKhoTu IS NULL OR HDR.NGAY_NKHO >=  TO_DATE(:ngayNhapKhoTu,'yyyy-MM-dd')) " +
            "AND (:ngayNhapKhoDen IS NULL OR HDR.NGAY_NKHO <= TO_DATE(:ngayNhapKhoDen,'yyyy-MM-dd'))" +
            "AND (:trangThai IS NULL OR HDR.TRANG_THAI = :trangThai)" +
            "AND (:maDvi IS NULL OR LOWER(HDR.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    Page<HhPhieuNhapKhoHdr> searchPage(Integer namKh, String soQuyetDinhNhap, String soPhieuNhapKho, String ngayNhapKhoTu, String ngayNhapKhoDen, String trangThai, String maDvi, Pageable pageable);

    List<HhPhieuNhapKhoHdr> findByIdIn(List<Long> id);
    @Transactional
    void deleteAllByIdIn(List<Long> ids);

    List<HhPhieuNhapKhoHdr> findAllByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh);

    List<HhPhieuNhapKhoHdr> findAllByIdQdGiaoNvNh(Long idQdGiaoNvNh);

    List<HhPhieuNhapKhoHdr> findAllBySoPhieuKtraCluong(String soPhieuKtraCluong);


}
