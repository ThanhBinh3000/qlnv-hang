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
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbBienBanTinhKhoHdr bbtk ON bbtk.qDinhDccId = qdc.id " +
            "and ((khdcd.maLoKho is not null and  bbtk.maLoKho = khdcd.maLoKho and bbtk.maNganKho = khdcd.maNganKho ) or (khdcd.maLoKho is null and bbtk.maNganKho = khdcd.maNganKho))" +
            "LEFT JOIN DcnbBienBanTinhKhoDtl bbtkd ON bbtkd.hdrId = bbtk.id " +
            "LEFT JOIN DcnbBangKeCanHangHdr bkch ON bkch.qDinhDccId = qdc.id " +
            "and ((khdcd.maLoKho is not null and bkch.maLoKho = khdcd.maLoKho and bkch.maNganKho = khdcd.maNganKho and bkch.trangThai = '17' ) or (khdcd.maLoKho is null and bkch.maNganKho = khdcd.maNganKho and bkch.trangThai = '17'))" +
            "LEFT JOIN DcnbPhieuXuatKhoHdr pxk ON pxk.id = bbtkd.phieuXuatKhoHdrId and bkch.trangThai = '17' " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.parentId is not null and qdc.trangThai = '29' " +
            "AND ((:#{#param.loaiDc} IS NULL OR qdc.loaiDc = :#{#param.loaiDc}))" +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND (bbtk.type IS NULL OR (:#{#param.type} IS NULL OR bbtk.type = :#{#param.type}))" +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND (qdc.loaiDc= 'DCNB' OR  ((:#{#param.typeQd} IS NULL OR qdc.type = :#{#param.typeQd})))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.trangThai} IS NULL OR bbtk.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.soBbTinhKho} IS NULL OR LOWER(bbtk.soBbTinhKho) LIKE CONCAT('%',LOWER(:#{#param.soBbTinhKho}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(bbtk.soQdinhDcc) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgayBdXuat}  IS NULL OR bbtk.ngayBatDauXuat >= :#{#param.tuNgayBdXuat})" +
            "AND (:#{#param.denNgayBdXuat}  IS NULL OR bbtk.ngayBatDauXuat <= :#{#param.denNgayBdXuat}) ) " +
            "AND ((:#{#param.tuNgayKtXuat}  IS NULL OR bbtk.ngayKeThucXuat >= :#{#param.tuNgayKtXuat})" +
            "AND (:#{#param.denNgayKtXuat}  IS NULL OR bbtk.ngayKeThucXuat <= :#{#param.denNgayKtXuat}) ) " +
            "GROUP BY bbtk.id,qdc.id,pxk.id,bkch.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,bbtk.soBbTinhKho,bbtk.ngayBatDauXuat, bbtk.ngayKeThucXuat,pxk.soPhieuXuatKho,bkch.soBangKe,pxk.ngayXuatKho," +
            "bbtk.trangThai,bbtk.trangThai,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKho,khdcd.tenNhaKho," +
            "khdcd.maNganKho,khdcd.tenNganKho,qdc.ngayHieuLuc,qdc.ngayKyQdinh "+
            "ORDER BY qdc.soQdinh DESC")
    Page<DcnbBienBanTinhKhoHdrDTO> searchPage(@Param("param") SearchDcnbBienBanTinhKho req, Pageable pageable);

    Optional<DcnbBienBanTinhKhoHdr> findFirstBySoBbTinhKho(String soBbTinhKho);

    List<DcnbBienBanTinhKhoHdr> findByIdIn(List<Long> ids);

    List<DcnbBienBanTinhKhoHdr> findAllByIdIn(List<Long> idList);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanTinhKhoHdrDTO(" +
            "bbtk.id,qdc.id,pxk.id,bkch.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,bbtk.soBbTinhKho,bbtk.ngayBatDauXuat, bbtk.ngayKeThucXuat,pxk.soPhieuXuatKho,bkch.soBangKe,pxk.ngayXuatKho," +
            "bbtk.trangThai,bbtk.trangThai,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKho,khdcd.tenNhaKho," +
            "khdcd.maNganKho,khdcd.tenNganKho,qdc.ngayHieuLuc,qdc.ngayKyQdinh) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbBienBanTinhKhoHdr bbtk ON bbtk.qDinhDccId = qdc.id " +
            "LEFT JOIN DcnbBienBanTinhKhoDtl bbtkd ON bbtkd.hdrId = bbtk.id " +
            "LEFT JOIN DcnbBangKeCanHangHdr bkch ON bkch.qDinhDccId = qdc.id " +
            "LEFT JOIN DcnbPhieuXuatKhoHdr pxk ON pxk.id = bbtkd.phieuXuatKhoHdrId " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29' " +
            "AND ((:#{#param.loaiDc} IS NULL OR qdc.loaiDc = :#{#param.loaiDc}))" +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND (bbtk.type IS NULL OR (:#{#param.type} IS NULL OR bbtk.type = :#{#param.type}))" +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND (qdc.loaiDc= 'DCNB' OR  ((:#{#param.typeQd} IS NULL OR qdc.type = :#{#param.typeQd})))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.trangThai} IS NULL OR bbtk.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.soBbTinhKho} IS NULL OR LOWER(bbtk.soBbTinhKho) LIKE CONCAT('%',LOWER(:#{#param.soBbTinhKho}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(bbtk.soQdinhDcc) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND (:#{#param.qdinhDccId} IS NULL OR bbtk.qDinhDccId = :#{#param.qdinhDccId}) " +
            "AND (:#{#param.maLoKho} IS NULL OR LOWER(bbtk.maLoKho) LIKE CONCAT('%',LOWER(:#{#param.maLoKho}),'%')) " +
            "AND (:#{#param.maNganKho} IS NULL OR LOWER(bbtk.maNganKho) LIKE CONCAT('%',LOWER(:#{#param.maNganKho}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgayBdXuat}  IS NULL OR bbtk.ngayBatDauXuat >= :#{#param.tuNgayBdXuat})" +
            "AND (:#{#param.denNgayBdXuat}  IS NULL OR bbtk.ngayBatDauXuat <= :#{#param.denNgayBdXuat}) ) " +
            "AND ((:#{#param.tuNgayKtXuat}  IS NULL OR bbtk.ngayKeThucXuat >= :#{#param.tuNgayKtXuat})" +
            "AND (:#{#param.denNgayKtXuat}  IS NULL OR bbtk.ngayKeThucXuat <= :#{#param.denNgayKtXuat}) ) " +
            "AND ((:#{#param.tuNgayXhXuat}  IS NULL OR bbtk.thoiHanXuatHang >= :#{#param.tuNgayXhXuat})" +
            "AND (:#{#param.denNgayXhXuat}  IS NULL OR bbtk.thoiHanXuatHang <= :#{#param.denNgayXhXuat}) ) " +
            "GROUP BY bbtk.id,qdc.id,pxk.id,bkch.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,bbtk.soBbTinhKho,bbtk.ngayBatDauXuat, bbtk.ngayKeThucXuat,pxk.soPhieuXuatKho,bkch.soBangKe,pxk.ngayXuatKho," +
            "bbtk.trangThai,bbtk.trangThai,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKho,khdcd.tenNhaKho," +
            "khdcd.maNganKho,khdcd.tenNganKho,qdc.ngayHieuLuc,qdc.ngayKyQdinh "+
            "ORDER BY qdc.soQdinh DESC")
    List<DcnbBienBanTinhKhoHdrDTO> searchList(@Param("param") SearchDcnbBienBanTinhKho req);
}
