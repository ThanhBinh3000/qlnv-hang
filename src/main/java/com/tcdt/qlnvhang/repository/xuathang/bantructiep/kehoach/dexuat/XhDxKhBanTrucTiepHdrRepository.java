package com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.dexuat;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdrReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.XhThopChiTieuReq;
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
public interface XhDxKhBanTrucTiepHdrRepository extends JpaRepository<XhDxKhBanTrucTiepHdr, Long> {

    @Query("SELECT DX FROM XhDxKhBanTrucTiepHdr DX " +
            " WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR DX.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.namKh} IS NULL OR DX.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soDxuat} IS NULL OR LOWER(DX.soDxuat) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soDxuat}),'%' ) ) )" +
            "AND (:#{#param.ngayTaoTu} IS NULL OR DX.ngayTao >= :#{#param.ngayTaoTu}) " +
            "AND (:#{#param.ngayTaoDen} IS NULL OR DX.ngayTao <= :#{#param.ngayTaoDen}) " +
            "AND (:#{#param.ngayDuyetTu} IS NULL OR DX.ngayPduyet >= :#{#param.ngayDuyetTu}) " +
            "AND (:#{#param.ngayDuyetDen} IS NULL OR DX.ngayPduyet <= :#{#param.ngayDuyetDen}) " +
            "AND (:#{#param.ngayKyQdTu} IS NULL OR DX.ngayKyQd >= :#{#param.ngayKyQdTu}) " +
            "AND (:#{#param.ngayKyQdDen} IS NULL OR DX.ngayKyQd <= :#{#param.ngayKyQdDen}) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(DX.trichYeu) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.trichYeu}),'%'))) " +
            "AND (:#{#param.loaiVthh} IS NULL OR DX.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR DX.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.trangThaiTh} IS NULL OR DX.trangThaiTh = :#{#param.trangThaiTh}) " +
            "ORDER BY DX.ngaySua desc , DX.ngayTao desc, DX.id desc")
    Page<XhDxKhBanTrucTiepHdr> searchPage(@Param("param") XhDxKhBanTrucTiepHdrReq param, Pageable pageable);

    @Query(value = " SELECT NVL(SUM(DVI.SO_LUONG_CHI_CUC),0) FROM XH_QD_PD_KH_BTT_HDR HDR " +
            " INNER JOIN XH_QD_PD_KH_BTT_DTL DTL on HDR.ID = DTL.ID_QD_HDR " +
            " LEFT JOIN XH_QD_PD_KH_BTT_DVI DVI ON DTL.ID = DVI.ID_QD_DTL " +
            "WHERE HDR.NAM_KH = :namKh AND HDR.LOAI_VTHH = :loaiVthh AND DVI.MA_DVI = :maDvi AND HDR.LASTEST = :lastest",
            nativeQuery = true)
    BigDecimal countSLDalenKh(Integer namKh, String loaiVthh, String maDvi, Integer lastest);

    @Query("SELECT TH from XhDxKhBanTrucTiepHdr TH WHERE 1 = 1 " +
            "AND (:#{#param.namKh} IS NULL OR TH.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR TH.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.cloaiVthh} IS NULL OR TH.cloaiVthh LIKE CONCAT(:#{#param.cloaiVthh},'%'))  " +
            "AND (:#{#param.ngayDuyetTu} IS NULL OR TH.ngayPduyet >= :#{#param.ngayDuyetTu}) " +
            "AND (:#{#param.ngayDuyetDen} IS NULL OR TH.ngayPduyet <= :#{#param.ngayDuyetDen}) " +
            "AND TH.idThop IS NULL " +
            "AND TH.soQdPd IS NULL " +
            "AND TH.trangThai ='" + Contains.DADUYET_LDC + "'" +
            "AND TH.trangThaiTh ='" + Contains.CHUATONGHOP + "'")
    List<XhDxKhBanTrucTiepHdr> listTongHop(@Param("param") XhThopChiTieuReq param);

