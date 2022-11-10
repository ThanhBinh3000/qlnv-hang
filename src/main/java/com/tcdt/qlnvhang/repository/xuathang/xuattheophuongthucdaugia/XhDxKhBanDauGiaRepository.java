package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttHdr;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.util.Contains;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhDxKhBanDauGiaRepository extends JpaRepository<XhDxKhBanDauGia,Long> {
    @Query(value = "select * from XH_DX_KH_BAN_DAU_GIA DX where (:namKh IS NULL OR DX.NAM_KH = TO_NUMBER(:namKh)) " +
            "AND (:soDxuat IS NULL OR LOWER(DX.SO_DXUAT) LIKE LOWER(CONCAT(CONCAT('%',:soDxuat),'%' ) ) )" +
            "AND (:ngayTaoTu IS NULL OR DX.NGAY_TAO >=  TO_DATE(:ngayTaoTu,'yyyy-MM-dd')) " +
            "AND (:ngayTaoDen IS NULL OR DX.NGAY_TAO <= TO_DATE(:ngayTaoDen,'yyyy-MM-dd'))" +
            "AND (:ngayDuyetTu IS NULL OR DX.NGAY_PDUYET >=  TO_DATE(:ngayDuyetTu,'yyyy-MM-dd')) " +
            "AND (:ngayDuyetDen IS NULL OR DX.NGAY_PDUYET <= TO_DATE(:ngayDuyetDen,'yyyy-MM-dd'))" +
            "AND (:trichYeu IS NULL OR LOWER(DX.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%',:trichYeu),'%' ) ) )" +
            "AND (:loaiVthh IS NULL OR DX.LOAI_VTHH = :loaiVthh) " +
            "AND (:trangThai IS NULL OR DX.TRANG_THAI = :trangThai)" +
            "AND (:trangThaiTh IS NULL OR DX.TRANG_THAI_TH = :trangThaiTh) " +
            "AND (:maDvi IS NULL OR LOWER(DX.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    Page<XhDxKhBanDauGia> searchPage(Integer namKh, String soDxuat, String ngayTaoTu, String ngayTaoDen, String ngayDuyetTu, String ngayDuyetDen, String trichYeu, String loaiVthh, String trangThai, String trangThaiTh, String maDvi, Pageable pageable);

    Optional<XhDxKhBanDauGia> findBySoDxuat(String soDxuat);

    List<XhDxKhBanDauGia> findAllByIdIn(List<Long> listId);

    XhDxKhBanDauGia findAllByLoaiVthhAndCloaiVthhAndNamKhAndMaDviAndTrangThaiNot(String loaiVthh, String cloaiVthh, Integer namKh, String maDvi, String trangThai);

    @Query(value = "select * from XH_DX_KH_BAN_DAU_GIA DX where (:namKh IS NULL OR DX.NAM_KH = TO_NUMBER(:namKh)) " +
            " AND (:loaiVthh IS NULL OR DX.LOAI_VTHH = :loaiVthh) " +
            " AND (:cloaiVthh IS NULL OR DX.CLOAI_VTHH = :cloaiVthh) " +
            " AND (:loaiHdong IS NULL OR DX.loai = :cloaiVthh) " +
            " AND (:ngayKyTu IS NULL OR DX.NGAY_PDUYET >=  TO_DATE(:ngayKyTu,'yyyy-MM-dd')) " +
            " AND (:ngayKyDen IS NULL OR DX.NGAY_PDUYET <= TO_DATE(:ngayKyDen,'yyyy-MM-dd'))" +
            " AND DX.TRANG_THAI = '"+ Contains.DADUYET_LDC+"'" +
            " AND DX.TRANG_THAI_TH = '"+ Contains.CHUATONGHOP+"'" +
            " AND DX.MA_THOP is null "+
            " AND DX.SO_QD_PDUYET is null "+
            " AND (:maDvi IS NULL OR LOWER(DX.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    List<XhDxKhBanDauGia> listTongHop(Integer namKh, String loaiVthh, String cloaiVthh,String loaiHdong,String ngayKyTu, String ngayKyDen, String maDvi);

    List<XhDxKhBanDauGia> findBySoDxuatIn (List<String> list);

    XhDxKhBanDauGia findAllById(Long idDxuat);

    List<XhDxKhBanDauGia> findByIdIn(List<Long> idDxList);
}
