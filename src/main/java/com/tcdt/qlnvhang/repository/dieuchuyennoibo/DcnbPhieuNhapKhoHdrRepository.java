package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbPhieuNhapKhoHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchBangKeCanHang;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuNhapKhoHdrDTO;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuNhapKhoHdrListDTO;
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
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
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
            "pnk.id,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKhoNhan,khdcd.tenNhaKhoNhan, khdcd.maDiemKhoNhan,khdcd.tenDiemKhoNhan,khdcd.maLoKhoNhan," +
            "khdcd.tenLoKhoNhan,khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan, khdcd.thayDoiThuKho,pnk.soPhieuNhapKho, pnk.ngayLap,  pnk.bangKeVtId, pnk.soBangKeVt," +
            "khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh, khdcd.tenCloaiVthh,khdcd.donViTinh,khdcd.soLuongDc,khdcd.duToanKphi," +
            "pnk.idPhieuKtraCluong, pnk.soPhieuKtraCluong, pktcl.ngayGiamDinh, " +
            "bkch.id, bkch.soBangKe, pnk.bbNghiemThuBqld, " +
            "pnk.trangThai, pnk.trangThai,khdcd.id) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbPhieuNhapKhoHdr pnk On pnk.keHoachDcDtlId = khdcd.id " +
            "LEFT JOIN DcnbPhieuKtChatLuongHdr pktcl On pktcl.id = pnk.idPhieuKtraCluong " +
            "LEFT JOIN DcnbBangKeCanHangHdr bkch On bkch.phieuNhapKhoId = pnk.id " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.parentId is not null and qdc.trangThai = '29'" +
            "AND ((:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho})) " +
            "AND ((:#{#param.loaiDc} IS NULL OR qdc.loaiDc = :#{#param.loaiDc})) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND ((:#{#param.maDvi} IS NULL OR LOWER(qdc.maDvi) LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND ((qdc.loaiDc= 'DCNB' OR qdc.loaiDc= 'CHI_CUC') OR  ((:#{#param.typeQd} IS NULL OR qdc.loaiQdinh = :#{#param.typeQd})))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.trangThai} IS NULL OR pnk.trangThai = :#{#param.trangThai}) " +
            "GROUP BY pnk.id,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKhoNhan,khdcd.tenNhaKhoNhan, khdcd.maDiemKhoNhan,khdcd.tenDiemKhoNhan,khdcd.maLoKhoNhan," +
            "khdcd.tenLoKhoNhan,khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan, khdcd.thayDoiThuKho,pnk.soPhieuNhapKho, pnk.ngayLap,  pnk.bangKeVtId, pnk.soBangKeVt," +
            "khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh, khdcd.tenCloaiVthh,khdcd.donViTinh,khdcd.soLuongDc,khdcd.duToanKphi," +
            "pnk.idPhieuKtraCluong, pnk.soPhieuKtraCluong, pktcl.ngayGiamDinh, " +
            "bkch.id, bkch.soBangKe, pnk.bbNghiemThuBqld, " +
            "pnk.trangThai, pnk.trangThai,khdcd.id " +
            "ORDER BY qdc.soQdinh DESC")
    Page<DcnbPhieuNhapKhoHdrDTO> searchPage(@Param("param")DcnbPhieuNhapKhoHdrReq req, Pageable pageable);
    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuNhapKhoHdrListDTO(" +
            "pnk.id,pnk.soPhieuNhapKho,pnk.ngayLap) " +
            "FROM DcnbPhieuNhapKhoHdr pnk " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = pnk.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND (:#{#param.trangThai} IS NULL OR pnk.trangThai = :#{#param.trangThai}) " +
            "AND ((:#{#param.qdDcCucId} IS NULL OR pnk.qdDcCucId = :#{#param.qdDcCucId}))"+
            "AND ((:#{#param.maLoKho} IS NULL OR pnk.maLoKho = :#{#param.maLoKho}))" +
            "AND ((:#{#param.maNganKho} IS NULL OR pnk.maNganKho = :#{#param.maNganKho}))" +
            "ORDER BY pnk.soPhieuNhapKho desc, pnk.nam desc")
    List<DcnbPhieuNhapKhoHdrListDTO> searchList(@Param("param")DcnbPhieuNhapKhoHdrReq objReq);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuNhapKhoHdrListDTO(" +
            "pnk.id,pnk.soPhieuNhapKho, pnk.id ,pnk.ngayLap,pnk.tongSoLuong, pktcl.id, pktcl.soPhieu,bkchh.id, bkchh.soBangKe,bknvt.id, bknvt.soBangKe, bbcbkh.id, bbcbkh.soBban ) " +
            "FROM DcnbPhieuNhapKhoHdr pnk " +
            "LEFT JOIN DcnbPhieuKtChatLuongHdr pktcl On pktcl.id = pnk.idPhieuKtraCluong " +
            "LEFT JOIN DcnbBangKeCanHangHdr bkchh On bkchh.phieuNhapKhoId = pnk.id " +
            "LEFT JOIN DcnbBangKeNhapVTHdr bknvt On bknvt.phieuNhapKhoId = pnk.id " +
            "LEFT JOIN DcnbBbChuanBiKhoHdr bbcbkh On bbcbkh.idPhieuNhapKho = pnk.id " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = pnk.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND (:#{#param.trangThai} IS NULL OR pnk.trangThai = :#{#param.trangThai}) " +
            "AND ((:#{#param.soQdDcCuc} IS NULL OR LOWER(pnk.soQdDcCuc) LIKE CONCAT('%',LOWER(:#{#param.soQdDcCuc}),'%')))" +
            "AND ((:#{#param.maLoKho} IS NULL OR pnk.maLoKho = :#{#param.maLoKho}))" +
            "AND ((:#{#param.maNganKho} IS NULL OR pnk.maNganKho = :#{#param.maNganKho})) " +
            "GROUP BY pnk.id,pnk.soPhieuNhapKho, pnk.id ,pnk.ngayLap,pnk.tongSoLuong, pktcl.id, pktcl.soPhieu,bkchh.id, bkchh.soBangKe,bknvt.id, bknvt.soBangKe, bbcbkh.id, bbcbkh.soBban "+
            "ORDER BY pnk.soPhieuNhapKho desc")
    List<DcnbPhieuNhapKhoHdrListDTO> searchListChung(@Param("param")DcnbPhieuNhapKhoHdrReq objReq);
    @Query(value = "SELECT dpxkh.* \n" +
            "FROM DCNB_KE_HOACH_DC_DTL dtl\n" +
            "JOIN DCNB_KE_HOACH_DC_HDR hdr ON hdr.id = dtl.HDR_ID\n" +
            "JOIN DCNB_QUYET_DINH_DC_C_DTL qdcd ON qdcd.DCNB_KE_HOACH_DC_HDR_ID = hdr.id\n" +
            "JOIN DCNB_QUYET_DINH_DC_C_HDR qdch ON qdch.ID = qdcd.HDR_ID\n" +
            "JOIN DCNB_PHIEU_NHAP_KHO_HDR dpxkh ON dpxkh.KE_HOACH_DC_DTL_ID = dtl.ID\n" +
            "WHERE dpxkh.TRANG_THAI = '17' AND qdch.SO_QDINH = ?1\n" +
            "AND dtl.ID IN (SELECT dtlv.ID \n" +
            "FROM DCNB_KE_HOACH_DC_DTL dtlv\n" +
            "WHERE dtlv.PARENT_ID = (SELECT dtlvv.PARENT_ID \n" +
            "FROM DCNB_KE_HOACH_DC_DTL dtlvv\n" +
            "WHERE dtlvv.ID = ?2) )", nativeQuery = true)
    List<DcnbPhieuNhapKhoHdr> findBySoQuyetDinhAndKeHoachDtlId(String soQdinhCuc, Long keHoachDtlId);
}
