package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBBKetThucNKReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBBKetThucNKHdrDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBKetThucNKHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DcnbBBKetThucNKHdrRepository extends JpaRepository<DcnbBBKetThucNKHdr, Long> {
    Optional<DcnbBBKetThucNKHdr> findFirstBySoBb(String soBb);
    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBBKetThucNKHdrDTO(" +
            "bbkt.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKho,khdcd.tenNhaKho, khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho,khdcd.loaiVthh,khdcd.tenLoaiVthh, khdcd.cloaiVthh, khdcd.tenCloaiVthh,khdcd.donViTinh, khdcd.tenDonViTinh ," +
            "bbkt.soBb, bbkt.ngayKetThucNhap, hskt.soHoSoKyThuat,hskt.id, bbktd.soPhieuNhapKho, bbktd.phieuNhapKhoId,bbktd.ngayNhap, bblm.soBbLayMau,bblm.id," +
            "bblm.soBbLayMau,bblm.id, bbkt.trangThai, bbkt.trangThai) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbBBKetThucNKHdr bbkt On bbkt.qDinhDccId = qdc.id " +
            "LEFT JOIN DcnbBBKetThucNKDtl bbktd On bbktd.hdrId = bbkt.id " +
            "LEFT JOIN DcnbHoSoKyThuatHdr hskt On hskt.idQdnh = qdc.id " +
            "LEFT JOIN DcnbBienBanLayMauHdr bblm On bblm.qdccId = qdc.id " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29'" +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "ORDER BY bbkt.soBb desc, bbkt.nam desc")
    Page<DcnbBBKetThucNKHdrDTO> searchPageChiCuc(@Param("param")DcnbBBKetThucNKReq req, Pageable pageable);
    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBBKetThucNKHdrDTO(" +
            "bbkt.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKho,khdcd.tenNhaKho, khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho,khdcd.loaiVthh,khdcd.tenLoaiVthh, khdcd.cloaiVthh, khdcd.tenCloaiVthh,khdcd.donViTinh, khdcd.tenDonViTinh ," +
            "bbkt.soBb, bbkt.ngayKetThucNhap, hskt.soHoSoKyThuat,hskt.id, bbktd.soPhieuNhapKho, bbktd.phieuNhapKhoId,bbktd.ngayNhap, bblm.soBbLayMau,bblm.id," +
            "bblm.soBbLayMau,bblm.id, bbkt.trangThai, bbkt.trangThai) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbDataLinkHdr dtlh On dtlh.qdCcParentId = qdc.id " +
            "LEFT JOIN DcnbBBKetThucNKHdr bbkt On bbkt.qDinhDccId = qdc.id " +
            "LEFT JOIN DcnbBBKetThucNKDtl bbktd On bbktd.hdrId = bbkt.id " +
            "LEFT JOIN DcnbHoSoKyThuatHdr hskt On hskt.idQdnh = qdc.id " +
            "LEFT JOIN DcnbBienBanLayMauHdr bblm On bblm.qdccId = qdc.id " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29'" +
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) " +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "ORDER BY bbkt.soBb desc, bbkt.nam desc")
    Page<DcnbBBKetThucNKHdrDTO> searchPageCuc(@Param("param")DcnbBBKetThucNKReq req, Pageable pageable);
}
