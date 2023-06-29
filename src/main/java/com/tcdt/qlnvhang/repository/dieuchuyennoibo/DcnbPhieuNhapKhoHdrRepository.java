package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbPhieuNhapKhoHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchBangKeCanHang;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuNhapKhoHdrDTO;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuNhapKhoHdrListDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeCanHangHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbPhieuNhapKhoHdrRepository extends JpaRepository<DcnbPhieuNhapKhoHdr, Long> {

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
    Page<DcnbPhieuNhapKhoHdr> search(@Param("param") SearchBangKeCanHang req, Pageable pageable);

    Optional<DcnbPhieuNhapKhoHdr> findBySoPhieuNhapKho(String soPhieuNhapKho);


    List<DcnbPhieuNhapKhoHdr> findByIdIn(List<Long> ids);

    List<DcnbPhieuNhapKhoHdr> findAllByIdIn(List<Long> idList);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuNhapKhoHdrDTO(" +
            "pnk.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKho,khdcd.tenNhaKho, khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho, khdcd.thayDoiThuKho,pnk.soPhieuNhapKho, pnk.ngayLap,  bknvt.id, bknvt.soBangKe," +
            "bknvt.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh, khdcd.tenCloaiVthh,khdcd.donViTinh, khdcd.tenDonViTinh,khdcd.soLuongDc,khdcd.duToanKphi," +
            "pnk.idPhieuKtraCluong, pnk.soPhieuKtraCluong, pktcl.ngayGiamDinh, " +
            "pnk.trangThai, pnk.trangThai) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbPhieuNhapKhoHdr pnk On pnk.qdDcCucId = qdc.id " +
            "LEFT JOIN DcnbPhieuKtChatLuongHdr pktcl On pktcl.id = pnk.idPhieuKtraCluong " +
            "LEFT JOIN DcnbBangKeNhapVTHdr bknvt On bknvt.phieuNhapKhoId = pnk.id " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29'" +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "ORDER BY pnk.soPhieuNhapKho desc, pnk.nam desc")
    Page<DcnbPhieuNhapKhoHdrDTO> searchPageChiCuc(@Param("param")DcnbPhieuNhapKhoHdrReq req, Pageable pageable);
    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuNhapKhoHdrDTO(" +
            "pnk.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKho,khdcd.tenNhaKho, khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho, khdcd.thayDoiThuKho,pnk.soPhieuNhapKho, pnk.ngayLap,  bknvt.id, bknvt.soBangKe," +
            "bknvt.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh, khdcd.tenCloaiVthh,khdcd.donViTinh, khdcd.tenDonViTinh,khdcd.soLuongDc,khdcd.duToanKphi," +
            "pnk.idPhieuKtraCluong, pnk.soPhieuKtraCluong, pktcl.ngayGiamDinh, " +
            "pnk.trangThai, pnk.trangThai) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbDataLinkHdr dtlh On dtlh.qdCcParentId = qdc.id " +
            "LEFT JOIN DcnbPhieuNhapKhoHdr pnk On pnk.qdDcCucId = dtlh.qdCcId " +
            "LEFT JOIN DcnbPhieuKtChatLuongHdr pktcl On pktcl.id = pnk.idPhieuKtraCluong " +
            "LEFT JOIN DcnbBangKeNhapVTHdr bknvt On bknvt.phieuNhapKhoId = pnk.id " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = dtlh.qdCcId " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29' " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "ORDER BY pnk.soPhieuNhapKho desc, pnk.nam desc")
    Page<DcnbPhieuNhapKhoHdrDTO> searchPage(@Param("param")DcnbPhieuNhapKhoHdrReq req, Pageable pageable);
    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuNhapKhoHdrListDTO(" +
            "pnk.id,pnk.soPhieuNhapKho,pnk.ngayLap) " +
            "FROM DcnbPhieuNhapKhoHdr pnk " +
            "WHERE 1 =1 " +
            "AND ((:#{#param.qdDcCucId} IS NULL OR pnk.qdDcCucId = :#{#param.qdDcCucId}))"+
            "ORDER BY pnk.soPhieuNhapKho desc, pnk.nam desc")
    List<DcnbPhieuNhapKhoHdrListDTO> searchList(@Param("param")DcnbPhieuNhapKhoHdrReq objReq);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuNhapKhoHdrListDTO(" +
            "pnk.id,pnk.soPhieuNhapKho,pnk.ngayLap,pnk.tongSoLuong, pktcl.id, pktcl.soPhieu,bkchh.id, bkchh.soBangKe,bknvt.id, bknvt.soBangKe ) " +
            "FROM DcnbPhieuNhapKhoHdr pnk " +
            "LEFT JOIN DcnbPhieuKtChatLuongHdr pktcl On pktcl.id = pnk.idPhieuKtraCluong " +
            "LEFT JOIN DcnbBangKeCanHangHdr bkchh On bkchh.phieuNhapKhoId = pnk.id " +
            "LEFT JOIN DcnbBangKeNhapVTHdr bknvt On bknvt.phieuNhapKhoId = pnk.id " +
            "WHERE 1 =1 " +
            "AND ((:#{#param.qdDcCucId} IS NULL OR pnk.qdDcCucId = :#{#param.qdDcCucId}))"+
            "ORDER BY pnk.soPhieuNhapKho desc, pnk.nam desc")
    List<DcnbPhieuNhapKhoHdrListDTO> searchListChung(@Param("param")DcnbPhieuNhapKhoHdrReq objReq);
}
