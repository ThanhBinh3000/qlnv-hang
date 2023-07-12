package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBBKetThucNKHdr;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkBBKetThucNKReq;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBBKetThucNKHdrDTO;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBBKetThucNKHdrListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HhNkBBKetThucNKHdrRepository extends JpaRepository<HhNkBBKetThucNKHdr, Long> {
    Optional<HhNkBBKetThucNKHdr> findFirstBySoBb(String soBb);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBBKetThucNKHdrDTO(" +
            "bbkt.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKho,khdcd.tenNhaKho, khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho,khdcd.loaiVthh,khdcd.tenLoaiVthh, khdcd.cloaiVthh, khdcd.tenCloaiVthh,khdcd.donViTinh, khdcd.tenDonViTinh ," +
            "bbkt.soBb, bbkt.ngayKetThucNhap, hskt.soHoSoKyThuat,hskt.id, bbktd.soPhieuNhapKho, bbktd.phieuNhapKhoId,bbktd.ngayNhap, bblm.soBbLayMau,bblm.id," +
            "bblm.soBbLayMau,bblm.id, bbkt.trangThai, bbkt.trangThai) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN HhNkBBKetThucNKHdr bbkt On bbkt.qdPdNkId = qdc.id and bbkt.maLoKho = khdcd.maLoKhoNhan and bbkt.maNganKho = khdcd.maNganKhoNhan " +
            "LEFT JOIN HhNkBBKetThucNKDtl bbktd On bbktd.hdrId = bbkt.id " +
            "LEFT JOIN DcnbHoSoKyThuatHdr hskt On hskt.idQdnh = qdc.id  and bbkt.maLoKho = hskt.maLoKho and bbkt.maNganKho = hskt.maNganKho " +
            "LEFT JOIN DcnbBienBanLayMauHdr bblm On bblm.qdccId = qdc.id and bbkt.maLoKho = bblm.maLoKho and bbkt.maNganKho = bblm.maNganKho " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.parentId is not null and qdc.trangThai = '29'" +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND (:#{#param.soBb} IS NULL OR LOWER(bbkt.soBb) LIKE CONCAT('%',LOWER(:#{#param.soBb}),'%')) " +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND ((:#{#param.tuNgayKtnk}  IS NULL OR bbkt.ngayKetThucNhap >= :#{#param.tuNgayKtnk})" +
            "AND (:#{#param.denNgayKtnk}  IS NULL OR bbkt.ngayKetThucNhap <= :#{#param.denNgayKtnk}) ) " +
            "AND ((:#{#param.tuNgayThoiHanNhap}  IS NULL OR khdcd.thoiGianDkDc >= :#{#param.tuNgayThoiHanNhap})" +
            "AND (:#{#param.denNgayThoiHanNhap}  IS NULL OR khdcd.thoiGianDkDc <= :#{#param.denNgayThoiHanNhap}) ) " +
            "GROUP BY bbkt.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKho,khdcd.tenNhaKho, khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho,khdcd.loaiVthh,khdcd.tenLoaiVthh, khdcd.cloaiVthh, khdcd.tenCloaiVthh,khdcd.donViTinh, khdcd.tenDonViTinh ," +
            "bbkt.soBb, bbkt.ngayKetThucNhap, hskt.soHoSoKyThuat,hskt.id, bbktd.soPhieuNhapKho, bbktd.phieuNhapKhoId,bbktd.ngayNhap, bblm.soBbLayMau,bblm.id," +
            "bblm.soBbLayMau,bblm.id, bbkt.trangThai, bbkt.trangThai")
    Page<HhNkBBKetThucNKHdrDTO> searchPage(@Param("param") HhNkBBKetThucNKReq req, Pageable pageable);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBBKetThucNKHdrListDTO(" +
            "bbkt.id,qdc.id,bbkt.soBb) " +
            "FROM HhNkBBKetThucNKHdr bbkt " +
            "LEFT JOIN DcnbQuyetDinhDcCHdr qdc On bbkt.qdPdNkId = qdc.id " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29'" +
            "AND ((:#{#param.qDinhDccId} IS NULL OR qdc.id = :#{#param.qDinhDccId})) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "ORDER BY bbkt.soBb desc, bbkt.nam desc")
    List<HhNkBBKetThucNKHdrListDTO> searchList(@Param("param") HhNkBBKetThucNKReq req);
}
