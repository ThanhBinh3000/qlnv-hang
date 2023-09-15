package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbBienBanHaoDoi;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanHaoDoiHdrDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanHaoDoiHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbBienBanHaoDoiHdrRepository extends JpaRepository<DcnbBienBanHaoDoiHdr, Long> {

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanHaoDoiHdrDTO(" +
            "bbhd.id,pxk.bangKeChId,bbtk.soBbTinhKho,bbtk.id,qdc.id,pxk.id,qdc.soQdinh,qdc.nam,qdc.ngayHieuLuc,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho,khdcd.tenLoKho," +
            "bbhd.soBienBan,pxk.soPhieuXuatKho,pxk.soBangKeCh,pxk.ngayXuatKho,bbhd.trangThai,bbhd.trangThai,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKho,khdcd.tenNhaKho,khdcd.donViTinh," +
            "khdcd.maNganKho,khdcd.tenNganKho,qdc.ngayKyQdinh,khdcd.id) FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN DcnbBienBanHaoDoiHdr bbhd ON bbhd.keHoachDcDtlId = khdcd.id " +
            "LEFT JOIN DcnbBienBanTinhKhoHdr bbtk ON bbtk.qDinhDccId = qdc.id " +
            "and ((khdcd.maLoKho is not null and bbtk.maLoKho = khdcd.maLoKho and bbtk.maNganKho = khdcd.maNganKho and bbtk.trangThai = '17' ) or (khdcd.maLoKhoNhan is null and bbtk.maNganKho = khdcd.maNganKho and bbtk.trangThai = '17' ))" +
            "LEFT JOIN DcnbBienBanTinhKhoDtl bbtkd ON bbtkd.hdrId = bbtk.id " +
            "LEFT JOIN DcnbPhieuXuatKhoHdr pxk ON pxk.id = bbtkd.phieuXuatKhoHdrId " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 = 1 " +
            "AND qdc.parentId is not null and qdc.trangThai = '29' " +
            "AND ((:#{#param.loaiDc} IS NULL OR qdc.loaiDc = :#{#param.loaiDc}))" +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND ((:#{#param.maDvi} IS NULL OR LOWER(qdc.maDvi) LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND (qdc.loaiDc= 'DCNB' OR  ((:#{#param.typeQd} IS NULL OR qdc.type = :#{#param.typeQd})))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.trangThai} IS NULL OR bbhd.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.soBbHaoDoi} IS NULL OR LOWER(bbhd.soBienBan) LIKE CONCAT('%',LOWER(:#{#param.soBbHaoDoi}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgayLapBb}  IS NULL OR bbhd.ngayLap >= :#{#param.tuNgayLapBb})" +
            "AND (:#{#param.denNgayLapBb}  IS NULL OR bbhd.ngayLap <= :#{#param.denNgayLapBb}) ) " +
            "AND ((:#{#param.tuNgayBdXuat}  IS NULL OR bbtk.ngayBatDauXuat >= :#{#param.tuNgayBdXuat})" +
            "AND (:#{#param.denNgayBdXuat}  IS NULL OR bbtk.ngayBatDauXuat <= :#{#param.denNgayBdXuat}) ) " +
            "AND ((:#{#param.tuNgayKtXuat}  IS NULL OR bbtk.ngayKetThucXuat >= :#{#param.tuNgayKtXuat})" +
            "AND (:#{#param.denNgayKtXuat}  IS NULL OR bbtk.ngayKetThucXuat <= :#{#param.denNgayKtXuat}) ) " +
            "AND ((:#{#param.tuNgayXhXuat}  IS NULL OR bbtk.thoiHanXuatHang >= :#{#param.tuNgayXhXuat})" +
            "AND (:#{#param.denNgayXhXuat}  IS NULL OR bbtk.thoiHanXuatHang <= :#{#param.denNgayXhXuat}) ) " +
            "GROUP BY bbhd.id,pxk.bangKeChId,bbtk.soBbTinhKho,bbtk.id,qdc.id,pxk.id,qdc.soQdinh,qdc.nam,qdc.ngayHieuLuc,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho,khdcd.tenLoKho," +
            "bbhd.soBienBan,pxk.soPhieuXuatKho,pxk.soBangKeCh,pxk.ngayXuatKho,bbhd.trangThai,bbhd.trangThai,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKho,khdcd.tenNhaKho,khdcd.donViTinh," +
            "khdcd.maNganKho,khdcd.tenNganKho,qdc.ngayKyQdinh,khdcd.id " +
            "ORDER BY qdc.soQdinh DESC")
    Page<DcnbBienBanHaoDoiHdrDTO> searchPage(@Param("param") SearchDcnbBienBanHaoDoi req, Pageable pageable);

    Optional<DcnbBienBanHaoDoiHdr> findFirstBySoBienBan(String soBbHaoDoi);

    List<DcnbBienBanHaoDoiHdr> findByIdIn(List<Long> ids);

    List<DcnbBienBanHaoDoiHdr> findAllByIdIn(List<Long> idList);
}
