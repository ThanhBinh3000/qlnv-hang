package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBangKeNhapVTReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeNhapVTHdrDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeNhapVTDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeNhapVTHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbBangKeNhapVTHdrRepository extends JpaRepository<DcnbBangKeNhapVTHdr, Long> {
    Optional<DcnbBangKeNhapVTHdr> findFirstBySoBangKe(String soBangKe);

    List<DcnbBangKeNhapVTHdr> findAllByIdIn(List<Long> listMulti);
    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeNhapVTHdrDTO(" +
            "bknvt.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maNhaKho, khdcd.tenNhaKho,khdcd.maNganKho, khdcd.tenNganKho, khdcd.maLoKho," +
            "khdcd.tenLoKho,bblm.id,bblm.soBbLayMau, bknvt.soBangKe, bknvt.soBangKe,pnk.soPhieuNhapKho, pnk.id, pnk.ngayLap,bknvt.trangThai ,bknvt.trangThai) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbBangKeNhapVTHdr bknvt ON bknvt.qDinhDccId = qdc.id "+
            "LEFT JOIN DcnbBienBanLayMauHdr bblm ON bblm.qdccId = qdc.id and bblm.maNganKho = bknvt.maNganKho and bblm.maLoKho = bknvt.maLoKho "+
            "LEFT JOIN DcnbPhieuNhapKhoHdr pnk ON pnk.id = bknvt.phieuNhapKhoId and pnk.maNganKho = bknvt.maNganKho and pnk.maLoKho = bknvt.maLoKho " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 "+
            "AND qdc.trangThai = '29' AND qdc.loaiDc = :#{#param.loaiDc} " +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) "+
            "AND (bknvt.type IS NULL OR (:#{#param.type} IS NULL OR bknvt.type = :#{#param.type}))" +
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) "+
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}) OR (:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))"+
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(bknvt.soBangKe) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgayThoiHan}  IS NULL OR bknvt.thoiHanGiaoNhan >= :#{#param.tuNgayThoiHan})" +
            "AND (:#{#param.denNgayThoiHan}  IS NULL OR bknvt.thoiHanGiaoNhan <= :#{#param.denNgayThoiHan}) ) " +
            "AND ((:#{#param.tuNgayNhapKho}  IS NULL OR pnk.ngayLap >= :#{#param.tuNgayNhapKho})" +
            "AND (:#{#param.denNgayNhapKho}  IS NULL OR pnk.ngayLap <= :#{#param.denNgayNhapKho}) ) " +
            "ORDER BY bknvt.soQdinhDcc desc, bknvt.nam desc")
    Page<DcnbBangKeNhapVTHdrDTO> searchPage(@Param("param")DcnbBangKeNhapVTReq req, Pageable pageable);
    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeNhapVTHdrDTO(" +
            "bknvt.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maNhaKho, khdcd.tenNhaKho,khdcd.maNganKho, khdcd.tenNganKho, khdcd.maLoKho," +
            "khdcd.tenLoKho,bblm.id,bblm.soBbLayMau, bknvt.soBangKe, bknvt.soBangKe,pnk.soPhieuNhapKho, pnk.id, pnk.ngayLap,bknvt.trangThai ,bknvt.trangThai) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbDataLinkHdr dtlh On dtlh.qdCcParentId = qdc.id " +
            "LEFT JOIN DcnbBangKeNhapVTHdr bknvt ON bknvt.qDinhDccId = dtlh.qdCcId "+
            "LEFT JOIN DcnbBienBanLayMauHdr bblm ON bblm.qdccId = dtlh.qdCcId and bblm.maNganKho = bknvt.maNganKho and bblm.maLoKho = bknvt.maLoKho "+
            "LEFT JOIN DcnbPhieuNhapKhoHdr pnk ON pnk.id = bknvt.phieuNhapKhoId and pnk.maNganKho = bknvt.maNganKho and pnk.maLoKho = bknvt.maLoKho " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = dtlh.qdCcId " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 "+
            "AND qdc.trangThai = '29' AND qdc.loaiDc = :#{#param.loaiDc} " +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) "+
            "AND (bknvt.type IS NULL OR (:#{#param.type} IS NULL OR bknvt.type = :#{#param.type}))" +
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) "+
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}) OR (:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))"+
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(bknvt.soBangKe) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgayThoiHan}  IS NULL OR bknvt.thoiHanGiaoNhan >= :#{#param.tuNgayThoiHan})" +
            "AND (:#{#param.denNgayThoiHan}  IS NULL OR bknvt.thoiHanGiaoNhan <= :#{#param.denNgayThoiHan}) ) " +
            "AND ((:#{#param.tuNgayNhapKho}  IS NULL OR pnk.ngayLap >= :#{#param.tuNgayNhapKho})" +
            "AND (:#{#param.denNgayNhapKho}  IS NULL OR pnk.ngayLap <= :#{#param.denNgayNhapKho}) ) " +
            "ORDER BY bknvt.soQdinhDcc desc, bknvt.nam desc")
    Page<DcnbBangKeNhapVTHdrDTO> searchPageCuc(@Param("param")DcnbBangKeNhapVTReq req, Pageable pageable);
}
