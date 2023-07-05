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
            "bbhd.id,bkch.id,bbtk.id,qdc.id,pxk.id,qdc.soQdinh,qdc.nam,qdc.ngayHieuLuc,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho,khdcd.tenLoKho," +
            "bbhd.soBienBan,pxk.soPhieuXuatKho,bkch.soBangKe,pxk.ngayXuatKho,bbhd.trangThai,bbhd.trangThai,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKho,khdcd.tenNhaKho,khdcd.donViTinh,khdcd.tenDonViTinh," +
            "khdcd.maNganKho,khdcd.tenNganKho,qdc.ngayKyQdinh) FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbBienBanHaoDoiHdr bbhd ON bbhd.qDinhDccId = qdc.id "+
            "LEFT JOIN DcnbBienBanTinhKhoHdr bbtk ON bbtk.qDinhDccId = qdc.id "+
            "LEFT JOIN DcnbBangKeCanHangHdr bkch ON bkch.qDinhDccId = qdc.id and bbtk.bangKeCanHangId = bkch.id "+
            "LEFT JOIN DcnbPhieuXuatKhoHdr pxk ON pxk.id = bbtk.phieuXuatKhoId " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 = 1 "+
            "AND qdc.trangThai = '29' AND qdc.loaiDc = :#{#param.loaiDc} "+
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) "+
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))"+
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBbHaoDoi} IS NULL OR LOWER(bbhd.soBienBan) LIKE CONCAT('%',LOWER(:#{#param.soBbHaoDoi}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgayLapBb}  IS NULL OR bbhd.ngayLap >= :#{#param.tuNgayLapBb})" +
            "AND (:#{#param.denNgayLapBb}  IS NULL OR bbhd.ngayLap <= :#{#param.denNgayLapBb}) ) " +
            "AND ((:#{#param.tuNgayBdXuat}  IS NULL OR bbtk.ngayBatDauXuat >= :#{#param.tuNgayBdXuat})" +
            "AND (:#{#param.denNgayBdXuat}  IS NULL OR bbtk.ngayBatDauXuat <= :#{#param.denNgayBdXuat}) ) " +
            "AND ((:#{#param.tuNgayKtXuat}  IS NULL OR bbtk.ngayKeThucXuat >= :#{#param.tuNgayKtXuat})" +
            "AND (:#{#param.denNgayKtXuat}  IS NULL OR bbtk.ngayKeThucXuat <= :#{#param.denNgayKtXuat}) ) " +
            "AND ((:#{#param.tuNgayXhXuat}  IS NULL OR bbtk.thoiHanXuatHang >= :#{#param.tuNgayXhXuat})" +
            "AND (:#{#param.denNgayXhXuat}  IS NULL OR bbtk.thoiHanXuatHang <= :#{#param.denNgayXhXuat}) ) " +
            "ORDER BY bbhd.soQdinhDcc desc, bbhd.nam desc")
    Page<DcnbBienBanHaoDoiHdrDTO> searchPageChiCuc(@Param("param") SearchDcnbBienBanHaoDoi req, Pageable pageable);

    Optional<DcnbBienBanHaoDoiHdr> findFirstBySoBienBan(String soBbHaoDoi);

    List<DcnbBienBanHaoDoiHdr> findByIdIn(List<Long> ids);

    List<DcnbBienBanHaoDoiHdr> findAllByIdIn(List<Long> idList);
    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanHaoDoiHdrDTO(" +
            "bbhd.id,bkch.id,bbtk.id,qdc.id,pxk.id,qdc.soQdinh,qdc.nam,qdc.ngayHieuLuc,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho,khdcd.tenLoKho," +
            "bbhd.soBienBan,pxk.soPhieuXuatKho,bkch.soBangKe,pxk.ngayXuatKho,bbhd.trangThai,bbhd.trangThai,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKho,khdcd.tenNhaKho,khdcd.donViTinh,khdcd.tenDonViTinh," +
            "khdcd.maNganKho,khdcd.tenNganKho,qdc.ngayKyQdinh) FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbDataLinkHdr dtlh On dtlh.qdCcParentId = qdc.id " +
            "LEFT JOIN DcnbBienBanHaoDoiHdr bbhd ON bbhd.qDinhDccId = dtlh.qdCcId "+
            "LEFT JOIN DcnbBienBanTinhKhoHdr bbtk ON bbtk.qDinhDccId = dtlh.qdCcId "+
            "LEFT JOIN DcnbBangKeCanHangHdr bkch ON bkch.qDinhDccId = dtlh.qdCcId and bbtk.bangKeCanHangId = bkch.id "+
            "LEFT JOIN DcnbPhieuXuatKhoHdr pxk ON pxk.qddcId = dtlh.qdCcId and pxk.id = bbtk.phieuXuatKhoId " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = dtlh.qdCcId " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 = 1 "+
            "AND qdc.trangThai = '29' AND qdc.loaiDc = :#{#param.loaiDc} "+
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) "+
            "AND (:#{#param.thayDoiThuKho} IS NULL OR khdcd.thayDoiThuKho = :#{#param.thayDoiThuKho}) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))"+
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBbHaoDoi} IS NULL OR LOWER(bbhd.soBienBan) LIKE CONCAT('%',LOWER(:#{#param.soBbHaoDoi}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(qdc.soQdinh) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgayLapBb}  IS NULL OR bbhd.ngayLap >= :#{#param.tuNgayLapBb})" +
            "AND (:#{#param.denNgayLapBb}  IS NULL OR bbhd.ngayLap <= :#{#param.denNgayLapBb}) ) " +
            "AND ((:#{#param.tuNgayBdXuat}  IS NULL OR bbtk.ngayBatDauXuat >= :#{#param.tuNgayBdXuat})" +
            "AND (:#{#param.denNgayBdXuat}  IS NULL OR bbtk.ngayBatDauXuat <= :#{#param.denNgayBdXuat}) ) " +
            "AND ((:#{#param.tuNgayKtXuat}  IS NULL OR bbtk.ngayKeThucXuat >= :#{#param.tuNgayKtXuat})" +
            "AND (:#{#param.denNgayKtXuat}  IS NULL OR bbtk.ngayKeThucXuat <= :#{#param.denNgayKtXuat}) ) " +
            "AND ((:#{#param.tuNgayXhXuat}  IS NULL OR bbtk.thoiHanXuatHang >= :#{#param.tuNgayXhXuat})" +
            "AND (:#{#param.denNgayXhXuat}  IS NULL OR bbtk.thoiHanXuatHang <= :#{#param.denNgayXhXuat}) ) " +
            "ORDER BY bbhd.soQdinhDcc desc, bbhd.nam desc")
    Page<DcnbBienBanHaoDoiHdrDTO> searchPageCuc(@Param("param") SearchDcnbBienBanHaoDoi req, Pageable pageable);
}
