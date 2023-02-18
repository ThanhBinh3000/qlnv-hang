package com.tcdt.qlnvhang.repository.xuathang.bantructiep.dieuchinh;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.dieuchinh.XhQdDchinhKhBttHdrReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdrReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdDchinhKhBttHdrRepository extends JpaRepository<XhQdDchinhKhBttHdr, Long> {

    @Query("SELECT DX from XhQdDchinhKhBttHdr DX WHERE 1 = 1 " +
            "AND (:#{#param.namKh} IS NULL OR DX.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.soQdDc} IS NULL OR LOWER(DX.soQdDc) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soQdDc}),'%' ) ) )" +
            "AND (:#{#param.ngayKyDcTu} IS NULL OR DX.ngayKyDc >= :#{#param.ngayKyDcTu}) " +
            "AND (:#{#param.ngayKyDcDen} IS NULL OR DX.ngayKyDc <= :#{#param.ngayKyDcDen}) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(DX.trichYeu) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.trichYeu}),'%'))) " +
            "AND (:#{#param.loaiVthh} IS NULL OR DX.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR DX.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDvi} IS NULL OR DX.maDvi = :#{#param.maDvi})")
    Page<XhQdDchinhKhBttHdr> searchPage(@Param("param") XhQdDchinhKhBttHdrReq param, Pageable pageable);


    List<XhQdDchinhKhBttHdr> findBySoQdDc(String soQdDc);

    List<XhQdDchinhKhBttHdr> findByIdIn(List<Long> idDcList);
}
