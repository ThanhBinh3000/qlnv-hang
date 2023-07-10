package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.suachua.ScBangKeNhapVtReq;
import com.tcdt.qlnvhang.response.suachua.ScBangKeNhapVtDTO;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeNhapVtHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScBangKeNhapVtHdrRepository extends JpaRepository<ScBangKeNhapVtHdr, Long> {
    Optional<ScBangKeNhapVtHdr> findFirstBySoBangKe(String soBangKe);

    List<ScBangKeNhapVtHdr> findAllByIdIn(List<Long> id);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.suachua.ScBangKeNhapVtDTO ( " +
            "hdr.id, qdn.id, qdn.soQd, hdr.nam, qdn.thoiHanNhap, hdr.maLoKho, hdr.tenLoKho, hdr.maDiemKho," +
            "hdr.tenDiemKho, hdr.soBangKe, hdr.phieuNhapKhoId, hdr.soPhieuNhapKho, pnk.ngayNhapKho, hdr.trangThai) " +
            "FROM ScQuyetDinhNhapHang qdn " +
            "LEFT JOIN ScBangKeNhapVtHdr hdr on hdr.qdGiaoNvNhapId = qdn.id " +
            "LEFT JOIN ScBangKeNhapVtDtl dtl ON dtl.hdrId = hdr.id " +
            "LEFT JOIN ScPhieuNhapKhoHdr pnk ON pnk.bangKeNhapVtId = hdr.id " +
            "WHERE 1=1 " +
            "AND (:#{#param.nam} IS NULL OR hdr.nam = :#{#param.nam}) " +
            "AND (:#{#param.soQdGiaoNvNhap} IS NULL OR qdn.soQd = :#{#param.soQdGiaoNvNhap}) " +
            "AND (:#{#param.soBangKe} IS NULL OR hdr.soBangKe = :#{#param.soBangKe}) ")
    Page<ScBangKeNhapVtDTO> searchPage(@Param("param") ScBangKeNhapVtReq req, Pageable pageable);
}
