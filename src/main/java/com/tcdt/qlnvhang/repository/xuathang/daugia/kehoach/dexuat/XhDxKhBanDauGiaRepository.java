package com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat;

import com.tcdt.qlnvhang.request.xuathang.daugia.XhThopChiTieuReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.dexuat.XhDxKhBanDauGiaReq;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.util.Contains;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface XhDxKhBanDauGiaRepository extends JpaRepository<XhDxKhBanDauGia, Long> {

    @Query("SELECT DX FROM XhDxKhBanDauGia DX " +
            " WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR DX.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.namKh} IS NULL OR DX.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soDxuat} IS NULL OR LOWER(DX.soDxuat) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soDxuat}),'%' ) ) )" +
            "AND (:#{#param.ngayTaoTu} IS NULL OR DX.ngayTao >= :#{#param.ngayTaoTu}) " +
            "AND (:#{#param.ngayTaoDen} IS NULL OR DX.ngayTao <= :#{#param.ngayTaoDen}) " +
            "AND (:#{#param.ngayDuyetTu} IS NULL OR DX.ngayPduyet >= :#{#param.ngayDuyetTu}) " +
            "AND (:#{#param.ngayDuyetDen} IS NULL OR DX.ngayPduyet <= :#{#param.ngayDuyetDen}) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(DX.trichYeu) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.trichYeu}),'%'))) " +
            "AND (:#{#param.loaiVthh} IS NULL OR DX.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR DX.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.trangThaiTh} IS NULL OR DX.trangThaiTh = :#{#param.trangThaiTh}) " +
            "AND (:#{#param.trangThaiList == null || #param.trangThaiList.isEmpty() } = true OR DX.trangThai IN :#{#param.trangThaiList}) " +
            "ORDER BY DX.ngaySua desc , DX.ngayTao desc, DX.id desc")
    Page<XhDxKhBanDauGia> searchPage(@feign.Param("param") XhDxKhBanDauGiaReq param, Pageable pageable);

    @Query("SELECT TH from XhDxKhBanDauGia TH WHERE 1 = 1 " +
            "AND (:#{#param.namKh} IS NULL OR TH.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR TH.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.cloaiVthh} IS NULL OR TH.cloaiVthh LIKE CONCAT(:#{#param.cloaiVthh},'%'))  " +
            "AND (:#{#param.ngayDuyetTu} IS NULL OR TH.ngayPduyet >= :#{#param.ngayDuyetTu}) " +
            "AND (:#{#param.ngayDuyetDen} IS NULL OR TH.ngayPduyet <= :#{#param.ngayDuyetDen}) " +
            "AND TH.idThop IS NULL " +
            "AND TH.soQdPd IS NULL " +
            "AND TH.trangThai ='" + Contains.DA_DUYET_CBV + "'" +
            "AND TH.trangThaiTh ='" + Contains.CHUATONGHOP + "'")
    List<XhDxKhBanDauGia> listTongHop(@Param("param") XhThopChiTieuReq param);

    @Query(value = " SELECT NVL(SUM(DSG.SO_LUONG_CHI_CUC),0) FROM XH_QD_PD_KH_BDG HDR " +
            " INNER JOIN XH_QD_PD_KH_BDG_DTL DTL on HDR.ID = DTL.ID_QD_HDR " +
            " LEFT JOIN XH_QD_PD_KH_BDG_PL DSG ON DSG.ID_QD_DTL = DTL.ID " +
            "WHERE HDR.NAM = :namKh AND HDR.LOAI_VTHH = :loaiVthh AND DSG.MA_DVI = :maDvi AND HDR.LASTEST = :lastest",
            nativeQuery = true)
    BigDecimal countSLDalenKh(Integer namKh, String loaiVthh, String maDvi, Integer lastest);


    @Transactional()
    @Modifying
    @Query(value = "UPDATE XH_DX_KH_BAN_DAU_GIA SET TRANG_THAI_TH = :trangThaiTh , ID_THOP = :idTh WHERE SO_DXUAT IN :soDxuatList", nativeQuery = true)
    void updateStatusInList(List<String> soDxuatList, String trangThaiTh, Long idTh);


    @Query(value = " SELECT dtl.GIA_QD FROM KH_PAG_TT_CHUNG dtl " +
            "JOIN KH_PAG_QD_BTC hdr ON dtl.QD_BTC_ID = hdr.ID " +
            " WHERE hdr.TRANG_THAI = '29' AND hdr.LOAI_GIA = 'LG02'  AND dtl.CLOAI_VTHH = :cloaiVthh AND hdr.NAM_KE_HOACH = :namKhoach AND hdr.NGAY_HIEU_LUC <= SYSDATE " +
            " FETCH FIRST 1 ROWS ONLY ",
            nativeQuery = true)
    BigDecimal getGiaBanToiThieuVt(String cloaiVthh, Integer namKhoach);

    @Query(value = " SELECT dtl.GIA_QD_BTC FROM KH_PAG_QD_BTC_CTIET dtl " +
            "JOIN KH_PAG_QD_BTC hdr ON dtl.QD_BTC_ID = hdr.ID " +
            " WHERE hdr.TRANG_THAI = '29' AND hdr.LOAI_GIA = 'LG02'  AND hdr.CLOAI_VTHH = :cloaiVthh AND dtl.MA_DVI = :maDvi AND hdr.NAM_KE_HOACH = :namKhoach AND hdr.NGAY_HIEU_LUC <= SYSDATE " +
            " FETCH FIRST 1 ROWS ONLY ",
            nativeQuery = true)
    BigDecimal getGiaBanToiThieuLt(String cloaiVthh, String maDvi, Integer namKhoach);

    @Query(value = " SELECT dtl.GIA_QD_TCDT FROM KH_PAG_TT_CHUNG dtl " +
            "JOIN KH_PAG_GCT_QD_TCDTNN hdr ON dtl.QD_TCDTNN_ID = hdr.ID " +
            " WHERE hdr.TRANG_THAI = '29' AND LOAI_GIA = 'LG03'  AND dtl.CLOAI_VTHH = :cloaiVthh AND hdr.NAM_KE_HOACH = :namKhoach AND hdr.NGAY_HIEU_LUC <= SYSDATE " +
            " FETCH FIRST 1 ROWS ONLY ",
            nativeQuery = true)
    BigDecimal getDonGiaDuocDuyetVt(String cloaiVthh, Integer namKhoach);

    @Query(value = " SELECT dtl.GIA_QD_TCDTNN FROM KH_PAG_TONG_HOP_CTIET dtl " +
            "JOIN KH_PAG_GCT_QD_TCDTNN hdr ON dtl.QD_TCDTNN_ID = hdr.ID " +
            " WHERE hdr.TRANG_THAI = '29' AND hdr.LOAI_GIA = 'LG03'  AND hdr.CLOAI_VTHH = :cloaiVthh AND dtl.MA_CHI_CUC = :maDvi AND hdr.NAM_KE_HOACH = :namKhoach AND hdr.NGAY_HIEU_LUC <= SYSDATE " +
            " FETCH FIRST 1 ROWS ONLY ",
            nativeQuery = true)
    BigDecimal getDonGiaDuocDuyetLt(String cloaiVthh, String maDvi, Integer namKhoach);

    Optional<XhDxKhBanDauGia> findBySoDxuat(String soDxuat);

    List<XhDxKhBanDauGia> findAllByIdIn(List<Long> listId);

    List<XhDxKhBanDauGia> findByIdIn(List<Long> idDxList);

    List<XhDxKhBanDauGia> findAllByIdThop(Long idThop);

    List<XhDxKhBanDauGia> findByIdThopIn(List<Long> listId);
}
