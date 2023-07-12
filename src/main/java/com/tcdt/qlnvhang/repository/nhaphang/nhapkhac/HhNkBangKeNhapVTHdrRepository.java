package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBangKeNhapVTHdr;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkBangKeNhapVTReq;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBangKeNhapVTHdrDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HhNkBangKeNhapVTHdrRepository extends JpaRepository<HhNkBangKeNhapVTHdr, Long> {
    Optional<HhNkBangKeNhapVTHdr> findFirstBySoBangKe(String soBangKe);

    List<HhNkBangKeNhapVTHdr> findAllByIdIn(List<Long> listMulti);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBangKeNhapVTHdrDTO(" +
            "bknvt.id,qdgnv.id,qdgnv.soQd,qdgnv.nam,dtl.maDiemKho,dmdvdiemkho.tenDvi,dtl.maNhaKho, dmdvnhakho.tenDvi,dtl.maNganKho, dmdvngankho.tenDvi, dtl.maLoKho," +
            "dmdvlokho.tenDvi,bblm.id,bblm.soBienBan, bknvt.soBangKe, bknvt.soBangKe,pnk.soPhieuNhapKho, pnk.id, pnk.ngayLap,bknvt.trangThai ,bknvt.trangThai) " +
            "FROM HhQdGiaoNvuNhapHangKhacHdr qdgnv " +
            "LEFT JOIN HhQdPdNhapKhacHdr hdr ON hdr.id = qdgnv.idQdPdNk " +
            "LEFT JOIN HhQdPdNhapKhacDtl dtl ON hdr.id = dtl.idHdr " +
            "LEFT JOIN HhNkBangKeNhapVTHdr bknvt ON bknvt.qdPdNkId = qdgnv.id and bknvt.maNganKho = dtl.maNganKho and bknvt.maLoKho = dtl.maLoKho " +
            "LEFT JOIN HhQdGiaoNvuNhapHangKhacHdr bblmh ON bblmh.idQdPdNk = qdgnv.id " +
            "LEFT JOIN BienBanLayMauKhac bblm ON bblmh.id = bblm.idQdGiaoNvNh and bblm.maNganKho = bknvt.maNganKho and bblm.maLoKho = bknvt.maLoKho " +
            "LEFT JOIN HhNkPhieuNhapKhoHdr pnk ON pnk.id = bknvt.phieuNhapKhoId and pnk.maNganKho = bknvt.maNganKho and pnk.maLoKho = bknvt.maLoKho " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = dtl.cloaiVthh " +
            "LEFT JOIN QlnvDmDonvi dmdvnhakho On dmdvnhakho.maDvi = dtl.maNhaKho " +
            "LEFT JOIN QlnvDmDonvi dmdvdiemkho On dmdvdiemkho.maDvi = dtl.maDiemKho " +
            "LEFT JOIN QlnvDmDonvi dmdvlokho On dmdvlokho.maDvi = dtl.maLoKho " +
            "LEFT JOIN QlnvDmDonvi dmdvngankho On dmdvngankho.maDvi = dtl.maNganKho " +
            "WHERE 1 =1 " +
            "AND qdgnv.trangThai = '29'" +
            "AND (:#{#param.loaiVthh} IS NULL OR dmvt.ma LIKE CONCAT('',LOWER(:#{#param.loaiVthh}),'%')) " +
            "AND ((:#{#param.maDvi} IS NULL OR dtl.maChiCuc LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND (:#{#param.soQdPdNk} IS NULL OR LOWER(qdgnv.soQd) LIKE CONCAT('%',LOWER(:#{#param.soQdPdNk}),'%')) " +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(bknvt.soBangKe) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
            "AND (:#{#param.nam} IS NULL OR qdgnv.nam = :#{#param.nam}) " +
            "AND ((:#{#param.tuNgayThoiHan}  IS NULL OR bknvt.thoiHanGiaoNhan >= :#{#param.tuNgayThoiHan})" +
            "AND (:#{#param.denNgayThoiHan}  IS NULL OR bknvt.thoiHanGiaoNhan <= :#{#param.denNgayThoiHan}) ) " +
            "AND ((:#{#param.tuNgayNhapKho}  IS NULL OR pnk.ngayLap >= :#{#param.tuNgayNhapKho})" +
            "AND (:#{#param.denNgayNhapKho}  IS NULL OR pnk.ngayLap <= :#{#param.denNgayNhapKho}) ) " +
            "GROUP BY bknvt.id,qdgnv.id,qdgnv.soQd,qdgnv.nam,dtl.maDiemKho,dmdvdiemkho.tenDvi,dtl.maNhaKho, dmdvnhakho.tenDvi,dtl.maNganKho, dmdvngankho.tenDvi, dtl.maLoKho," +
            "dmdvlokho.tenDvi,bblm.id,bblm.soBienBan, bknvt.soBangKe, bknvt.soBangKe,pnk.soPhieuNhapKho, pnk.id, pnk.ngayLap,bknvt.trangThai ,bknvt.trangThai")
    Page<HhNkBangKeNhapVTHdrDTO> searchPage(@Param("param") HhNkBangKeNhapVTReq req, Pageable pageable);
}
