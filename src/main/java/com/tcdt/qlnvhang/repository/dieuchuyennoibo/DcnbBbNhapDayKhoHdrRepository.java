package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbNhapDayKhoHdrReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbNhapDayKhoHdrDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbNhapDayKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbBbNhapDayKhoHdrRepository extends JpaRepository<DcnbBbNhapDayKhoHdr, Long> {

    @Query(value = "SELECT distinct c FROM DcnbBangKeCanHangHdr c LEFT JOIN QlnvDmDonvi dvi ON dvi.maDvi = c.maDvi WHERE 1=1 " +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(c.soBangKe) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
            "AND ((:#{#param.maDvi} IS NULL OR c.maDvi = :#{#param.maDvi}) " +
            "OR (:#{#param.maDvi} IS NULL OR dvi.parent.maDvi = :#{#param.maDvi}))" +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR c.ngayXuatKho >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR c.ngayXuatKho <= :#{#param.denNgay}) ) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(c.soQdinhDcc) LIKE CONCAT('%',LOWER(:#{#param.soBbLayMau}),'%')) " +
            "AND (:#{#param.loaiDc} IS NULL OR c.loaiDc = :#{#param.loaiDc}) " +
            "ORDER BY c.soQdinhDcc desc , c.nam desc, c.id desc")
    Page<DcnbBbNhapDayKhoHdr> search(@Param("param") DcnbBbNhapDayKhoHdrReq req, Pageable pageable);

    Optional<DcnbBbNhapDayKhoHdr> findBySoBb(String soBb);


    List<DcnbBbNhapDayKhoHdr> findByIdIn(List<Long> ids);

    List<DcnbBbNhapDayKhoHdr> findAllByIdIn(List<Long> idList);

    @Query(value = "SELECT distinct c FROM DcnbBbNhapDayKhoHdr c " +
            "WHERE 1=1 " +
            "AND c.trangThai = '17' " +
            "AND (:#{#param.maDvi} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDvi},'%')) " +
            "ORDER BY c.soQdDcCuc desc , c.nam desc, c.id desc")
    List<DcnbBbNhapDayKhoHdr> searchList(@Param("param") DcnbBbNhapDayKhoHdrReq param);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbNhapDayKhoHdrDTO(" +
            "bbndk.id,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKhoNhan,khdcd.tenNhaKhoNhan, khdcd.maDiemKhoNhan,khdcd.tenDiemKhoNhan,khdcd.maLoKhoNhan," +
            "khdcd.tenLoKhoNhan,khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan,khdcd.loaiVthh,khdcd.tenLoaiVthh, khdcd.cloaiVthh, khdcd.tenCloaiVthh,khdcd.soLuongDc ,khdcd.donViTinh, khdcd.tenDonViTinh ," +
            "bbndk.soBb, bbndk.ngayBdNhap, bbndk.ngayKtNhap,bbndkd.soPhieuKiemTraCl,bbndkd.phieuKiemTraClId, bbndkd.phieuNhapKho, bbndkd.idPhieuNhapKho,bbndkd.soBangKeCh,bbndkd.idBangKeCh," +
            "bbndk.ngayLap, bbndk.trangThai,  bbndk.trangThai) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbBbNhapDayKhoHdr bbndk On bbndk.qdDcCucId = qdc.id " +
            "and ((khdcd.maLoKhoNhan is not null and khdcd.maNganKhoNhan = bbndk.maNganKho and khdcd.maLoKhoNhan = bbndk.maLoKho ) or (khdcd.maLoKhoNhan is null and khdcd.maNganKhoNhan = bbndk.maNganKho ))" +
            "LEFT JOIN DcnbBbNhapDayKhoDtl bbndkd On bbndkd.hdrId = bbndk.id " +
            "LEFT JOIN DcnbPhieuNhapKhoHdr pnk On pnk.id = bbndkd.idPhieuNhapKho " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.parentId is not null and qdc.trangThai = '29'" +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND ((:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho})) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND (:#{#param.soQdDcCuc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdDcCuc}),'%')) " +
            "AND ((:#{#param.loaiDc} IS NULL OR qdc.loaiDc = :#{#param.loaiDc}))" +
            "AND (qdc.loaiDc= 'DCNB' OR  ((:#{#param.typeQd} IS NULL OR qdc.type = :#{#param.typeQd})))" +
            "AND (:#{#param.soBb} IS NULL OR LOWER(bbndk.soBb) LIKE CONCAT('%',LOWER(:#{#param.soBb}),'%')) " +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.trangThai} IS NULL OR bbndk.trangThai = :#{#param.trangThai}) " +
            "AND ((:#{#param.tuNgayBdNhap}  IS NULL OR bbndk.ngayBdNhap >= :#{#param.tuNgayBdNhap})" +
            "AND (:#{#param.denNgayBdNhap}  IS NULL OR bbndk.ngayBdNhap <= :#{#param.denNgayBdNhap}) ) " +
            "AND ((:#{#param.tuNgayKtNhap}  IS NULL OR bbndk.ngayKtNhap >= :#{#param.tuNgayKtNhap})" +
            "AND (:#{#param.denNgayKtNhap}  IS NULL OR bbndk.ngayKtNhap <= :#{#param.denNgayKtNhap}) ) " +
            "AND ((:#{#param.tuNgayThoiHanNh}  IS NULL OR khdcd.thoiGianDkDc >= :#{#param.tuNgayThoiHanNh})" +
            "AND (:#{#param.denNgayThoiHanNh}  IS NULL OR khdcd.thoiGianDkDc <= :#{#param.denNgayThoiHanNh}) ) " +
            "GROUP BY bbndk.id,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKhoNhan,khdcd.tenNhaKhoNhan, khdcd.maDiemKhoNhan,khdcd.tenDiemKhoNhan,khdcd.maLoKhoNhan," +
            "khdcd.tenLoKhoNhan,khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan,khdcd.loaiVthh,khdcd.tenLoaiVthh, khdcd.cloaiVthh, khdcd.tenCloaiVthh,khdcd.soLuongDc ,khdcd.donViTinh, khdcd.tenDonViTinh ," +
            "bbndk.soBb, bbndk.ngayBdNhap, bbndk.ngayKtNhap,bbndkd.soPhieuKiemTraCl,bbndkd.phieuKiemTraClId, bbndkd.phieuNhapKho, bbndkd.idPhieuNhapKho,bbndkd.soBangKeCh,bbndkd.idBangKeCh," +
            "bbndk.ngayLap, bbndk.trangThai,  bbndk.trangThai "+
            "ORDER BY qdc.soQdinh DESC")
    Page<DcnbBbNhapDayKhoHdrDTO> searchPage(@Param("param") DcnbBbNhapDayKhoHdrReq req, Pageable pageable);

}
