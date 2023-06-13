package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBBNTBQHdrReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBBNTBQHdrDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DcnbBBNTBQHdrRepository extends JpaRepository<DcnbBBNTBQHdr, Long> {

    @Query(value = "SELECT distinct c FROM DcnbPhieuXuatKhoHdr c " +
            " LEFT JOIN QlnvDmDonvi dvi ON dvi.maDvi = c.maDvi " +
            " WHERE 1=1 " +
            " AND (:#{#param.soBangKe} IS NULL OR LOWER(c.soPhieuXuatKho) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
            " ORDER BY c.soPhieuXuatKho desc , c.nam desc, c.id desc")
    Page<DcnbBBNTBQHdr> search(@Param("param") DcnbBBNTBQHdrReq req, Pageable pageable);

    //
    Optional<DcnbBBNTBQHdr> findBySoBban(String soBban);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBBNTBQHdrDTO(" +
            "bblm.id, qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho, khdcd.maDiemKhoNhan, khdcd.tenDiemKhoNhan, khdcd.maLoKhoNhan, khdcd.tenLoKhoNhan,khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan, bblm.soBban," +
            "bblm.ngayLap,bblm.ngayKetThucNt , bblm.tongKinhPhiDaTh,bblm.tongKinhPhiDaTh ,bblm.trangThai, bblm.trangThai) FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbBBNTBQHdr bblm On bblm.qdDcCucId = qdc.id " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29'" +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}) OR (:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "ORDER BY bblm.soQdDcCuc desc, bblm.nam desc")
    Page<DcnbBBNTBQHdrDTO> searchPageChiCuc(@Param("param") DcnbBBNTBQHdrReq req, Pageable pageable);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBBNTBQHdrDTO(" +
            "bblm.id, qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho, khdcd.maDiemKhoNhan, khdcd.tenDiemKhoNhan, khdcd.maLoKhoNhan, khdcd.tenLoKhoNhan,khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan, bblm.soBban," +
            "bblm.ngayLap,bblm.ngayKetThucNt , bblm.tongKinhPhiDaTh,bblm.tongKinhPhiDaTh ,bblm.trangThai, bblm.trangThai) FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbQuyetDinhDcCHdr qdc2 On qdc2.id = qdc.parentId " +
            "LEFT JOIN DcnbBBNTBQHdr bblm On bblm.qdDcCucId = qdc.id " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "WHERE 1 =1 " +
            "AND qdc.trangThai = '29'" +
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}) OR (:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))" +
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND qdc.type IS NULL " +
            "ORDER BY bblm.soQdDcCuc desc, bblm.nam desc")
    Page<DcnbBBNTBQHdrDTO> searchPage(@Param("param") DcnbBBNTBQHdrReq req, Pageable pageable);
}
