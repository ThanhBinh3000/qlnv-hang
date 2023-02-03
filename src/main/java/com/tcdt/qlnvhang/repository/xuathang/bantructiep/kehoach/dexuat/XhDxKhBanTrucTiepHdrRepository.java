package com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.dexuat;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdrReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.dexuat.XhDxKhBanDauGiaReq;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    Optional<XhDxKhBanTrucTiepHdr> findBySoKeHoach(String soKeHoach);

    List<XhDxKhBanTrucTiepHdr> findByIdIn(List<Long> idDxList);
}
