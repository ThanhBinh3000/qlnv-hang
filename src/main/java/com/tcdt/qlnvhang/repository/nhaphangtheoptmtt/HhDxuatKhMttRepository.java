package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.request.nhaphangtheoptt.SearchHhDxKhMttHdrReq;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttHdr;
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
public interface HhDxuatKhMttRepository extends JpaRepository<HhDxuatKhMttHdr, Long> {

    @Query("SELECT DISTINCT DX FROM HhDxuatKhMttHdr DX " +
            " left join HhDxKhMttThopHdr TH on TH.id = DX.maThop WHERE 1=1 " +
            "AND (:#{#param.namKh} IS NULL OR DX.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soDxuat} IS NULL OR LOWER(DX.soDxuat) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soDxuat}),'%' ) ) )" +
            "AND (:#{#param.ngayTaoTu} IS NULL OR DX.ngayTao >= :#{#param.ngayTaoTu}) " +
            "AND (:#{#param.ngayTaoDen} IS NULL OR DX.ngayTao <= :#{#param.ngayTaoDen}) " +
            "AND (:#{#param.ngayDuyetTu} IS NULL OR DX.ngayPduyet >= :#{#param.ngayDuyetTu}) " +
            "AND (:#{#param.ngayDuyetDen} IS NULL OR DX.ngayPduyet <= :#{#param.ngayDuyetDen}) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(DX.trichYeu) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.trichYeu}),'%'))) " +
            "AND (:#{#param.noiDungThop} IS NULL OR LOWER(TH.noiDungThop) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.noiDungThop}),'%'))) " +
            "AND (:#{#param.loaiVthh} IS NULL OR DX.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR DX.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.trangThaiTh} IS NULL OR DX.trangThaiTh = :#{#param.trangThaiTh}) " +
            "AND (:#{#param.maDvi} IS NULL OR DX.maDvi = :#{#param.maDvi})")
    Page<HhDxuatKhMttHdr> searchPage(@Param("param") SearchHhDxKhMttHdrReq param, Pageable pageable);


    @Transactional()
    @Modifying
    @Query(value = "UPDATE HH_DX_KHMTT_HDR SET TRANG_THAI_TH = :trangThaiTh , MA_THOP = :idTh WHERE SO_DXUAT IN :soDxuatList", nativeQuery = true)
    void updateStatusInList(List<String> soDxuatList, String trangThaiTh,Long idTh);

    @Transactional
    void deleteAllByIdIn(List<Long> ids);
    Optional<HhDxuatKhMttHdr> findBySoDxuat(String soDxuat);

    @Query(value = "select * from HH_DX_KHMTT_HDR MTT where (:namKh IS NULL OR MTT.NAM_KH = TO_NUMBER(:namKh)) " +
            " AND (:loaiVthh IS NULL OR MTT.LOAI_VTHH = :loaiVthh) " +
            " AND (:cloaiVthh IS NULL OR MTT.CLOAI_VTHH = :cloaiVthh) " +
            " AND MTT.TRANG_THAI = '"+ Contains.DA_DUYET_CBV+"'" +
            " AND MTT.TRANG_THAI_TH = '"+ Contains.CHUATONGHOP+"'" +
            " AND MTT.MA_THOP is null "
            ,nativeQuery = true)
    List<HhDxuatKhMttHdr> listTongHop(Integer namKh, String loaiVthh, String cloaiVthh);

    List<HhDxuatKhMttHdr> findByIdIn(List<Long> id);

    @Query(value = " SELECT NVL(SUM(SLDD.SO_LUONG),0) FROM HH_QD_PHE_DUYET_KHMTT_HDR HDR " +
            " INNER JOIN HH_QD_PHE_DUYET_KHMTT_DX DX on HDR.ID = DX.ID_QD_HDR " +
            " LEFT JOIN HH_QD_PHE_DUYET_KHMTT_SLDD SLDD ON SLDD.ID_QD_DTL = DX.ID " +
            "WHERE (:#{#namKh} IS NULL OR HDR.NAM_KH = :#{#namKh})" +
            " AND (:#{#loaiVthh} IS NULL OR HDR.LOAI_VTHH = :#{#loaiVthh}) " +
            " AND (:#{#maDvi} IS NULL OR SLDD.MA_DVI = :#{#maDvi}) " +
            " AND (:#{#trangThai} IS NULL OR HDR.TRANG_THAI = :#{#trangThai}) ",
            nativeQuery = true)
    BigDecimal countSLDalenKh(Integer namKh, String loaiVthh, String maDvi, String trangThai);

    HhDxuatKhMttHdr findAllByLoaiVthhAndCloaiVthhAndNamKhAndMaDviAndTrangThaiNot(String loaiVthh,String cloaiVthh,Integer namKh,String maDvi,String trangThai);

    @Transactional()
    @Modifying
    @Query(value = "UPDATE HH_DX_KHMTT_HDR SET ID_SO_QD_PDUYET = NULL, TRANG_THAI = :trangThai, TRANG_THAI_TH = :trangThaiTh, SO_QD_PDUYET = NULL WHERE ID = :idDxuat", nativeQuery = true)
    void updateIdSoQdPd(Long idDxuat, String trangThai, String trangThaiTh);

}
