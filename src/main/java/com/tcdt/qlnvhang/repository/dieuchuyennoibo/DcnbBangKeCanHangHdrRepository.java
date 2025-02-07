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
            "bkch.id,qdc.id,pxk.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,pxk.soPhieuXuatKho,bkch.soBangKe,pxk.ngayXuatKho,pnk.soPhieuNhapKho, pnk.ngayLap ,bkch.trangThai,bkch.trangThai,khdcd.loaiVthh," +
            "khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKho,khdcd.tenNhaKho,khdcd.donViTinh," +
            "khdcd.maNganKho,khdcd.tenNganKho,pxk.nguoiGiaoHang,pxk.soCmt,pxk.ctyNguoiGh,pxk.diaChi,pxk.thoiGianGiaoNhan,khdcd.id) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbBangKeCanHangHdr bkch ON bkch.keHoachDcDtlId = khdcd.id AND (bkch.type IS NULL OR (:#{#param.type} IS NULL OR bkch.type = :#{#param.type})) and (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "LEFT JOIN DcnbPhieuXuatKhoHdr pxk ON pxk.id = bkch.phieuXuatKhoId " +
            "LEFT JOIN DcnbPhieuNhapKhoHdr pnk ON pnk.id = bkch.phieuNhapKhoId " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.parentId is not null and qdc.trangThai = '29' " +
            "AND ((:#{#param.loaiDc} IS NULL OR qdc.loaiDc = :#{#param.loaiDc}))" +
            "AND (bkch.type IS NULL OR (:#{#param.type} IS NULL OR bkch.type = :#{#param.type}))" +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.type} IS NULL OR qdc.type = :#{#param.type=='01'? 'NDC':'DC'})) " +
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND ((:#{#param.maDvi} IS NULL OR LOWER(qdc.maDvi) LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND ((qdc.loaiDc= 'DCNB' OR qdc.loaiDc= 'CHI_CUC') OR  ((:#{#param.typeQd} IS NULL OR qdc.loaiQdinh = :#{#param.typeQd})))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.trangThai} IS NULL OR bkch.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(bkch.soBangKe) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND (:#{#param.maLoKho} IS NULL OR bkch.maLoKho = :#{#param.maLoKho}) " +
            "AND (:#{#param.maNganKho} IS NULL OR bkch.maNganKho = :#{#param.maNganKho}) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR pxk.ngayXuatKho >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR pxk.ngayXuatKho <= :#{#param.denNgay}) ) " +
            "GROUP BY bkch.id,qdc.id,pxk.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,pxk.soPhieuXuatKho,bkch.soBangKe,pxk.ngayXuatKho,pnk.soPhieuNhapKho, pnk.ngayLap ,bkch.trangThai,bkch.trangThai,khdcd.loaiVthh," +
            "khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKho,khdcd.tenNhaKho,khdcd.donViTinh," +
            "khdcd.maNganKho,khdcd.tenNganKho,pxk.nguoiGiaoHang,pxk.soCmt,pxk.ctyNguoiGh,pxk.diaChi,pxk.thoiGianGiaoNhan,khdcd.id " +
            "ORDER BY qdc.soQdinh DESC")
    Page<DcnbBangKeCanHangHdrDTO> searchPageXuat(@Param("param") SearchBangKeCanHang req, Pageable pageable);

    Optional<DcnbBangKeCanHangHdr> findFirstBySoBangKe(String soBangKe);


    List<DcnbBangKeCanHangHdr> findByIdIn(List<Long> ids);

    List<DcnbBangKeCanHangHdr> findAllByIdIn(List<Long> idList);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeCanHangHdrDTO(" +
            "bkch.id,qdc.id,pxk.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKhoNhan,khdcd.tenDiemKhoNhan,khdcd.maLoKhoNhan," +
            "khdcd.tenLoKhoNhan,pxk.soPhieuXuatKho,bkch.soBangKe,pxk.ngayXuatKho,pnk.soPhieuNhapKho, pnk.ngayLap ,bkch.trangThai,bkch.trangThai,khdcd.loaiVthh," +
            "khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKhoNhan,khdcd.tenNhaKhoNhan,khdcd.donViTinh," +
            "khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan,pxk.nguoiGiaoHang,pxk.soCmt,pxk.ctyNguoiGh,pxk.diaChi,pxk.thoiGianGiaoNhan,khdcd.id) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbBangKeCanHangHdr bkch ON bkch.keHoachDcDtlId = khdcd.id AND (bkch.type IS NULL OR (:#{#param.type} IS NULL OR bkch.type = :#{#param.type})) " +
            "LEFT JOIN DcnbPhieuXuatKhoHdr pxk ON pxk.id = bkch.phieuXuatKhoId " +
            "LEFT JOIN DcnbPhieuNhapKhoHdr pnk ON pnk.id = bkch.phieuNhapKhoId " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.parentId is not null and qdc.trangThai = '29' " +
            "AND ((:#{#param.loaiDc} IS NULL OR qdc.loaiDc = :#{#param.loaiDc}))" +
            "AND (bkch.type IS NULL OR (:#{#param.type} IS NULL OR bkch.type = :#{#param.type}))" +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND ((:#{#param.maDvi} IS NULL OR LOWER(qdc.maDvi) LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND ((:#{#param.type} IS NULL OR qdc.type = :#{#param.type=='01'? 'NDC':'DC'})) " +
            "AND ((qdc.loaiDc= 'DCNB' OR qdc.loaiDc= 'CHI_CUC') OR  ((:#{#param.typeQd} IS NULL OR qdc.loaiQdinh = :#{#param.typeQd})))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.trangThai} IS NULL OR bkch.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(bkch.soBangKe) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND (:#{#param.maLoKho} IS NULL OR bkch.maLoKho = :#{#param.maLoKho}) " +
            "AND (:#{#param.maNganKho} IS NULL OR bkch.maNganKho = :#{#param.maNganKho}) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR pxk.ngayXuatKho >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR pxk.ngayXuatKho <= :#{#param.denNgay}) ) " +
            "GROUP BY bkch.id,qdc.id,pxk.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKhoNhan,khdcd.tenDiemKhoNhan,khdcd.maLoKhoNhan," +
            "khdcd.tenLoKhoNhan,pxk.soPhieuXuatKho,bkch.soBangKe,pxk.ngayXuatKho,pnk.soPhieuNhapKho, pnk.ngayLap ,bkch.trangThai,bkch.trangThai,khdcd.loaiVthh," +
            "khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKhoNhan,khdcd.tenNhaKhoNhan,khdcd.donViTinh," +
            "khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan,pxk.nguoiGiaoHang,pxk.soCmt,pxk.ctyNguoiGh,pxk.diaChi,pxk.thoiGianGiaoNhan,khdcd.id " +
            "ORDER BY qdc.soQdinh DESC")
    Page<DcnbBangKeCanHangHdrDTO> searchPageNhan(@Param("param") SearchBangKeCanHang req, Pageable pageable);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeCanHangHdrDTO(" +
            "bkch.id,qdc.id,pxk.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,pxk.soPhieuXuatKho,bkch.soBangKe,pxk.ngayXuatKho,pnk.soPhieuNhapKho, pnk.ngayLap ,bkch.trangThai,bkch.trangThai,khdcd.loaiVthh," +
            "khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKho,khdcd.tenNhaKho,khdcd.donViTinh," +
            "khdcd.maNganKho,khdcd.tenNganKho,pxk.nguoiGiaoHang,pxk.soCmt,pxk.ctyNguoiGh,pxk.diaChi,pxk.thoiGianGiaoNhan, khdcd.id) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbBangKeCanHangHdr bkch ON bkch.keHoachDcDtlId = khdcd.id " +
            "LEFT JOIN DcnbPhieuXuatKhoHdr pxk ON pxk.id = bkch.phieuXuatKhoId " +
            "LEFT JOIN DcnbPhieuNhapKhoHdr pnk ON pnk.id = bkch.phieuNhapKhoId " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.parentId is not null and qdc.trangThai = '29' " +
            "AND ((:#{#param.loaiDc} IS NULL OR qdc.loaiDc = :#{#param.loaiDc}))" +
            "AND (bkch.type IS NULL OR (:#{#param.type} IS NULL OR bkch.type = :#{#param.type}))" +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND ((:#{#param.type} IS NULL OR qdc.type = :#{#param.type=='01'? 'NDC':'DC'})) " +
            "AND ((:#{#param.maDvi} IS NULL OR LOWER(qdc.maDvi) LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND ((qdc.loaiDc= 'DCNB' OR qdc.loaiDc= 'CHI_CUC') OR  ((:#{#param.typeQd} IS NULL OR qdc.loaiQdinh = :#{#param.typeQd})))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.trangThai} IS NULL OR bkch.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(bkch.soBangKe) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND (:#{#param.maLoKho} IS NULL OR bkch.maLoKho = :#{#param.maLoKho}) " +
            "AND (:#{#param.maNganKho} IS NULL OR bkch.maNganKho = :#{#param.maNganKho}) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR pxk.ngayXuatKho >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR pxk.ngayXuatKho <= :#{#param.denNgay}) ) " +
            "GROUP BY bkch.id,qdc.id,pxk.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,pxk.soPhieuXuatKho,bkch.soBangKe,pxk.ngayXuatKho,pnk.soPhieuNhapKho, pnk.ngayLap ,bkch.trangThai,bkch.trangThai,khdcd.loaiVthh," +
            "khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKho,khdcd.tenNhaKho,khdcd.donViTinh," +
            "khdcd.maNganKho,khdcd.tenNganKho,pxk.nguoiGiaoHang,pxk.soCmt,pxk.ctyNguoiGh,pxk.diaChi,pxk.thoiGianGiaoNhan, khdcd.id " +
            "ORDER BY qdc.soQdinh DESC")
    List<DcnbBangKeCanHangHdrDTO> searchListXuat(@Param("param") SearchBangKeCanHang req);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeCanHangHdrDTO(" +
            "bkch.id,qdc.id,pxk.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKhoNhan,khdcd.tenDiemKhoNhan,khdcd.maLoKhoNhan," +
            "khdcd.tenLoKhoNhan,pxk.soPhieuXuatKho,bkch.soBangKe,pxk.ngayXuatKho,pnk.soPhieuNhapKho, pnk.ngayLap ,bkch.trangThai,bkch.trangThai,khdcd.loaiVthh," +
            "khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKhoNhan,khdcd.tenNhaKhoNhan,khdcd.donViTinh," +
            "khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan,pxk.nguoiGiaoHang,pxk.soCmt,pxk.ctyNguoiGh,pxk.diaChi,pxk.thoiGianGiaoNhan,khdcd.id) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbBangKeCanHangHdr bkch ON bkch.keHoachDcDtlId = khdcd.id " +
            "LEFT JOIN DcnbPhieuXuatKhoHdr pxk ON pxk.id = bkch.phieuXuatKhoId " +
            "LEFT JOIN DcnbPhieuNhapKhoHdr pnk ON pnk.id = bkch.phieuNhapKhoId " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.parentId is not null and qdc.trangThai = '29' " +
            "AND ((:#{#param.loaiDc} IS NULL OR qdc.loaiDc = :#{#param.loaiDc}))" +
            "AND (bkch.type IS NULL OR (:#{#param.type} IS NULL OR bkch.type = :#{#param.type}))" +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND ((:#{#param.maDvi} IS NULL OR LOWER(qdc.maDvi) LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND ((:#{#param.type} IS NULL OR qdc.type = :#{#param.type=='01'? 'NDC':'DC'})) " +
            "AND ((qdc.loaiDc= 'DCNB' OR qdc.loaiDc= 'CHI_CUC') OR  ((:#{#param.typeQd} IS NULL OR qdc.loaiQdinh = :#{#param.typeQd})))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.trangThai} IS NULL OR bkch.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(bkch.soBangKe) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND (:#{#param.maLoKho} IS NULL OR bkch.maLoKho = :#{#param.maLoKho}) " +
            "AND (:#{#param.maNganKho} IS NULL OR bkch.maNganKho = :#{#param.maNganKho}) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR pxk.ngayXuatKho >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR pxk.ngayXuatKho <= :#{#param.denNgay}) ) " +
            "GROUP BY bkch.id,qdc.id,pxk.id,qdc.soQdinh,qdc.ngayKyQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKhoNhan,khdcd.tenDiemKhoNhan,khdcd.maLoKhoNhan," +
            "khdcd.tenLoKhoNhan,pxk.soPhieuXuatKho,bkch.soBangKe,pxk.ngayXuatKho,pnk.soPhieuNhapKho, pnk.ngayLap ,bkch.trangThai,bkch.trangThai,khdcd.loaiVthh," +
            "khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKhoNhan,khdcd.tenNhaKhoNhan,khdcd.donViTinh," +
            "khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan,pxk.nguoiGiaoHang,pxk.soCmt,pxk.ctyNguoiGh,pxk.diaChi,pxk.thoiGianGiaoNhan,khdcd.id " +
            "ORDER BY qdc.soQdinh DESC")
    List<DcnbBangKeCanHangHdrDTO> searchListNhan(@Param("param") SearchBangKeCanHang req);
    Optional<DcnbBangKeCanHangHdr> findByMaDvi (String maDvi);
}
