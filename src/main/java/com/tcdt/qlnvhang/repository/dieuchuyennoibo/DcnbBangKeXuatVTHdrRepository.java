package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBangKeXuatVTReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeXuatVTHdrDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeXuatVTHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbBangKeXuatVTHdrRepository extends JpaRepository<DcnbBangKeXuatVTHdr, Long> {
    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeXuatVTHdrDTO(" +
            "bkxvt.id,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,khdch.nam,khdcd.thoiGianDkDc,khdcd.maNhaKho,khdcd.tenNhaKho," +
            "khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho,khdcd.tenLoKho," +
            "khdcd.maNganKho,khdcd.tenNganKho,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.donViTinh,khdcd.tenDonViTinh," +
            "pxk.soPhieuXuatKho,pxk.id,bkxvt.soBangKe,pxk.ngayXuatKho, bkxvt.trangThai,bkxvt.trangThai) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbBangKeXuatVTHdr bkxvt ON bkxvt.qDinhDccId = qdc.id " +
            "and ((khdcd.maLoKho is not null and  bkxvt.maLoKho = khdcd.maLoKho and bkxvt.maNganKho = khdcd.maNganKho ) or (khdcd.maLoKho is null and bkxvt.maNganKho = khdcd.maNganKho))" +
            "LEFT JOIN DcnbBienBanTinhKhoHdr bbtk ON bbtk.qDinhDccId = qdc.id " +
            "and ((khdcd.maLoKho is not null and  bbtk.maLoKho = khdcd.maLoKho and bbtk.maNganKho = khdcd.maNganKho ) or (khdcd.maLoKho is null and bbtk.maNganKho = khdcd.maNganKho ))" +
            "LEFT JOIN DcnbPhieuXuatKhoHdr pxk ON pxk.id = bkxvt.phieuXuatKhoId " +
            "and ((khdcd.maLoKho is not null and  pxk.maLoKho = khdcd.maLoKho and pxk.maNganKho = khdcd.maNganKho ) or (khdcd.maLoKho is null and pxk.maNganKho = khdcd.maNganKho ))" +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 = 1 " +
            "AND qdc.parentId is not null and qdc.trangThai = '29' AND qdc.loaiDc = :#{#param.loaiDc} " +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(bkxvt.soBangKe) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgayThoiHan}  IS NULL OR bkxvt.thoiHanGiaoNhan >= :#{#param.tuNgayThoiHan})" +
            "AND (:#{#param.denNgayThoiHan}  IS NULL OR bkxvt.thoiHanGiaoNhan <= :#{#param.denNgayThoiHan}) ) " +
            "AND ((:#{#param.tuNgayXuatKho}  IS NULL OR pxk.ngayXuatKho >= :#{#param.tuNgayXuatKho})" +
            "AND (:#{#param.denNgayXuatKho}  IS NULL OR pxk.ngayXuatKho <= :#{#param.denNgayXuatKho}) ) " +
            "GROUP BY bkxvt.id,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,khdch.nam,khdcd.thoiGianDkDc,khdcd.maNhaKho,khdcd.tenNhaKho," +
            "khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho,khdcd.tenLoKho," +
            "khdcd.maNganKho,khdcd.tenNganKho,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.donViTinh,khdcd.tenDonViTinh," +
            "pxk.soPhieuXuatKho,pxk.id,bkxvt.soBangKe,pxk.ngayXuatKho, bkxvt.trangThai,bkxvt.trangThai")
    Page<DcnbBangKeXuatVTHdrDTO> searchPage(@Param("param") DcnbBangKeXuatVTReq req, Pageable pageable);

    Optional<DcnbBangKeXuatVTHdr> findFirstBySoBangKe(String soBangKe);

    List<DcnbBangKeXuatVTHdr> findAllByIdIn(List<Long> listMulti);
}
