package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbBienBanTinhKho;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanTinhKhoHdrDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanTinhKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbBienBanTinhKhoHdrRepository extends JpaRepository<DcnbBienBanTinhKhoHdr, Long> {

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanTinhKhoHdrDTO(" +
            "bbtk.id,qdc.id,pxk.id,bkch.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,bbtk.soBbTinhKho,bbtk.ngayBatDauXuat, bbtk.ngayKeThucXuat,pxk.soPhieuXuatKho,bkch.soBangKe,pxk.ngayXuatKho," +
            "bbtk.trangThai,bbtk.trangThai,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKho,khdcd.tenNhaKho," +
            "khdcd.maNganKho,khdcd.tenNganKho,qdc.ngayHieuLuc,qdc.ngayKyQdinh) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbBienBanTinhKhoHdr bbtk ON bbtk.qDinhDccId = qdc.id "+
            "LEFT JOIN DcnbBangKeCanHangHdr bkch ON bkch.qDinhDccId = qdc.id "+
            "LEFT JOIN DcnbPhieuXuatKhoHdr pxk ON pxk.id = bbtk.phieuXuatKhoId " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "WHERE 1 =1 "+
            "AND qdc.trangThai = '29' AND qdc.loaiDc = :#{#param.loaiDc} " +
            "AND (bbtk.type = :#{#param.type})"+
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) "+
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}) OR (:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))"+
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBbTinhKho} IS NULL OR LOWER(bbtk.soBbTinhKho) LIKE CONCAT('%',LOWER(:#{#param.soBbTinhKho}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(bbtk.soQdinhDcc) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR bbtk.ngayBatDauXuat >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR bbtk.ngayBatDauXuat <= :#{#param.denNgay}) ) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR bbtk.ngayKeThucXuat >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR bbtk.ngayKeThucXuat <= :#{#param.denNgay}) ) " +
            "ORDER BY bbtk.soQdinhDcc desc, bbtk.nam desc")
    Page<DcnbBienBanTinhKhoHdrDTO> searchPageChiCuc(@Param("param") SearchDcnbBienBanTinhKho req, Pageable pageable);

    Optional<DcnbBienBanTinhKhoHdr> findFirstBySoBbTinhKho(String soBbTinhKho);

    List<DcnbBienBanTinhKhoHdr> findByIdIn(List<Long> ids);

    List<DcnbBienBanTinhKhoHdr> findAllByIdIn(List<Long> idList);
    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanTinhKhoHdrDTO(" +
            "bbtk.id,qdc.id,pxk.id,bkch.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,bbtk.soBbTinhKho,bbtk.ngayBatDauXuat, bbtk.ngayKeThucXuat,pxk.soPhieuXuatKho,bkch.soBangKe,pxk.ngayXuatKho,bbtk.trangThai,bbtk.trangThai,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKho,khdcd.tenNhaKho,khdcd.maNganKho,khdcd.tenNganKho,qdc.ngayHieuLuc,qdc.ngayKyQdinh) FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbDataLinkHdr dtlh On dtlh.qdCcParentId = qdc.id " +
            "LEFT JOIN DcnbBienBanTinhKhoHdr bbtk ON bbtk.qDinhDccId = dtlh.qdCcId "+
            "LEFT JOIN DcnbBangKeCanHangHdr bkch ON bkch.qDinhDccId = dtlh.qdCcId "+
            "LEFT JOIN DcnbPhieuXuatKhoHdr pxk ON pxk.id = bbtk.phieuXuatKhoId " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = dtlh.qdCcId " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "WHERE 1 =1 "+
            "AND qdc.trangThai = '29' AND qdc.loaiDc = :#{#param.loaiDc} AND (:#{#param.type} IS NULL OR bbtk.type = :#{#param.type})"+
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) "+
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}) OR (:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))"+
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBbTinhKho} IS NULL OR LOWER(bbtk.soBbTinhKho) LIKE CONCAT('%',LOWER(:#{#param.soBbTinhKho}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(bbtk.soQdinhDcc) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR bbtk.ngayBatDauXuat >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR bbtk.ngayBatDauXuat <= :#{#param.denNgay}) ) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR bbtk.ngayKeThucXuat >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR bbtk.ngayKeThucXuat <= :#{#param.denNgay}) ) " +
            "ORDER BY bbtk.soQdinhDcc desc, bbtk.nam desc")
    Page<DcnbBienBanTinhKhoHdrDTO> searchPageCuc(@Param("param") SearchDcnbBienBanTinhKho req, Pageable pageable);
}
