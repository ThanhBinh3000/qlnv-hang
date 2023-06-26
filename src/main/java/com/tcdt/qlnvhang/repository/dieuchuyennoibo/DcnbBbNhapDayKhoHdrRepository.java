package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbNhapDayKhoHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchBangKeCanHang;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbNhapDayKhoHdrDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeCanHangHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbNhapDayKhoHdr;
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
    List<DcnbBbNhapDayKhoHdrDTO> searchList(@Param("param")DcnbBbNhapDayKhoHdrReq param);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbNhapDayKhoHdrDTO(" +
            "bbndk.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKho,khdcd.tenNhaKho, khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho,khdcd.loaiVthh,khdcd.tenLoaiVthh, khdcd.cloaiVthh, khdcd.tenCloaiVthh,khdcd.donViTinh, khdcd.tenDonViTinh ," +
            "bbndk.soBb, bbndk.ngayBdNhap, bbndk.ngayKtNhap,bbndkd.phieuKnghiemCluong,bbndkd.idPhieuKnghiemCluong, bbndkd.phieuNhapKho, bbndkd.idPhieuNhapKho,bbndkd.soBangKeCh,bbndkd.idBangKeCh," +
            "bbndk.ngayLap, pnk.trangThai, pnk.trangThai) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbBbNhapDayKhoHdr bbndk On bbndk.qdDcCucId = qdc.id " +
            "LEFT JOIN DcnbBbNhapDayKhoDtl bbndkd On bbndkd.hdrId = bbndk.id " +
            "LEFT JOIN DcnbPhieuNhapKhoHdr pnk On pnk.id = bbndkd.idPhieuNhapKho " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29'" +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "ORDER BY pnk.soPhieuNhapKho desc, pnk.nam desc")
    Page<DcnbBbNhapDayKhoHdrDTO> searchPageChiCuc(@Param("param") DcnbBbNhapDayKhoHdrReq req, Pageable pageable);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbNhapDayKhoHdrDTO(" +
            "bbndk.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKho,khdcd.tenNhaKho, khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho,khdcd.loaiVthh,khdcd.tenLoaiVthh, khdcd.cloaiVthh, khdcd.tenCloaiVthh,khdcd.donViTinh, khdcd.tenDonViTinh ," +
            "bbndk.soBb, bbndk.ngayBdNhap, bbndk.ngayKtNhap,bbndkd.phieuKnghiemCluong,bbndkd.idPhieuKnghiemCluong, bbndkd.phieuNhapKho, bbndkd.idPhieuNhapKho,bbndkd.soBangKeCh,bbndkd.idBangKeCh," +
            "bbndk.ngayLap, pnk.trangThai, pnk.trangThai) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbDataLinkHdr dtlh On dtlh.qdCcParentId = qdc.id " +
            "LEFT JOIN DcnbBbNhapDayKhoHdr bbndk On bbndk.qdDcCucId = dtlh.qdCcId " +
            "LEFT JOIN DcnbBbNhapDayKhoDtl bbndkd On bbndkd.hdrId = bbndk.id " +
            "LEFT JOIN DcnbPhieuNhapKhoHdr pnk On pnk.id = bbndkd.idPhieuNhapKho " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29' " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "ORDER BY pnk.soPhieuNhapKho desc, pnk.nam desc")
    Page<DcnbBbNhapDayKhoHdrDTO> searchPage(@Param("param") DcnbBbNhapDayKhoHdrReq req, Pageable pageable);
}
