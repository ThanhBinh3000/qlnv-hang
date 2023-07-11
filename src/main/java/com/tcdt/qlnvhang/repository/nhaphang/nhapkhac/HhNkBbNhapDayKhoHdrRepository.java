package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBbNhapDayKhoHdr;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbNhapDayKhoHdrReq;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBbNhapDayKhoHdrDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HhNkBbNhapDayKhoHdrRepository extends JpaRepository<HhNkBbNhapDayKhoHdr, Long> {

    @Query(value = "SELECT distinct c FROM HhNkBbNhapDayKhoHdr c LEFT JOIN QlnvDmDonvi dvi ON dvi.maDvi = c.maDvi WHERE 1=1 " +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(c.soBb) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
            "AND ((:#{#param.maDvi} IS NULL OR c.maDvi LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "ORDER BY c.soBb desc , c.nam desc, c.id desc")
    Page<HhNkBbNhapDayKhoHdr> search(@Param("param") DcnbBbNhapDayKhoHdrReq req, Pageable pageable);

    Optional<HhNkBbNhapDayKhoHdr> findBySoBb(String soBb);


    List<HhNkBbNhapDayKhoHdr> findByIdIn(List<Long> ids);

    List<HhNkBbNhapDayKhoHdr> findAllByIdIn(List<Long> idList);

    @Query(value = "SELECT distinct c FROM HhNkBbNhapDayKhoHdr c " +
            "WHERE 1=1 " +
            "AND c.trangThai = '17' " +
            "AND (:#{#param.maDvi} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.maDvi},'%')) " +
            "ORDER BY c.soBb desc , c.nam desc, c.id desc")
    List<HhNkBbNhapDayKhoHdrDTO> searchList(@Param("param") DcnbBbNhapDayKhoHdrReq param);

//    @Query(value = "SELECT new com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBbNhapDayKhoHdrDTO(" +
//            "bbndk.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKho,khdcd.tenNhaKho, khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
//            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho,khdcd.loaiVthh,khdcd.tenLoaiVthh, khdcd.cloaiVthh, khdcd.tenCloaiVthh,khdcd.donViTinh, khdcd.tenDonViTinh ," +
//            "bbndk.soBb, bbndk.ngayBdNhap, bbndk.ngayKtNhap,bbndkd.phieuKtCluong,bbndkd.idPhieuKtCluong, bbndkd.phieuNhapKho, bbndkd.idPhieuNhapKho,bbndkd.soBangKeCh,bbndkd.idBangKeCh," +
//            "bbndk.ngayLap, bbndk.trangThai, bbndk.trangThai) " +
//            "FROM DcnbQuyetDinhDcCHdr qdc " +
//            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
//            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
//            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
//            "LEFT JOIN HhNkBbNhapDayKhoHdr bbndk On bbndk.qdPdNkId = qdc.id and khdcd.maNganKhoNhan = bbndk.maNganKho and khdcd.maLoKhoNhan = bbndk.maLoKho " +
//            "LEFT JOIN DcnbBbNhapDayKhoDtl bbndkd On bbndkd.hdrId = bbndk.id " +
//            "LEFT JOIN DcnbPhieuNhapKhoHdr pnk On pnk.id = bbndkd.idPhieuNhapKho " +
//            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
//            "WHERE 1 =1 " +
//            "AND qdc.parentId is not null and qdc.trangThai = '29'" +
//            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
//            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
//            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
//            "AND (:#{#param.soQdDcCuc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdDcCuc}),'%')) " +
//            "AND (:#{#param.soBb} IS NULL OR LOWER(bbndk.soBb) LIKE CONCAT('%',LOWER(:#{#param.soBb}),'%')) " +
//            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
//            "AND ((:#{#param.tuNgayBdNhap}  IS NULL OR bbndk.ngayBdNhap >= :#{#param.tuNgayBdNhap})" +
//            "AND (:#{#param.denNgayBdNhap}  IS NULL OR bbndk.ngayBdNhap <= :#{#param.denNgayBdNhap}) ) " +
//            "AND ((:#{#param.tuNgayKtNhap}  IS NULL OR bbndk.ngayKtNhap >= :#{#param.tuNgayKtNhap})" +
//            "AND (:#{#param.denNgayKtNhap}  IS NULL OR bbndk.ngayKtNhap <= :#{#param.denNgayKtNhap}) ) " +
//            "AND ((:#{#param.tuNgayThoiHanNh}  IS NULL OR khdcd.thoiGianDkDc >= :#{#param.tuNgayThoiHanNh})" +
//            "AND (:#{#param.denNgayThoiHanNh}  IS NULL OR khdcd.thoiGianDkDc <= :#{#param.denNgayThoiHanNh}) ) " +
//            "GROUP BY bbndk.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKho,khdcd.tenNhaKho, khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
//            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho,khdcd.loaiVthh,khdcd.tenLoaiVthh, khdcd.cloaiVthh, khdcd.tenCloaiVthh,khdcd.donViTinh, khdcd.tenDonViTinh ," +
//            "bbndk.soBb, bbndk.ngayBdNhap, bbndk.ngayKtNhap,bbndkd.phieuKtCluong,bbndkd.idPhieuKtCluong, bbndkd.phieuNhapKho, bbndkd.idPhieuNhapKho,bbndkd.soBangKeCh,bbndkd.idBangKeCh," +
//            "bbndk.ngayLap, bbndk.trangThai, bbndk.trangThai")
//    Page<HhNkBbNhapDayKhoHdrDTO> searchPage(@Param("param") DcnbBbNhapDayKhoHdrReq req, Pageable pageable);

}
