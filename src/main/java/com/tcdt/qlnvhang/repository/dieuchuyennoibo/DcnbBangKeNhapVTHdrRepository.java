package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBangKeNhapVTReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeNhapVTHdrDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeNhapVTHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbBangKeNhapVTHdrRepository extends JpaRepository<DcnbBangKeNhapVTHdr, Long> {
    Optional<DcnbBangKeNhapVTHdr> findFirstBySoBangKe(String soBangKe);

    List<DcnbBangKeNhapVTHdr> findAllByIdIn(List<Long> listMulti);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeNhapVTHdrDTO(" +
            "bknvt.id,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maNhaKho, khdcd.tenNhaKho," +
            "khdcd.maNganKho, khdcd.tenNganKho, khdcd.maLoKho,khdcd.tenLoKho,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.donViTinh,khdcd.tenDonViTinh," +
            "bblm.id,bblm.soBbLayMau, bknvt.soBangKe, bknvt.soBangKe,pnk.soPhieuNhapKho, pnk.id, pnk.ngayLap,bknvt.trangThai ,bknvt.trangThai) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbBangKeNhapVTHdr bknvt ON bknvt.qDinhDccId = qdc.id " +
            "and ((khdcd.maNganKhoNhan is not null and  bknvt.maNganKho = khdcd.maNganKhoNhan and bknvt.maLoKho = khdcd.maLoKhoNhan ) or (khdcd.maLoKho is null and bknvt.maNganKho = khdcd.maNganKhoNhan))" +
            "LEFT JOIN DcnbBienBanLayMauHdr bblm ON bblm.qdccId = qdc.id " +
            "and ((bblm.maLoKho is not null and  bblm.maNganKho = bknvt.maNganKho and bblm.maLoKho = bknvt.maLoKho ) or (bblm.maLoKho is null and bblm.maNganKho = bknvt.maNganKho))" +
            "LEFT JOIN DcnbPhieuNhapKhoHdr pnk ON pnk.id = bknvt.phieuNhapKhoId " +
            "and pnk.maNganKho = bknvt.maNganKho and pnk.maLoKho = bknvt.maLoKho " +
            "and ((pnk.maLoKho is not null and  pnk.maNganKho = bknvt.maNganKho and pnk.maLoKho = bknvt.maLoKho ) or (pnk.maLoKho is null and pnk.maNganKho = bknvt.maNganKho))" +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.parentId is not null and qdc.trangThai = '29' AND qdc.loaiDc = :#{#param.loaiDc} " +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND (bknvt.type IS NULL OR (:#{#param.type} IS NULL OR bknvt.type = :#{#param.type}))" +
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%'))) " +
            "AND (qdc.loaiDc= 'DCNB' OR  ((:#{#param.typeQd} IS NULL OR qdc.type LIKE CONCAT('%',:#{#param.typeQd},'%'))))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(bknvt.soBangKe) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgayThoiHan}  IS NULL OR bknvt.thoiHanGiaoNhan >= :#{#param.tuNgayThoiHan})" +
            "AND (:#{#param.denNgayThoiHan}  IS NULL OR bknvt.thoiHanGiaoNhan <= :#{#param.denNgayThoiHan}) ) " +
            "AND ((:#{#param.tuNgayNhapKho}  IS NULL OR pnk.ngayLap >= :#{#param.tuNgayNhapKho})" +
            "AND (:#{#param.denNgayNhapKho}  IS NULL OR pnk.ngayLap <= :#{#param.denNgayNhapKho}) ) " +
            "GROUP BY bknvt.id,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maNhaKho, khdcd.tenNhaKho," +
            "khdcd.maNganKho, khdcd.tenNganKho, khdcd.maLoKho,khdcd.tenLoKho,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.donViTinh,khdcd.tenDonViTinh," +
            "bblm.id,bblm.soBbLayMau, bknvt.soBangKe, bknvt.soBangKe,pnk.soPhieuNhapKho, pnk.id, pnk.ngayLap,bknvt.trangThai ,bknvt.trangThai "+
            "ORDER BY qdc.soQdinh DESC")
    Page<DcnbBangKeNhapVTHdrDTO> searchPage(@Param("param") DcnbBangKeNhapVTReq req, Pageable pageable);
}
