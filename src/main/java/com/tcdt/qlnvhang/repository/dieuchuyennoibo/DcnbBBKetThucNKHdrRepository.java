package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBBKetThucNKReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBBKetThucNKHdrDTO;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBBKetThucNKHdrListDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBKetThucNKHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbBBKetThucNKHdrRepository extends JpaRepository<DcnbBBKetThucNKHdr, Long> {
    Optional<DcnbBBKetThucNKHdr> findFirstBySoBb(String soBb);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBBKetThucNKHdrDTO(" +
            "bbkt.id,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKhoNhan,khdcd.tenNhaKhoNhan, khdcd.maDiemKhoNhan,khdcd.tenDiemKhoNhan,khdcd.maLoKhoNhan," +
            "khdcd.tenLoKhoNhan,khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan,khdcd.loaiVthh,khdcd.tenLoaiVthh, khdcd.cloaiVthh, khdcd.tenCloaiVthh,khdcd.soLuongDc,khdcd.donViTinh, " +
            "bbkt.soBb, bbkt.ngayKetThucNhap, hskt.soHskt,hskt.id, bbktd.soPhieuNhapKho, bbktd.phieuNhapKhoId,bbktd.ngayNhap, bblm.soBbLayMau,bblm.id," +
            "bblm.soBbLayMau,bblm.id, bbkt.trangThai, bbkt.trangThai) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbBBKetThucNKHdr bbkt On bbkt.qDinhDccId = qdc.id " +
            "and ((khdcd.maLoKhoNhan is not null and  bbkt.maLoKho = khdcd.maLoKhoNhan and bbkt.maNganKho = khdcd.maNganKhoNhan ) or (khdcd.maLoKhoNhan is null and bbkt.maNganKho = khdcd.maNganKhoNhan ))" +
            "LEFT JOIN DcnbBBKetThucNKDtl bbktd On bbktd.hdrId = bbkt.id " +
            "LEFT JOIN XhHoSoKyThuatHdr hskt On 1 = 1  " +
            "and ((bbkt.maLoKho is not null and  bbkt.maLoKho = SUBSTRING(hskt.maDiaDiem, 1, 17) and bbkt.maNganKho = SUBSTRING(hskt.maDiaDiem, 1, 15) ) or (bbkt.maLoKho is null and bbkt.maNganKho = SUBSTRING(hskt.maDiaDiem, 1, 15) ))" +
            "LEFT JOIN DcnbBienBanLayMauHdr bblm On bblm.qdccId = qdc.id " +
            "and ((bbkt.maLoKho is not null and  bbkt.maLoKho = bblm.maLoKho and bbkt.maNganKho = bblm.maNganKho ) or (bbkt.maLoKho is null and bbkt.maNganKho = bblm.maNganKho ))" +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.parentId is not null and qdc.trangThai = '29'" +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND ((:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho})) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.maDvi} IS NULL OR LOWER(qdc.maDvi) LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.loaiDc} IS NULL OR qdc.loaiDc = :#{#param.loaiDc}))" +
            "AND (qdc.loaiDc= 'DCNB' OR  ((:#{#param.typeQd} IS NULL OR qdc.type = :#{#param.typeQd})))" +
            "AND (:#{#param.soBb} IS NULL OR LOWER(bbkt.soBb) LIKE CONCAT('%',LOWER(:#{#param.soBb}),'%')) " +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.trangThai} IS NULL OR bbkt.trangThai = :#{#param.trangThai}) " +
            "AND ((:#{#param.tuNgayKtnk}  IS NULL OR bbkt.ngayKetThucNhap >= :#{#param.tuNgayKtnk})" +
            "AND (:#{#param.denNgayKtnk}  IS NULL OR bbkt.ngayKetThucNhap <= :#{#param.denNgayKtnk}) ) " +
            "AND ((:#{#param.tuNgayThoiHanNhap}  IS NULL OR khdcd.thoiGianDkDc >= :#{#param.tuNgayThoiHanNhap})" +
            "AND (:#{#param.denNgayThoiHanNhap}  IS NULL OR khdcd.thoiGianDkDc <= :#{#param.denNgayThoiHanNhap}) ) " +
            "GROUP BY bbkt.id,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKhoNhan,khdcd.tenNhaKhoNhan, khdcd.maDiemKhoNhan,khdcd.tenDiemKhoNhan,khdcd.maLoKhoNhan," +
            "khdcd.tenLoKhoNhan,khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan,khdcd.loaiVthh,khdcd.tenLoaiVthh, khdcd.cloaiVthh, khdcd.tenCloaiVthh,khdcd.soLuongDc,khdcd.donViTinh, " +
            "bbkt.soBb, bbkt.ngayKetThucNhap, hskt.soHskt,hskt.id, bbktd.soPhieuNhapKho, bbktd.phieuNhapKhoId,bbktd.ngayNhap, bblm.soBbLayMau,bblm.id," +
            "bblm.soBbLayMau,bblm.id, bbkt.trangThai, bbkt.trangThai "+
            "ORDER BY qdc.soQdinh DESC")
    Page<DcnbBBKetThucNKHdrDTO> searchPage(@Param("param") DcnbBBKetThucNKReq req, Pageable pageable);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBBKetThucNKHdrListDTO(" +
            "bbkt.id,qdc.id,bbkt.soBb) " +
            "FROM DcnbBBKetThucNKHdr bbkt " +
            "LEFT JOIN DcnbQuyetDinhDcCHdr qdc On bbkt.qDinhDccId = qdc.id " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29'" +
            "AND ((:#{#param.qDinhDccId} IS NULL OR qdc.id = :#{#param.qDinhDccId})) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.maDvi} IS NULL OR LOWER(qdc.maDvi) LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND ((:#{#param.loaiDc} IS NULL OR qdc.loaiDc = :#{#param.loaiDc}))" +
            "AND (qdc.loaiDc= 'DCNB' OR  ((:#{#param.typeQd} IS NULL OR qdc.type = :#{#param.typeQd})))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "ORDER BY bbkt.soBb desc, bbkt.nam desc")
    List<DcnbBBKetThucNKHdrListDTO> searchList(@Param("param") DcnbBBKetThucNKReq req);
}
