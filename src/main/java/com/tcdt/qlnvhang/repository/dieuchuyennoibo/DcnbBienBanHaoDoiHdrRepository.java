package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbBienBanHaoDoi;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbBienBanTinhKho;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanHaoDoiHdrDTO;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanTinhKhoHdrDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanHaoDoiHdr;
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
public interface DcnbBienBanHaoDoiHdrRepository extends JpaRepository<DcnbBienBanHaoDoiHdr, Long> {

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBienBanHaoDoiHdrDTO(" +
            "bbhd.id,bkch.id,bbtk.id,qdc.id,pxk.id,qdc.soQdinh,qdc.nam,qdc.ngayHieuLuc,pxk.maDiemKho,pxk.tenDiemKho,pxk.maLoKho,pxk.tenLoKho," +
            "bbhd.soBienBan,pxk.soPhieuXuatKho,bkch.soBangKe,pxk.ngayXuatKho,bbhd.trangThai,bbhd.trangThai,pxk.loaiVthh,pxk.tenLoaiVthh,pxk.cloaiVthh,pxk.tenCloaiVthh,pxk.maNhaKho,pxk.tenNhaKho,pxk.donViTinh,khdcd.tenDonViTinh," +
            "pxk.maNganKho,pxk.tenNganKho,qdc.ngayKyQdinh) FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbBienBanHaoDoiHdr bbhd ON bbhd.qDinhDccId = qdc.id "+
            "LEFT JOIN DcnbBienBanTinhKhoHdr bbtk ON bbtk.qDinhDccId = qdc.id "+
            "LEFT JOIN DcnbBangKeCanHangHdr bkch ON bkch.qDinhDccId = qdc.id "+
            "LEFT JOIN DcnbPhieuXuatKhoHdr pxk ON pxk.qddcId = qdc.id " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "WHERE 1 = 1 "+
            "AND qdc.trangThai = '29' AND qdc.loaiDc = :#{#param.loaiDc} AND (:#{#param.type} IS NULL OR bbtk.type = :#{#param.type})"+
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}) OR (:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))"+
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBbHaoDoi} IS NULL OR LOWER(bbhd.soBienBan) LIKE CONCAT('%',LOWER(:#{#param.soBbHaoDoi}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(bbhd.soQdinhDcc) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR bbtk.ngayBatDauXuat >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR bbtk.ngayBatDauXuat <= :#{#param.denNgay}) ) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR bbtk.ngayKeThucXuat >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR bbtk.ngayKeThucXuat <= :#{#param.denNgay}) ) " +
            "ORDER BY bbhd.soQdinhDcc desc, bbhd.nam desc")
    Page<DcnbBienBanHaoDoiHdrDTO> searchPage(@Param("param") SearchDcnbBienBanHaoDoi req, Pageable pageable);

    Optional<DcnbBienBanHaoDoiHdr> findFirstBySoBienBan(String soBbHaoDoi);

    List<DcnbBienBanHaoDoiHdr> findByIdIn(List<Long> ids);

    List<DcnbBienBanHaoDoiHdr> findAllByIdIn(List<Long> idList);
}
