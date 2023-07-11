package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScBangKeXuatVatTuReq;
import com.tcdt.qlnvhang.response.suachua.ScBangKeXuatVtDTO;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeXuatVatTuHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScBangKeXuatVatTuHdrRepository extends JpaRepository<ScBangKeXuatVatTuHdr, Long> {
    Optional<ScBangKeXuatVatTuHdr> findFirstBySoBangKe(String soBangKe);

    List<ScBangKeXuatVatTuHdr> findAllByIdIn(List<Long> ids);

//    @Query(value = "SELECT new com.tcdt.qlnvhang.response.suachua.ScBangKeXuatVtDTO(" +
//            "hdr.id,qd.id,qd.soQd, hdr.nam, qd.thoiHanXuat, hdr.maDiemKho, hdr.tenDiemKho, hdr.maLoKho, hdr.tenLoKho," +
//            "hdr.phieuXuatKhoId, hdr.soPhieuXuatKho, hdr.soBangKe, pxk.ngayXuatKho, hdr.trangThai" +
//            ") FROM ScQuyetDinhXuatHang qd  " +
//            "LEFT JOIN ScBangKeXuatVatTuHdr hdr on hdr.soQdGiaoNv = qd.soQd " +
//            "LEFT JOIN ScBangKeXuatVatTuDtl dtl on dtl.hdrId = hdr.id " +
//            "LEFT JOIN ScPhieuXuatKhoHdr pxk on pxk.soPhieuXuatKho = hdr.soPhieuXuatKho " +
//            "WHERE 1=1" +
//            "AND (:#{#param.nam} IS NULL OR hdr.nam = :#{#param.nam})" +
//            "AND (:#{#param.soQdGiaoNv} IS NULL OR qd.soQd = :#{#param.soQdGiaoNv})" +
//            "AND (:#{#param.soBangKe} IS NULL OR hdr.soBangKe = :#{#param.soBangKe})"
//    )
//    Page<ScBangKeXuatVtDTO> searchPage(@Param("param") ScBangKeXuatVatTuReq req, Pageable pageable);

}
