package com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.dexuat;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdrReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.dexuat.XhDxKhBanDauGiaReq;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.util.Contains;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhDxKhBanTrucTiepHdrRepository extends JpaRepository<XhDxKhBanTrucTiepHdr, Long> {

    @Query("SELECT DX from XhDxKhBanTrucTiepHdr DX WHERE 1 = 1 " +
            "AND (:#{#param.namKh} IS NULL OR DX.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soKeHoach} IS NULL OR LOWER(DX.soKeHoach) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soKeHoach}),'%' ) ) )" +
            "AND (:#{#param.ngayTaoTu} IS NULL OR DX.ngayTao >= :#{#param.ngayTaoTu}) " +
            "AND (:#{#param.ngayTaoDen} IS NULL OR DX.ngayTao <= :#{#param.ngayTaoDen}) " +
            "AND (:#{#param.ngayDuyetTu} IS NULL OR DX.ngayPduyet >= :#{#param.ngayDuyetTu}) " +
            "AND (:#{#param.ngayDuyetDen} IS NULL OR DX.ngayPduyet <= :#{#param.ngayDuyetDen}) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(DX.trichYeu) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.trichYeu}),'%'))) " +
            "AND (:#{#param.loaiVthh} IS NULL OR DX.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR DX.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.trangThaiTh} IS NULL OR DX.trangThaiTh = :#{#param.trangThaiTh}) " +
            "AND (:#{#param.maDvi} IS NULL OR DX.maDvi = :#{#param.maDvi})")
    Page<XhDxKhBanTrucTiepHdr> searchPage(@Param("param") XhDxKhBanTrucTiepHdrReq param, Pageable pageable);

    @Query(value = "select * from XH_DX_KH_BAN_TRUC_TIEP_HDR DX where (:namKh IS NULL OR DX.NAM_KH = TO_NUMBER(:namKh)) " +
            " AND (:loaiVthh IS NULL OR DX.LOAI_VTHH = :loaiVthh) " +
            " AND (:cloaiVthh IS NULL OR DX.CLOAI_VTHH = :cloaiVthh) " +
            " AND (:ngayDuyetTu IS NULL OR DX.NGAY_PDUYET >=  TO_DATE(:ngayDuyetTu,'yyyy-MM-dd')) " +
            " AND (:ngayDuyetDen IS NULL OR DX.NGAY_PDUYET <= TO_DATE(:ngayDuyetDen,'yyyy-MM-dd'))" +
            " AND DX.TRANG_THAI = '"+ Contains.DADUYET_LDC+"'" +
            " AND DX.TRANG_THAI_TH = '"+ Contains.CHUATONGHOP+"'" +
            " AND DX.ID_THOP is null "+
            " AND DX.SO_QD_PD is null "
            ,nativeQuery = true)
    List<XhDxKhBanTrucTiepHdr> listTongHop(Integer namKh, String loaiVthh, String cloaiVthh,String ngayDuyetTu, String ngayDuyetDen);


    @Transactional()
    @Modifying
    @Query(value = "UPDATE XH_DX_KH_BAN_TRUC_TIEP_HDR SET TRANG_THAI_TH = :trangThaiTh , ID_THOP = :idTh WHERE SO_DXUAT IN :soDxuatList", nativeQuery = true)
    void updateStatusInList(List<String> soDxuatList, String trangThaiTh,Long idTh);



    Optional<XhDxKhBanTrucTiepHdr> findBySoKeHoach(String soKeHoach);

    List<XhDxKhBanTrucTiepHdr> findByIdIn(List<Long> idDxList);
}
