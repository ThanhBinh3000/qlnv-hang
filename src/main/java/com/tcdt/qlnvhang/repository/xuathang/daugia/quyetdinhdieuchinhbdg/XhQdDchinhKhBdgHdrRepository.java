package com.tcdt.qlnvhang.repository.xuathang.daugia.quyetdinhdieuchinhbdg;


import com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgHdr;
import com.tcdt.qlnvhang.request.xuathang.daugia.quyetdinhdieuchinh.XhQdDchinhKhBdgReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface XhQdDchinhKhBdgHdrRepository extends JpaRepository<XhQdDchinhKhBdgHdr, Long> {

    @Query("SELECT DC from XhQdDchinhKhBdgHdr DC WHERE 1 = 1 " +
            "AND (:#{#param.nam} IS NULL OR DC.nam = :#{#param.namKh}) " +
            "AND (:#{#param.soQdDc} IS NULL OR LOWER(DC.soQdDc) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soQdDc}),'%' ) ) )" +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(DC.trichYeu) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.trichYeu}),'%'))) " +
            "AND (:#{#param.ngayDuyetTu} IS NULL OR DC.ngayPduyet >= :#{#param.ngayDuyetTu}) " +
            "AND (:#{#param.ngayDuyetDen} IS NULL OR DC.ngayPduyet <= :#{#param.ngayDuyetDen}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR DC.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR DC.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.maDvi} IS NULL OR DC.maDvi = :#{#param.maDvi}) ")
    Page<XhQdDchinhKhBdgHdr> searchPage(@Param("param") XhQdDchinhKhBdgReq param, Pageable pageable);

    List<XhQdDchinhKhBdgHdr> findBySoQdDc(String soQdDc);

    List <XhQdDchinhKhBdgHdr> findAllByIdIn(List<Long> ids);

}