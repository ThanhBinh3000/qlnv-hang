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
            "bknvt.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maNhaKho, khdcd.tenNhaKho,khdcd.maNganKho, khdcd.tenNganKho, khdcd.maLoKho," +
            "khdcd.tenLoKho,bblm.id,bblm.soBbLayMau, bknvt.soBangKe, bknvt.soBangKe,pnk.soPhieuNhapKho, pnk.id, pnk.ngayLap,bknvt.trangThai ,bknvt.trangThai) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN HhNkBangKeNhapVTHdr bknvt ON bknvt.qdPdNkId = qdc.id and bknvt.maNganKho = khdcd.maNganKhoNhan and bknvt.maLoKho = khdcd.maLoKhoNhan " +
            "LEFT JOIN DcnbBienBanLayMauHdr bblm ON bblm.qdccId = qdc.id and bblm.maNganKho = bknvt.maNganKho and bblm.maLoKho = bknvt.maLoKho " +
            "LEFT JOIN DcnbPhieuNhapKhoHdr pnk ON pnk.id = bknvt.phieuNhapKhoId and pnk.maNganKho = bknvt.maNganKho and pnk.maLoKho = bknvt.maLoKho " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.parentId is not null and qdc.trangThai = '29' AND qdc.loaiDc = :#{#param.loaiDc} " +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND (bknvt.type IS NULL OR (:#{#param.type} IS NULL OR bknvt.type = :#{#param.type}))" +
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(bknvt.soBangKe) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgayThoiHan}  IS NULL OR bknvt.thoiHanGiaoNhan >= :#{#param.tuNgayThoiHan})" +
            "AND (:#{#param.denNgayThoiHan}  IS NULL OR bknvt.thoiHanGiaoNhan <= :#{#param.denNgayThoiHan}) ) " +
            "AND ((:#{#param.tuNgayNhapKho}  IS NULL OR pnk.ngayLap >= :#{#param.tuNgayNhapKho})" +
            "AND (:#{#param.denNgayNhapKho}  IS NULL OR pnk.ngayLap <= :#{#param.denNgayNhapKho}) ) " +
            "GROUP BY bknvt.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maNhaKho, khdcd.tenNhaKho,khdcd.maNganKho, khdcd.tenNganKho, khdcd.maLoKho," +
            "khdcd.tenLoKho,bblm.id,bblm.soBbLayMau, bknvt.soBangKe, bknvt.soBangKe,pnk.soPhieuNhapKho, pnk.id, pnk.ngayLap,bknvt.trangThai ,bknvt.trangThai")
    Page<HhNkBangKeNhapVTHdrDTO> searchPage(@Param("param") HhNkBangKeNhapVTReq req, Pageable pageable);
}
