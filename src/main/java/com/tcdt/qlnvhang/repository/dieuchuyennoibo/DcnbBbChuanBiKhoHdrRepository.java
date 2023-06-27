package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbChuanBiKhoHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchBangKeCanHang;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbChuanBiKhoHdrDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeCanHangHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbChuanBiKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbBbChuanBiKhoHdrRepository extends JpaRepository<DcnbBbChuanBiKhoHdr, Long> {

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
    Page<DcnbBbChuanBiKhoHdr> search(@Param("param") SearchBangKeCanHang req, Pageable pageable);

    Optional<DcnbBbChuanBiKhoHdr> findBySoBban(String soBban);


    List<DcnbBbChuanBiKhoHdr> findByIdIn(List<Long> ids);

    List<DcnbBbChuanBiKhoHdr> findAllByIdIn(List<Long> idList);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbChuanBiKhoHdrDTO(" +
            "bbcb.id, qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc, bbcb.soBban, bbcb.ngayLap, pnk.id, pnk.soPhieuNhapKho, bbktnk.id," +
            " bbktnk.soBb,bbktnk.ngayKetThucNhap,khdcd.maDiemKho, khdcd.tenDiemKho, khdcd.maLoKho, " +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho, bbcb.trangThai, bbcb.trangThai) FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbBbChuanBiKhoHdr bbcb On bbcb.qdDcCucId = qdc.id " +
            "LEFT JOIN DcnbPhieuNhapKhoHdr pnk On pnk.id = bbcb.idPhieuNhapKho " +
            "LEFT JOIN DcnbBBKetThucNKHdr bbktnk On bbktnk.qDinhDccId = qdc.id " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29'" +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}) OR (:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "ORDER BY bbcb.soQdDcCuc desc, bbcb.nam desc")
    Page<DcnbBbChuanBiKhoHdrDTO> searchPageChiCuc(@Param("param")DcnbBbChuanBiKhoHdrReq req, Pageable pageable);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbChuanBiKhoHdrDTO(" +
            "bbcb.id, qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc, bbcb.soBban, bbcb.ngayLap, pnk.id, pnk.soPhieuNhapKho, bbktnk.id," +
            " bbktnk.soBb,bbktnk.ngayKetThucNhap,khdcd.maDiemKho, khdcd.tenDiemKho, khdcd.maLoKho, " +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho, bbcb.trangThai, bbcb.trangThai) FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbDataLinkHdr dtlh On dtlh.qdCcParentId = qdc.id " +
            "LEFT JOIN DcnbBbChuanBiKhoHdr bbcb On bbcb.qdDcCucId = dtlh.qdCcId " +
            "LEFT JOIN DcnbPhieuNhapKhoHdr pnk On pnk.id = bbcb.idPhieuNhapKho " +
            "LEFT JOIN DcnbBBKetThucNKHdr bbktnk On bbktnk.qDinhDccId = dtlh.qdCcId " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = dtlh.qdCcId " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29'" +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}) OR (:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "ORDER BY bbcb.soQdDcCuc desc, bbcb.nam desc")
    Page<DcnbBbChuanBiKhoHdrDTO> searchPage(@Param("param")DcnbBbChuanBiKhoHdrReq req, Pageable pageable);
}
