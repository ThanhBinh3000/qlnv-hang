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

@Repository
public interface XhDxKhBanDauGiaRepository extends JpaRepository<XhDxKhBanDauGia, Long> {

    @Query("SELECT DISTINCT DX FROM XhDxKhBanDauGia DX " +
            "WHERE (:#{#param.dvql} IS NULL OR DX.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.namKh} IS NULL OR DX.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soDxuat} IS NULL OR DX.soDxuat = :#{#param.soDxuat}) " +
            "AND (:#{#param.ngayTaoTu} IS NULL OR DX.ngayTao >= :#{#param.ngayTaoTu}) " +
            "AND (:#{#param.ngayTaoDen} IS NULL OR DX.ngayTao <= :#{#param.ngayTaoDen}) " +
            "AND (:#{#param.ngayDuyetTu} IS NULL OR DX.ngayPduyet >= :#{#param.ngayDuyetTu}) " +
            "AND (:#{#param.ngayDuyetDen} IS NULL OR DX.ngayPduyet <= :#{#param.ngayDuyetDen}) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(DX.trichYeu) LIKE LOWER(CONCAT('%', :#{#param.trichYeu}, '%'))) " +
            "AND (:#{#param.loaiVthh} IS NULL OR DX.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
            "AND (:#{#param.trangThai} IS NULL OR DX.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.trangThaiTh} IS NULL OR DX.trangThaiTh = :#{#param.trangThaiTh}) " +
            "AND (:#{#param.trangThaiList == null || #param.trangThaiList.isEmpty()} = true OR DX.trangThai IN :#{#param.trangThaiList}) " +
            "ORDER BY DX.namKh DESC, DX.ngaySua DESC, DX.ngayTao DESC, DX.id DESC")
    Page<XhDxKhBanDauGia> searchPage(@Param("param") XhDxKhBanDauGiaReq param, Pageable pageable);

    @Query("SELECT TH FROM XhDxKhBanDauGia TH " +
            "WHERE (:#{#param.namKh} IS NULL OR TH.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR TH.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
            "AND (:#{#param.cloaiVthh} IS NULL OR TH.cloaiVthh LIKE CONCAT(:#{#param.cloaiVthh}, '%')) " +
            "AND (:#{#param.ngayDuyetTu} IS NULL OR TH.ngayPduyet >= :#{#param.ngayDuyetTu}) " +
            "AND (:#{#param.ngayDuyetDen} IS NULL OR TH.ngayPduyet <= :#{#param.ngayDuyetDen}) " +
            "AND TH.idThop IS NULL " +
            "AND TH.soQdPd IS NULL " +
            "AND TH.trangThai ='" + Contains.DA_DUYET_CBV + "'" +
            "AND TH.trangThaiTh ='" + Contains.CHUATONGHOP + "'")
    List<XhDxKhBanDauGia> listTongHop(@Param("param") XhThopChiTieuReq param);

    @Query(value = "SELECT COALESCE(SUM(DSG.TONG_SL_XUAT_BAN_DX), 0) " +
            "FROM XH_QD_PD_KH_BDG HDR " +
            "INNER JOIN XH_QD_PD_KH_BDG_DTL DTL ON HDR.ID = DTL.ID_QD_HDR " +
            "LEFT JOIN XH_QD_PD_KH_BDG_PL DSG ON DSG.ID_QD_DTL = DTL.ID " +
            "WHERE HDR.NAM = :namKh " +
            "  AND HDR.LOAI_VTHH = :loaiVthh " +
            "  AND DSG.MA_DVI = :maDvi " +
            "  AND HDR.LASTEST = :lastest",
            nativeQuery = true)
    BigDecimal countSLDalenKh(@Param("namKh") Integer namKh,
                              @Param("loaiVthh") String loaiVthh,
                              @Param("maDvi") String maDvi,
                              @Param("lastest") Integer lastest);

    @Transactional
    @Modifying
    @Query(value = "UPDATE XH_DX_KH_BAN_DAU_GIA " +
            "SET TRANG_THAI_TH = :trangThaiTh, ID_THOP = :idTh " +
            "WHERE SO_DXUAT IN :soDxuatList", nativeQuery = true)
    void updateStatusInList(
            @Param("soDxuatList") List<String> soDxuatList,
            @Param("trangThaiTh") String trangThaiTh,
            @Param("idTh") Long idTh
    );

    boolean existsBySoDxuat(String soDxuat);

    boolean existsBySoDxuatAndIdNot(String soDxuat, Long id);

    List<XhDxKhBanDauGia> findAllByIdIn(List<Long> listId);

    List<XhDxKhBanDauGia> findByIdIn(List<Long> idDxList);

    List<XhDxKhBanDauGia> findAllByIdThop(Long idThop);

    List<XhDxKhBanDauGia> findByIdThopIn(List<Long> listId);
}
