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
//    String loaiVthh, String cloaiVthh, String tenLoaiVthh, String tenCloaiVthh,
//    String maDiemKho, String tenDiemKho, String maNhaKho, String tenNhaKho, String maNganKho, String tenNganKho, String maLoKho, String tenLoKho,
//    String maDviNhan, String tenDviNhan, String maDiemKhoNhan, String tenDiemKhoNhan, String maNhaKhoNhan, String tenNhaKhoNhan, String maNganKhoNhan, String tenNganKhoNhan, String maLoKhoNhan, String tenLoKhoNhan,
//    String donViTinh,
//    java.math.BigDecimal slTon, BigDecimal slDieuChuyenQd, BigDecimal slXuatTt, BigDecimal slNhapTt, BigDecimal kinhPhiTheoQd, BigDecimal kinhPhiXuatTt, BigDecimal kinhPhiNhapTt, Boolean ketQua, Boolean tinhTrang, String type
//    slNhapTt = bbtk.tongSlXuatTheoTt sai
//    kinhPhiXuatTt =   khdcd.duToanKphi sai
//    kinhPhiNhapTt =   khdcd.duToanKphi sai
    @Query(value = "SELECT new com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBcKqDcDtl(" +
            "khdcd.loaiVthh, khdcd.cloaiVthh,khdcd.tenLoaiVthh, khdcd.tenCloaiVthh," +
            "khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maNhaKho,khdcd.tenNhaKho,khdcd.maNganKho,khdcd.tenNganKho, khdcd.maLoKho,khdcd.tenLoKho," +
            "khdcd.maChiCucNhan,khdcd.tenChiCucNhan, khdcd.maDiemKhoNhan,khdcd.tenDiemKhoNhan,khdcd.maNhaKhoNhan,khdcd.tenNhaKhoNhan,khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan, khdcd.maLoKhoNhan,khdcd.tenLoKhoNhan," +
            "khdcd.donViTinh, " +
            "khdcd.tonKho, khdcd.soLuongDc, bbtk.tongSlXuatTheoTt,pnkh.tongSoLuong,khdcd.duToanKphi, pxkh.thanhTien, pnkh.tongKinhPhi,false, false, 'CHI_CUC') " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbBienBanTinhKhoHdr bbtk On bbtk.qDinhDccId = qdc.id " +
            "and ((khdcd.maLoKho is not null and bbtk.maLoKho = khdcd.maLoKho and bbtk.maNganKho = khdcd.maNganKho ) or (khdcd.maLoKho is null and bbtk.maNganKho = khdcd.maNganKho )) " +
            "LEFT JOIN DcnbPhieuXuatKhoHdr pxkh On pxkh.qddcId = qdc.id " +
            "and ((khdcd.maLoKho is not null and pxkh.maLoKho = khdcd.maLoKho and pxkh.maNganKho = khdcd.maNganKho ) or (khdcd.maLoKho is null and pxkh.maNganKho = khdcd.maNganKho )) " +
            "LEFT JOIN DcnbPhieuNhapKhoHdr pnkh On pnkh.qdDcCucId = qdc.id " +
            "and ((khdcd.maLoKhoNhan is not null and pnkh.maLoKho = khdcd.maLoKhoNhan and pnkh.maNganKho = khdcd.maNganKhoNhan ) or (khdcd.maLoKhoNhan is null and pnkh.maNganKho = khdcd.maNganKhoNhan )) " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29'" +
            "AND ((:#{#param.maDvi} IS NULL OR LOWER(qdc.maDvi) = :#{#param.maDvi}))" +
            "AND (:#{#param.soQdinhCuc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhCuc}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR khdcd.thoiGianDkDc >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR khdcd.thoiGianDkDc <= :#{#param.denNgay}) ) " +
            "ORDER BY qdc.soQdinh desc")
    List<DcnbBcKqDcDtl> thongTinXuatNhapHangChiCuc(@Param("param") DcnbBbKqDcSearch objReq);

    @Query(value = "SELECT new com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBcKqDcDtl(" +
            "khdcd.loaiVthh, khdcd.cloaiVthh,khdcd.tenLoaiVthh, khdcd.tenCloaiVthh," +
            "khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maNhaKho,khdcd.tenNhaKho,khdcd.maNganKho,khdcd.tenNganKho, khdcd.maLoKho,khdcd.tenLoKho," +
            "khdcd.maChiCucNhan,khdcd.tenChiCucNhan, khdcd.maDiemKhoNhan,khdcd.tenDiemKhoNhan,khdcd.maNhaKhoNhan,khdcd.tenNhaKhoNhan,khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan, khdcd.maLoKhoNhan,khdcd.tenLoKhoNhan," +
            "khdcd.donViTinh, " +
            "khdcd.tonKho, khdcd.soLuongDc, bbtk.tongSlXuatTheoTt,bbtk.tongSlXuatTheoTt,khdcd.duToanKphi, khdcd.duToanKphi, khdcd.duToanKphi,false, false, 'CUC') " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbBienBanTinhKhoHdr bbtk On bbtk.qDinhDccId = qdc.id and bbtk.maLoKho = khdcd.maLoKho and bbtk.maNganKho = khdcd.maNganKho " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29'" +
            "AND ((:#{#param.maDvi} IS NULL OR LOWER(qdc.maDvi) = :#{#param.maDvi}))" +
            "AND (:#{#param.soQdinhCuc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhCuc}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR khdcd.thoiGianDkDc >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR khdcd.thoiGianDkDc <= :#{#param.denNgay}) ) " +
            "ORDER BY qdc.soQdinh desc")
    List<DcnbBcKqDcDtl> thongTinXuatNhapHangCuc(@Param("param") DcnbBbKqDcSearch objReq);

    List<DcnbBcKqDcDtl> findByHdrIdIn(List<Long> ids);
}
