package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbKqDcSearch;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBcKqDcDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DcnbBcKqDcDtlRepository extends JpaRepository<DcnbBcKqDcDtl, Long> {
    List<DcnbBcKqDcDtl> findByHdrId(Long id);
//    String loaiVthh, String cloaiVthh, String tenLoaiVthh, String tenCloaiVthh, String maDiemKho, String tenDiemKho, String maNhaKho, String tenNhaKho,
//    String maNganKho, String tenNganKho, String maLoKho, String tenLoKho, String donViTinh, String tenDonViTinh,
//    java.math.BigDecimal slTon, BigDecimal slDieuChuyenQd, BigDecimal slXuatTt, BigDecimal slNhapTt, BigDecimal kinhPhiTheoQd, BigDecimal kinhPhiXuatTt, BigDecimal kinhPhiNhapTt, BigDecimal trichYeu, Boolean ketQua, Boolean tinhTrang, String type
    @Query(value = "SELECT new com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBcKqDcDtl(" +
            "khdcd.loaiVthh, khdcd.cloaiVthh,khdcd.tenLoaiVthh, khdcd.tenCloaiVthh, khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maNhaKho,khdcd.tenNhaKho," +
            "khdcd.maNganKho,khdcd.tenNganKho, khdcd.maLoKho,khdcd.tenLoKho,khdcd.donViTinh, khdcd.tenDonViTinh ," +
            "khdcd.tonKho, khdcd.soLuongDc, bbtk.tongSlXuatTheoTt,bbtk.tongSlXuatTheoTt,khdcd.duToanKphi, khdcd.duToanKphi, khdcd.duToanKphi,false, false, 'CHI_CUC') " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbBienBanTinhKhoHdr bbtk On bbtk.qDinhDccId = qdc.id and bbtk.maLoKho = khdcd.maLoKho and bbtk.maNganKho = khdcd.maNganKho " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29'" +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))" +
            "AND (:#{#param.soQdinhCuc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhCuc}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR khdcd.thoiGianDkDc >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR khdcd.thoiGianDkDc <= :#{#param.denNgay}) ) " +
            "ORDER BY qdc.soQdinh desc")
    List<DcnbBcKqDcDtl> thongTinXuatHangChiCuc(@Param("param") DcnbBbKqDcSearch objReq);
    @Query(value = "SELECT new com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBcKqDcDtl(" +
            "khdcd.loaiVthh, khdcd.cloaiVthh,khdcd.tenLoaiVthh, khdcd.tenCloaiVthh, khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maNhaKho,khdcd.tenNhaKho," +
            "khdcd.maNganKho,khdcd.tenNganKho, khdcd.maLoKho,khdcd.tenLoKho,khdcd.donViTinh, khdcd.tenDonViTinh ," +
            "khdcd.tonKho, khdcd.soLuongDc, bbtk.tongSlXuatTheoTt,bbtk.tongSlXuatTheoTt,khdcd.duToanKphi, khdcd.duToanKphi, khdcd.duToanKphi,false, false, 'CHI_CUC') " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbBienBanTinhKhoHdr bbtk On bbtk.qDinhDccId = qdc.id and bbtk.maLoKho = khdcd.maLoKho and bbtk.maNganKho = khdcd.maNganKho " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29'" +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))" +
            "AND (:#{#param.soQdinhCuc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhCuc}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR khdcd.thoiGianDkDc >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR khdcd.thoiGianDkDc <= :#{#param.denNgay}) ) " +
            "ORDER BY qdc.soQdinh desc")
    List<DcnbBcKqDcDtl> thongTinNhapHangChiCuc(@Param("param")DcnbBbKqDcSearch objReq);
    @Query(value = "SELECT new com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBcKqDcDtl(" +
            "khdcd.loaiVthh, khdcd.cloaiVthh,khdcd.tenLoaiVthh, khdcd.tenCloaiVthh, khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maNhaKho,khdcd.tenNhaKho," +
            "khdcd.maNganKho,khdcd.tenNganKho, khdcd.maLoKho,khdcd.tenLoKho,khdcd.donViTinh, khdcd.tenDonViTinh ," +
            "khdcd.tonKho, khdcd.soLuongDc, bbtk.tongSlXuatTheoTt,bbtk.tongSlXuatTheoTt,khdcd.duToanKphi, khdcd.duToanKphi, khdcd.duToanKphi,false, false, 'CHI_CUC') " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbBienBanTinhKhoHdr bbtk On bbtk.qDinhDccId = qdc.id and bbtk.maLoKho = khdcd.maLoKho and bbtk.maNganKho = khdcd.maNganKho " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29'" +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))" +
            "AND (:#{#param.soQdinhCuc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhCuc}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR khdcd.thoiGianDkDc >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR khdcd.thoiGianDkDc <= :#{#param.denNgay}) ) " +
            "ORDER BY qdc.soQdinh desc")
    List<DcnbBcKqDcDtl> thongTinXuatHangCuc(@Param("param")DcnbBbKqDcSearch objReq);

    @Query(value = "SELECT new com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBcKqDcDtl(" +
            "khdcd.loaiVthh, khdcd.cloaiVthh,khdcd.tenLoaiVthh, khdcd.tenCloaiVthh, khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maNhaKho,khdcd.tenNhaKho," +
            "khdcd.maNganKho,khdcd.tenNganKho, khdcd.maLoKho,khdcd.tenLoKho,khdcd.donViTinh, khdcd.tenDonViTinh ," +
            "khdcd.tonKho, khdcd.soLuongDc, bbtk.tongSlXuatTheoTt,bbtk.tongSlXuatTheoTt,khdcd.duToanKphi, khdcd.duToanKphi, khdcd.duToanKphi,false, false, 'CHI_CUC') " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbBienBanTinhKhoHdr bbtk On bbtk.qDinhDccId = qdc.id and bbtk.maLoKho = khdcd.maLoKho and bbtk.maNganKho = khdcd.maNganKho " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29'" +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))" +
            "AND (:#{#param.soQdinhCuc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhCuc}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR khdcd.thoiGianDkDc >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR khdcd.thoiGianDkDc <= :#{#param.denNgay}) ) " +
            "ORDER BY qdc.soQdinh desc")
    List<DcnbBcKqDcDtl> thongTinNhapHangCuc(@Param("param")DcnbBbKqDcSearch objReq);
}
