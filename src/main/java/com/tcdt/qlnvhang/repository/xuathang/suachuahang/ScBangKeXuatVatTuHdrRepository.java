package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScBangKeXuatVatTuReq;
import com.tcdt.qlnvhang.request.suachua.ScPhieuXuatKhoReq;
import com.tcdt.qlnvhang.response.suachua.ScBangKeXuatVtDTO;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeXuatVatTuHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuXuatKhoHdr;
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

    @Query(value = "SELECT qd FROM ScBangKeXuatVatTuHdr qd " +
            " LEFT JOIN ScPhieuXuatKhoHdr pxk on qd.idPhieuXuatKho = pxk.id " +
            " WHERE 1 = 1 " +
            "AND (:#{#param.nam} IS NULL OR qd.nam = :#{#param.nam}) " +
            "AND (:#{#param.maDviSr} IS NULL OR qd.maDvi = :#{#param.maDviSr}) " +
            "AND (:#{#param.idScDanhSachHdr} IS NULL OR pxk.idScDanhSachHdr = :#{#param.idScDanhSachHdr}) " +
            "AND (:#{#param.soPhieuXuatKho} IS NULL OR qd.soPhieuXuatKho LIKE CONCAT(:#{#param.soPhieuXuatKho},'%'))" +
            "AND (:#{#param.soBangKe} IS NULL OR qd.soBangKe LIKE CONCAT(:#{#param.soBangKe},'%'))" +
            "AND ((:#{#param.ngayTu} IS NULL OR qd.ngayXuatKho >= :#{#param.ngayTu}) AND (:#{#param.ngayDen} IS NULL OR qd.ngayXuatKho <= :#{#param.ngayDen})) " +
            " ORDER BY qd.ngaySua desc , qd.ngayTao desc, qd.id desc "
    )
    List<ScBangKeXuatVatTuHdr> searchList(@Param("param") ScBangKeXuatVatTuReq req);

}
