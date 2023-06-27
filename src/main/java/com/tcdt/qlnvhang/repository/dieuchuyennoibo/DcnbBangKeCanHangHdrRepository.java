package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchBangKeCanHang;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeCanHangHdrDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeCanHangHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbBangKeCanHangHdrRepository extends JpaRepository<DcnbBangKeCanHangHdr, Long> {

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeCanHangHdrDTO(" +
            "bkch.id,pxk.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,pxk.maDiemKho,pxk.tenDiemKho,pxk.maLoKho," +
            "pxk.tenLoKho,pxk.soPhieuXuatKho,bkch.soBangKe,pxk.ngayXuatKho,bkch.trangThai,bkch.trangThai,pxk.loaiVthh,pxk.tenLoaiVthh,pxk.cloaiVthh,pxk.tenCloaiVthh,pxk.maNhaKho,pxk.tenNhaKho,pxk.donViTinh,khdcd.tenDonViTinh,pxk.maNganKho,pxk.tenNganKho,pxk.nguoiGiaoHang,pxk.soCmt,pxk.ctyNguoiGh,pxk.diaChi,pxk.thoiGianGiaoNhan) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbBangKeCanHangHdr bkch ON bkch.qDinhDccId = qdc.id "+
            "LEFT JOIN DcnbPhieuXuatKhoHdr pxk ON pxk.id = bkch.phieuXuatKhoId " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 "+
            "AND qdc.trangThai = '29' AND qdc.loaiDc = :#{#param.loaiDc} AND (bkch.type IS NULL OR (:#{#param.type} IS NULL OR bkch.type = :#{#param.type}))"+
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) "+
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) "+
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}) OR (:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))"+
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(bkch.soBangKe) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(bkch.soQdinhDcc) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR pxk.ngayXuatKho >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR pxk.ngayXuatKho <= :#{#param.denNgay}) ) " +
            "ORDER BY bkch.soQdinhDcc desc, bkch.nam desc")
    Page<DcnbBangKeCanHangHdrDTO> searchPage(@Param("param")SearchBangKeCanHang req, Pageable pageable);

    Optional<DcnbBangKeCanHangHdr> findFirstBySoBangKe(String soBangKe);


    List<DcnbBangKeCanHangHdr> findByIdIn(List<Long> ids);

    List<DcnbBangKeCanHangHdr> findAllByIdIn(List<Long> idList);
    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeCanHangHdrDTO(" +
            "bkch.id,pxk.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,pxk.maDiemKho,pxk.tenDiemKho,pxk.maLoKho," +
            "pxk.tenLoKho,pxk.soPhieuXuatKho,bkch.soBangKe,pxk.ngayXuatKho,bkch.trangThai,bkch.trangThai,pxk.loaiVthh,pxk.tenLoaiVthh,pxk.cloaiVthh,pxk.tenCloaiVthh,pxk.maNhaKho,pxk.tenNhaKho,pxk.donViTinh,khdcd.tenDonViTinh,pxk.maNganKho,pxk.tenNganKho,pxk.nguoiGiaoHang,pxk.soCmt,pxk.ctyNguoiGh,pxk.diaChi,pxk.thoiGianGiaoNhan) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbDataLinkHdr dtlh On dtlh.qdCcParentId = qdc.id " +
            "LEFT JOIN DcnbBangKeCanHangHdr bkch ON bkch.qDinhDccId = dtlh.qdCcId "+
            "LEFT JOIN DcnbPhieuXuatKhoHdr pxk ON pxk.id = bkch.phieuXuatKhoId " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = dtlh.qdCcId " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 "+
            "AND dtlh.type = :#{#param.typeDataLink} AND qdc.trangThai = '29' AND qdc.loaiDc = :#{#param.loaiDc} AND (bkch.type IS NULL OR (:#{#param.type} IS NULL OR bkch.type = :#{#param.type}))"+
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) "+
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) "+
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}) OR (:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))"+
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(bkch.soBangKe) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(bkch.soQdinhDcc) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR pxk.ngayXuatKho >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR pxk.ngayXuatKho <= :#{#param.denNgay}) ) " +
            "ORDER BY bkch.soQdinhDcc desc, bkch.nam desc")
    Page<DcnbBangKeCanHangHdrDTO> searchPageCuc(@Param("param")SearchBangKeCanHang req, Pageable pageable);
}
