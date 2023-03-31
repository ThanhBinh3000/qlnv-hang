package com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat;

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
public interface XhDxKhBanDauGiaRepository extends JpaRepository<XhDxKhBanDauGia,Long> {
    @Query("SELECT DX from XhDxKhBanDauGia DX WHERE 1 = 1 " +
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
            "AND (:#{#param.maDvi} IS NULL OR DX.maDvi = :#{#param.maDvi}) " +
            "AND (:#{#param.trangThaiList == null || #param.trangThaiList.isEmpty() } = true OR DX.trangThai IN :#{#param.trangThaiList}) ")
    Page<XhDxKhBanDauGia> searchPage(@Param("param") XhDxKhBanDauGiaReq param, Pageable pageable);

    Optional<XhDxKhBanDauGia> findBySoDxuat(String soDxuat);

    List<XhDxKhBanDauGia> findAllByIdIn(List<Long> listId);

    XhDxKhBanDauGia findAllByLoaiVthhAndCloaiVthhAndNamKhAndMaDviAndTrangThaiNot(String loaiVthh, String cloaiVthh, Integer namKh, String maDvi, String trangThai);

    @Query(value = "select * from XH_DX_KH_BAN_DAU_GIA DX where (:namKh IS NULL OR DX.NAM_KH = TO_NUMBER(:namKh)) " +
            " AND (:loaiVthh IS NULL OR DX.LOAI_VTHH = :loaiVthh) " +
            " AND (:cloaiVthh IS NULL OR DX.CLOAI_VTHH = :cloaiVthh) " +
            " AND (:ngayDuyetTu IS NULL OR DX.NGAY_PDUYET >=  TO_DATE(:ngayDuyetTu,'yyyy-MM-dd')) " +
            " AND (:ngayDuyetDen IS NULL OR DX.NGAY_PDUYET <= TO_DATE(:ngayDuyetDen,'yyyy-MM-dd'))" +
            " AND DX.TRANG_THAI = '"+ Contains.DA_DUYET_CBV+"'" +
            " AND DX.TRANG_THAI_TH = '"+ Contains.CHUATONGHOP+"'" +
            " AND DX.ID_THOP is null "+
            " AND DX.SO_QD_PD is null "
            ,nativeQuery = true)
    List<XhDxKhBanDauGia> listTongHop(Integer namKh, String loaiVthh, String cloaiVthh,String ngayDuyetTu, String ngayDuyetDen);

    List<XhDxKhBanDauGia> findBySoDxuatIn (List<String> list);

    XhDxKhBanDauGia findAllById(Long idDxuat);

    List<XhDxKhBanDauGia> findByIdIn(List<Long> idDxList);

    @Transactional
    void deleteAllByIdIn(List<Long> ids);

    @Query(value = " SELECT NVL(SUM(DSG.SO_LUONG),0) FROM XH_QD_PD_KH_BDG HDR " +
            " INNER JOIN XH_QD_PD_KH_BDG_DTL DTL on HDR.ID = DTL.ID_QD_HDR " +
            " LEFT JOIN XH_QD_PD_KH_BDG_PL DSG ON DSG.ID_QD_DTL = DTL.ID " +
            "WHERE HDR.NAM = :namKh AND HDR.LOAI_VTHH = :loaiVthh AND DSG.MA_DVI = :maDvi AND HDR.LASTEST = :lastest",
            nativeQuery = true)
    BigDecimal countSLDalenKh(Integer namKh, String loaiVthh, String maDvi, Integer lastest);


    @Transactional()
    @Modifying
    @Query(value = "UPDATE XH_DX_KH_BAN_DAU_GIA SET TRANG_THAI_TH = :trangThaiTh , ID_THOP = :idTh WHERE SO_DXUAT IN :soDxuatList", nativeQuery = true)
    void updateStatusInList(List<String> soDxuatList, String trangThaiTh,Long idTh);

    @Transactional()
    @Modifying
    @Query(value = "UPDATE XH_DX_KH_BAN_DAU_GIA SET TRANG_THAI_TH = :trangThaiTh WHERE ID = :idDx", nativeQuery = true)
    void updateStatusTh(Long idDx, String trangThaiTh);




}
