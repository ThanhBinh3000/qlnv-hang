package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchPhieuXuatKho;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuXuatKhoHdrDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuXuatKhoHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbPhieuXuatKhoHdrRepository extends JpaRepository<DcnbPhieuXuatKhoHdr, Long> {
    Optional<DcnbPhieuXuatKhoHdr> findBySoPhieuXuatKho(String soPhieuXuatKho);

    List<DcnbPhieuXuatKhoHdr> findByIdIn(List<Long> ids);
    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuXuatKhoHdrDTO(" +
            "pxk.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKho,khdcd.tenNhaKho, khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho, khdcd.thayDoiThuKho,pxk.soPhieuXuatKho,pxk.ngayXuatKho,pxk.soPhieuKtChatLuong,pxk.ngayKyPhieuKtChatLuong," +
            "pxk.trangThai, pxk.trangThai) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbPhieuXuatKhoHdr pxk On pxk.qddcId = qdc.id " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "WHERE 1 =1 "+
            "AND qdc.trangThai = '29' AND qdc.loaiDc = :#{#param.loaiDc} "+
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) "+
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))"+
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "ORDER BY pxk.soPhieuXuatKho desc, pxk.nam desc")
    Page<DcnbPhieuXuatKhoHdrDTO> searchPageChiCuc(@Param("param")SearchPhieuXuatKho req, Pageable pageable);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuXuatKhoHdrDTO(" +
            "pxk.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maNhaKho,khdcd.tenNhaKho, khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho, khdcd.thayDoiThuKho,pxk.soPhieuXuatKho,pxk.ngayXuatKho,pxk.soPhieuKtChatLuong,pxk.ngayKyPhieuKtChatLuong," +
            "pxk.trangThai, pxk.trangThai) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbPhieuXuatKhoHdr pxk On pxk.qddcId = qdc.id " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "WHERE 1 =1 "+
            "AND qdc.trangThai = '29' AND qdc.loaiDc = :#{#param.loaiDc} "+
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) "+
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))"+
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "ORDER BY pxk.soPhieuXuatKho desc, pxk.nam desc")
    Page<DcnbPhieuXuatKhoHdrDTO> searchPageCuc(@Param("param")SearchPhieuXuatKho req, Pageable pageable);
}