    @Transactional()
    @Modifying
    @Query(value = "UPDATE XH_DX_KH_BAN_TRUC_TIEP_HDR SET TRANG_THAI_TH = :trangThaiTh , ID_THOP = :idTh WHERE SO_DXUAT IN :soDxuatList", nativeQuery = true)
    void updateStatusInList(List<String> soDxuatList, String trangThaiTh, Long idTh);

    @Query(value = " SELECT dtl.GIA_QD_TCDT FROM KH_PAG_TT_CHUNG dtl " +
            "JOIN KH_PAG_GCT_QD_TCDTNN hdr ON dtl.QD_TCDTNN_ID = hdr.ID " +
            " WHERE hdr.TRANG_THAI = '29' AND LOAI_GIA = 'LG04'  AND dtl.CLOAI_VTHH = :cloaiVthh AND hdr.NAM_KE_HOACH = :namKhoach AND hdr.NGAY_HIEU_LUC <= SYSDATE " +
            " FETCH FIRST 1 ROWS ONLY ",
            nativeQuery = true)
    BigDecimal getDonGiaDuocDuyetVt(String cloaiVthh, Integer namKhoach);

    @Query(value = " SELECT dtl.GIA_QD_TCDTNN FROM KH_PAG_TONG_HOP_CTIET dtl " +
            "JOIN KH_PAG_GCT_QD_TCDTNN hdr ON dtl.QD_TCDTNN_ID = hdr.ID " +
            " WHERE hdr.TRANG_THAI = '29' AND hdr.LOAI_GIA = 'LG04'  AND hdr.CLOAI_VTHH = :cloaiVthh AND dtl.MA_CHI_CUC = :maDvi AND hdr.NAM_KE_HOACH = :namKhoach AND hdr.NGAY_HIEU_LUC <= SYSDATE " +
            " FETCH FIRST 1 ROWS ONLY ",
            nativeQuery = true)
    BigDecimal getDonGiaDuocDuyetLt(String cloaiVthh, String maDvi, Integer namKhoach);

    @Query(value = " SELECT dtl.GIA_QD_BTC FROM KH_PAG_TT_CHUNG dtl " +
            "JOIN KH_PAG_QD_BTC hdr ON dtl.QD_BTC_ID = hdr.ID " +
            " WHERE hdr.TRANG_THAI = '29' AND hdr.LOAI_GIA = 'LG02'  AND dtl.LOAI_VTHH = :cloaiVthh AND hdr.NAM_KE_HOACH = :namKhoach AND hdr.NGAY_HIEU_LUC <= SYSDATE " +
            " FETCH FIRST 1 ROWS ONLY ",
            nativeQuery = true)
    BigDecimal getGiaBanToiThieuVt(String cloaiVthh, Integer namKhoach);

    @Query(value = " SELECT dtl.GIA_QD_BTC FROM KH_PAG_QD_BTC_CTIET dtl " +
            "JOIN KH_PAG_QD_BTC hdr ON dtl.QD_BTC_ID = hdr.ID " +
            " WHERE hdr.TRANG_THAI = '29' AND hdr.LOAI_GIA = 'LG02'  AND hdr.CLOAI_VTHH = :cloaiVthh AND dtl.MA_DVI = :maDvi AND hdr.NAM_KE_HOACH = :namKhoach AND hdr.NGAY_HIEU_LUC <= SYSDATE " +
            " FETCH FIRST 1 ROWS ONLY ",
            nativeQuery = true)
    BigDecimal getGiaBanToiThieuLt(String cloaiVthh, String maDvi, Integer namKhoach);

    Optional<XhDxKhBanTrucTiepHdr> findBySoDxuat(String soDxuat);

    List<XhDxKhBanTrucTiepHdr> findByIdIn(List<Long> idDxList);

    List<XhDxKhBanTrucTiepHdr> findAllByIdIn(List<Long> listId);

    List<XhDxKhBanTrucTiepHdr> findAllByIdThop(Long idThop);

    List<XhDxKhBanTrucTiepHdr> findByIdThopIn(List<Long> listId);
}
