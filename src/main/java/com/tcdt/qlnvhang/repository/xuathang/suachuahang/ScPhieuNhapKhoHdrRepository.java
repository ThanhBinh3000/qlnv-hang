package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScPhieuNhapKhoReq;
import com.tcdt.qlnvhang.response.suachua.ScPhieuNhapKhoDTO;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuNhapKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ScPhieuNhapKhoHdrRepository extends JpaRepository<ScPhieuNhapKhoHdr, Long> {

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.suachua.ScPhieuNhapKhoDTO( " +
            "hdr.id, qd.id, qd.soQd, hdr.nam, qd.thoiHanNhap, hdr.tenDiemKho, hdr.tenLoKho, hdr.soPhieuNhapKho," +
            "hdr.ngayNhapKho,nvt.id ,nvt.soBangKe,nvt.id ,nvt.soBangKe, hdr.trangThai " +
            ") FROM ScQuyetDinhNhapHang qd " +
            "LEFT JOIN ScPhieuNhapKhoHdr hdr ON hdr.qdGiaoNvNhapId = qd.id " +
            "LEFT JOIN ScPhieuNhapKhoDtl dtl ON dtl.hdrId = hdr.id " +
            "LEFT JOIN ScBangKeNhapVtHdr nvt ON nvt.phieuNhapKhoId = hdr.id " +
            "WHERE 1=1 " +
            "AND (:#{#param.nam} IS NULL OR hdr.nam = :#{#param.nam}) " +
            "AND (:#{#param.soQdGiaoNvNhap} IS NULL OR qd.soQd = :#{#param.soQdGiaoNvNhap}) " +
            "AND (:#{#param.soPhieuNhapKho} IS NULL OR hdr.soPhieuNhapKho = :#{#param.soPhieuNhapKho}) " +
            "AND ((:#{#param.ngayNhapKhoTu}  IS NULL OR hdr.ngayNhapKho >= :#{#param.ngayNhapKhoTu}) " +
            "AND (:#{#param.ngayNhapKhoDen}  IS NULL OR hdr.ngayNhapKho <= :#{#param.ngayNhapKhoDen}) ) ")
    Page<ScPhieuNhapKhoDTO> searchPage(@Param("param") ScPhieuNhapKhoReq req, Pageable pageable);

    Optional<ScPhieuNhapKhoHdr> findBySoPhieuNhapKho(String soPhieuNhapKho);

}
