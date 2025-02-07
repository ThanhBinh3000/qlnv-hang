package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchPhieuXuatKho;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuXuatKhoHdrDTO;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuXuatKhoHdrListDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbGiaoNhanHdr;
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
    List<DcnbPhieuXuatKhoHdr> findByKeHoachDcDtlId(Long keHoachDcDtlId);
    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuXuatKhoHdrDTO(" +
            "pxk.id,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKho,khdcd.tenNhaKho, khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho, khdcd.thayDoiThuKho,pxk.soPhieuXuatKho,pxk.ngayXuatKho, pxk.phieuKnChatLuongHdrId,pxk.soPhieuKnChatLuong,pxk.ngayKyPhieuKnChatLuong," +
            "khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh, khdcd.tenCloaiVthh,pkncl.nguoiKt,khdcd.donViTinh,khdcd.soLuongDc,khdcd.duToanKphi," +
            "pxk.bangKeChId, pxk.soBangKeCh," +
            "pxk.bangKeVtId, pxk.soBangKeVt," +
            "pxk.trangThai, pxk.trangThai,khdcd.id) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbPhieuXuatKhoHdr pxk On khdcd.id = pxk.keHoachDcDtlId AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "LEFT JOIN DcnbPhieuKnChatLuongHdr pkncl On qdc.parentId = pkncl.qdDcId " +
            "and ((pkncl.maLoKho is not null and pkncl.maLoKho = khdcd.maLoKho and pkncl.trangThai = '05' ) or (pkncl.maLoKho is null and pkncl.maNganKho = khdcd.maNganKho and pkncl.trangThai = '05'))" +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.parentId is not null and qdc.trangThai = '29' " +
            "AND ((:#{#param.loaiDc} IS NULL OR qdc.loaiDc = :#{#param.loaiDc}))" +
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.maDvi} IS NULL OR LOWER(qdc.maDvi) LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND ((qdc.loaiDc= 'DCNB' OR qdc.loaiDc= 'CHI_CUC') OR  ((:#{#param.typeQd} IS NULL OR qdc.loaiQdinh = :#{#param.typeQd})))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.trangThai} IS NULL OR pxk.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND (:#{#param.soPhieuXuatKho} IS NULL OR LOWER(pxk.soPhieuXuatKho) LIKE CONCAT('%',LOWER(:#{#param.soPhieuXuatKho}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR pxk.ngayXuatKho >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR pxk.ngayXuatKho <= :#{#param.denNgay}) ) " +
            "GROUP BY pxk.id,qdc.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKho,khdcd.tenNhaKho, khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho, khdcd.thayDoiThuKho,pxk.soPhieuXuatKho,pxk.ngayXuatKho, pxk.phieuKnChatLuongHdrId,pxk.soPhieuKnChatLuong,pxk.ngayKyPhieuKnChatLuong," +
            "khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh, khdcd.tenCloaiVthh,pkncl.nguoiKt,khdcd.donViTinh,khdcd.soLuongDc,khdcd.duToanKphi," +
            "pxk.bangKeChId, pxk.soBangKeCh," +
            "pxk.bangKeVtId, pxk.soBangKeVt," +
            "pxk.trangThai, pxk.trangThai,khdcd.id " +
            "ORDER BY qdc.soQdinh DESC")
    Page<DcnbPhieuXuatKhoHdrDTO> searchPage(@Param("param") SearchPhieuXuatKho req, Pageable pageable);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuXuatKhoHdrListDTO(" +
            "pxk.id,pxk.soPhieuXuatKho,pxk.ngayTaoPhieu) " +
            "FROM DcnbPhieuXuatKhoHdr pxk " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.id = pxk.keHoachDcDtlId " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = pxk.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND (:#{#param.trangThai} IS NULL OR pxk.trangThai = :#{#param.trangThai}) " +
            "AND ((:#{#param.qdinhDccId} IS NULL OR pxk.qddcId = :#{#param.qdinhDccId}))" +
            "AND ((:#{#param.maLoKho} IS NULL OR pxk.maLoKho = :#{#param.maLoKho}))" +
            "AND ((:#{#param.maNganKho} IS NULL OR pxk.maNganKho = :#{#param.maNganKho}))" +
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "ORDER BY pxk.soPhieuXuatKho desc, pxk.nam desc")
    List<DcnbPhieuXuatKhoHdrListDTO> searchList(@Param("param") SearchPhieuXuatKho req);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuXuatKhoHdrListDTO(" +
            "pxk.id,pxk.soPhieuXuatKho,pxk.ngayTaoPhieu, pxk.tongSoLuong, pkncl.id, pkncl.soPhieu,pxk.bangKeChId, pxk.soBangKeCh,bkxvth.id, bkxvth.soBangKe ) " +
            "FROM DcnbPhieuXuatKhoHdr pxk " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.id = pxk.keHoachDcDtlId " +
            "LEFT JOIN DcnbPhieuKnChatLuongHdr pkncl On pkncl.id = pxk.phieuKnChatLuongHdrId " +
            "LEFT JOIN DcnbBangKeXuatVTHdr bkxvth On bkxvth.phieuXuatKhoId = pxk.id " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = pxk.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND (:#{#param.trangThai} IS NULL OR pxk.trangThai = :#{#param.trangThai}) " +
            "AND ((:#{#param.qdinhDccId} IS NULL OR pxk.qddcId = :#{#param.qdinhDccId}))" +
            "AND ((:#{#param.maLoKho} IS NULL OR pxk.maLoKho = :#{#param.maLoKho}))" +
            "AND ((:#{#param.maNganKho} IS NULL OR pxk.maNganKho = :#{#param.maNganKho}))" +
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "GROUP BY pxk.id,pxk.soPhieuXuatKho,pxk.ngayTaoPhieu, pxk.tongSoLuong, pkncl.id, pkncl.soPhieu,pxk.bangKeChId, pxk.soBangKeCh,bkxvth.id, bkxvth.soBangKe "+
            "ORDER BY pxk.soPhieuXuatKho desc")
    List<DcnbPhieuXuatKhoHdrListDTO> searchListChung(@Param("param") SearchPhieuXuatKho req);
    @Query(value = "SELECT dpxkh.* \n" +
            "FROM DCNB_KE_HOACH_DC_DTL dtl\n" +
            "JOIN DCNB_KE_HOACH_DC_HDR hdr ON hdr.id = dtl.HDR_ID\n" +
            "JOIN DCNB_QUYET_DINH_DC_C_DTL qdcd ON qdcd.DCNB_KE_HOACH_DC_HDR_ID = hdr.id\n" +
            "JOIN DCNB_QUYET_DINH_DC_C_HDR qdch ON qdch.ID = qdcd.HDR_ID\n" +
            "JOIN DCNB_PHIEU_XUAT_KHO_HDR dpxkh ON dpxkh.KE_HOACH_DC_DTL_ID = dtl.ID\n" +
            "WHERE dpxkh.TRANG_THAI = '17' AND qdch.SO_QDINH = ?1  \n" +
            "AND dtl.ID IN (SELECT dtlv.ID \n" +
            "FROM DCNB_KE_HOACH_DC_DTL dtlv\n" +
            "WHERE dtlv.PARENT_ID = (SELECT dtlvv.PARENT_ID \n" +
            "FROM DCNB_KE_HOACH_DC_DTL dtlvv\n" +
            "WHERE dtlvv.ID = ?2) )", nativeQuery = true)
    List<DcnbPhieuXuatKhoHdr> findBySoQuyetDinhAndKeHoachDtlId(String soQdinhCuc, Long keHoachDtlId);
}
