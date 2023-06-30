package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchPhieuXuatKho;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuXuatKhoHdrDTO;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuXuatKhoHdrListDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuXuatKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbPhieuXuatKhoHdrRepository extends JpaRepository<DcnbPhieuXuatKhoHdr, Long> {
    Optional<DcnbPhieuXuatKhoHdr> findBySoPhieuXuatKho(String soPhieuXuatKho);

    List<DcnbPhieuXuatKhoHdr> findByIdIn(List<Long> ids);
    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuXuatKhoHdrDTO(" +
            "pxk.id,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKho,khdcd.tenNhaKho, khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho, khdcd.thayDoiThuKho,pxk.soPhieuXuatKho,pxk.ngayXuatKho, pkncl.id,pkncl.soPhieu,pkncl.ngayKiem," +
            "khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh, khdcd.tenCloaiVthh,pkncl.nguoiKt,khdcd.donViTinh,khdcd.soLuongDc,khdcd.duToanKphi," +
            "bkchh.id, bkchh.soBangKe," +
            "pxk.trangThai, pxk.trangThai) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbPhieuXuatKhoHdr pxk On pxk.qddcId = qdc.id " +
            "LEFT JOIN DcnbPhieuKnChatLuongHdr pkncl On pkncl.id = pxk.phieuKnChatLuongHdrId " +
            "LEFT JOIN DcnbBangKeCanHangHdr bkchh On bkchh.phieuXuatKhoId = pxk.id " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "WHERE 1 =1 "+
            "AND qdc.trangThai = '29' AND qdc.loaiDc = :#{#param.loaiDc} "+
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) "+
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))"+
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "ORDER BY pxk.soPhieuXuatKho desc, pxk.nam desc")
    Page<DcnbPhieuXuatKhoHdrDTO> searchPageChiCuc(@Param("param")SearchPhieuXuatKho req, Pageable pageable);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuXuatKhoHdrDTO(" +
            "pxk.id,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKho,khdcd.tenNhaKho, khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho, khdcd.thayDoiThuKho,pxk.soPhieuXuatKho,pxk.ngayXuatKho, pkncl.id,pkncl.soPhieu,pkncl.ngayKiem," +
            "khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh, khdcd.tenCloaiVthh,pkncl.nguoiKt,khdcd.donViTinh,khdcd.soLuongDc,khdcd.duToanKphi," +
            "bkchh.id, bkchh.soBangKe," +
            "pxk.trangThai, pxk.trangThai) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbDataLinkHdr dtlh On dtlh.qdCcId = qdc.id " +
            "LEFT JOIN DcnbPhieuXuatKhoHdr pxk On pxk.qddcId = qdc.id " +
            "LEFT JOIN DcnbPhieuKnChatLuongHdr pkncl On pkncl.id = pxk.phieuKnChatLuongHdrId " +
            "LEFT JOIN DcnbBangKeCanHangHdr bkchh On bkchh.phieuXuatKhoId = pxk.id " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "WHERE 1 =1 "+
            "AND qdc.trangThai = '29' AND qdc.loaiDc = :#{#param.loaiDc} "+
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) "+
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))"+
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "ORDER BY pxk.soPhieuXuatKho desc, pxk.nam desc")
    Page<DcnbPhieuXuatKhoHdrDTO> searchPageCuc(@Param("param")SearchPhieuXuatKho req, Pageable pageable);
    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuXuatKhoHdrListDTO(" +
            "pxk.id,pxk.soPhieuXuatKho,pxk.ngayTaoPhieu) " +
            "FROM DcnbPhieuXuatKhoHdr pxk " +
            "WHERE 1 =1 " +
            "AND ((:#{#param.qdinhDccId} IS NULL OR pxk.qddcId = :#{#param.qdinhDccId}))"+
            "AND ((:#{#param.maLoKho} IS NULL OR pxk.maLoKho = :#{#param.maLoKho}))" +
            "AND ((:#{#param.maNganKho} IS NULL OR pxk.maNganKho = :#{#param.maNganKho}))" +
            "ORDER BY pxk.soPhieuXuatKho desc, pxk.nam desc")
    List<DcnbPhieuXuatKhoHdrListDTO> searchList(@Param("param")SearchPhieuXuatKho req);
    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuXuatKhoHdrListDTO(" +
            "pxk.id,pxk.soPhieuXuatKho,pxk.ngayTaoPhieu, pxk.tongSoLuong, pkncl.id, pkncl.soPhieu,bkchh.id, bkchh.soBangKe,bkxvth.id, bkxvth.soBangKe ) " +
            "FROM DcnbPhieuXuatKhoHdr pxk " +
            "LEFT JOIN DcnbPhieuKnChatLuongHdr pkncl On pkncl.id = pxk.phieuKnChatLuongHdrId " +
            "LEFT JOIN DcnbBangKeXuatVTHdr bkxvth On bkxvth.phieuXuatKhoId = pxk.id " +
            "LEFT JOIN DcnbBangKeCanHangHdr bkchh On bkchh.phieuXuatKhoId = pxk.id " +
            "WHERE 1 =1 " +
            "AND ((:#{#param.qdinhDccId} IS NULL OR pxk.qddcId = :#{#param.qdinhDccId}))" +
            "AND ((:#{#param.maLoKho} IS NULL OR pxk.maLoKho = :#{#param.maLoKho}))" +
            "AND ((:#{#param.maNganKho} IS NULL OR pxk.maNganKho = :#{#param.maNganKho}))" +
            "ORDER BY pxk.soPhieuXuatKho desc, pxk.nam desc")
    List<DcnbPhieuXuatKhoHdrListDTO> searchListChung(@Param("param")SearchPhieuXuatKho req);
}
