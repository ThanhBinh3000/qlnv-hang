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

@Repository
public interface XhDxKhBanTrucTiepHdrRepository extends JpaRepository<XhDxKhBanTrucTiepHdr, Long> {

    @Query("SELECT DX FROM XhDxKhBanTrucTiepHdr DX " +
            "WHERE (:#{#param.dvql} IS NULL OR DX.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.namKh} IS NULL OR DX.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soDxuat} IS NULL OR LOWER(DX.soDxuat) LIKE LOWER(CONCAT('%', :#{#param.soDxuat}, '%'))) " +
            "AND (:#{#param.ngayTaoTu} IS NULL OR DX.ngayTao >= :#{#param.ngayTaoTu}) " +
            "AND (:#{#param.ngayTaoDen} IS NULL OR DX.ngayTao <= :#{#param.ngayTaoDen}) " +
            "AND (:#{#param.ngayDuyetTu} IS NULL OR DX.ngayPduyet >= :#{#param.ngayDuyetTu}) " +
            "AND (:#{#param.ngayDuyetDen} IS NULL OR DX.ngayPduyet <= :#{#param.ngayDuyetDen}) " +
            "AND (:#{#param.ngayKyQdTu} IS NULL OR DX.ngayKyQd >= :#{#param.ngayKyQdTu}) " +
            "AND (:#{#param.ngayKyQdDen} IS NULL OR DX.ngayKyQd <= :#{#param.ngayKyQdDen}) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(DX.trichYeu) LIKE LOWER(CONCAT('%', :#{#param.trichYeu}, '%'))) " +
            "AND (:#{#param.loaiVthh} IS NULL OR DX.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
            "AND (:#{#param.trangThai} IS NULL OR DX.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.trangThaiTh} IS NULL OR DX.trangThaiTh = :#{#param.trangThaiTh}) " +
            "ORDER BY DX.namKh DESC, DX.ngaySua DESC, DX.ngayTao DESC, DX.id DESC")
    Page<XhDxKhBanTrucTiepHdr> searchPage(@Param("param") XhDxKhBanTrucTiepHdrReq param, Pageable pageable);

    @Query(value = "SELECT COALESCE(SUM(DVI.SO_LUONG_CHI_CUC), 0) " +
                    "FROM XH_QD_PD_KH_BTT_HDR HDR " +
                    "INNER JOIN XH_QD_PD_KH_BTT_DTL DTL ON HDR.ID = DTL.ID_HDR " +
                    "LEFT JOIN XH_QD_PD_KH_BTT_DVI DVI ON DTL.ID = DVI.ID_DTL " +
                    "WHERE HDR.NAM_KH = :namKh " +
                    "  AND HDR.LOAI_VTHH = :loaiVthh " +
                    "  AND DVI.MA_DVI = :maDvi " +
                    "  AND HDR.LASTEST = :lastest",
            nativeQuery = true)
    BigDecimal countSLDalenKh(@Param("namKh") Integer namKh,
                              @Param("loaiVthh") String loaiVthh,
                              @Param("maDvi") String maDvi,
                              @Param("lastest") Integer lastest);

    @Query("SELECT TH FROM XhDxKhBanTrucTiepHdr TH " +
            "WHERE (:#{#param.namKh} IS NULL OR TH.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR TH.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
            "AND (:#{#param.cloaiVthh} IS NULL OR TH.cloaiVthh LIKE CONCAT(:#{#param.cloaiVthh}, '%')) " +
            "AND (:#{#param.ngayDuyetTu} IS NULL OR TH.ngayPduyet >= :#{#param.ngayDuyetTu}) " +
            "AND (:#{#param.ngayDuyetDen} IS NULL OR TH.ngayPduyet <= :#{#param.ngayDuyetDen}) " +
            "AND TH.idThop IS NULL " +
            "AND TH.soQdPd IS NULL " +
            "AND TH.trangThai ='" + Contains.DADUYET_LDC + "'" +
            "AND TH.trangThaiTh ='" + Contains.CHUATONGHOP + "'")
    List<XhDxKhBanTrucTiepHdr> listTongHop(@Param("param") XhThopChiTieuReq param);

    @Transactional
    @Modifying
    @Query(value = "UPDATE XH_DX_KH_BAN_TRUC_TIEP_HDR " +
            "SET TRANG_THAI_TH = :trangThaiTh, ID_THOP = :idTh " +
            "WHERE SO_DXUAT IN :soDxuatList", nativeQuery = true)
    void updateStatusInList(@Param("soDxuatList") List<String> soDxuatList,
                            @Param("trangThaiTh") String trangThaiTh,
                            @Param("idTh") Long idTh);

    boolean existsBySoDxuat(String soDxuat);

    boolean existsBySoDxuatAndIdNot(String soDxuat, Long id);

    List<XhDxKhBanTrucTiepHdr> findByIdIn(List<Long> idDxList);

    List<XhDxKhBanTrucTiepHdr> findAllByIdIn(List<Long> listId);

    List<XhDxKhBanTrucTiepHdr> findAllByIdThop(Long idThop);

    List<XhDxKhBanTrucTiepHdr> findByIdThopIn(List<Long> listId);
}
