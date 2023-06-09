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
            "bkch.id,pxk.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,pxk.soPhieuXuatKho,bkch.soBangKe,pxk.ngayXuatKho,bkch.trangThai,bkch.trangThai,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh) FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbBangKeCanHangHdr bkch ON bkch.qDinhDccId = qdc.id "+
            "LEFT JOIN DcnbPhieuXuatKhoHdr pxk ON pxk.qddcId = qdc.id " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "WHERE 1 =1 "+
            "AND qdc.trangThai = '29'"+
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}) OR (:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))"+
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(bkch.soBangKe) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(bkch.soQdinhDcc) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR pxk.ngayXuatKho >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR pxk.ngayXuatKho <= :#{#param.denNgay}) ) " +
            "ORDER BY bkch.soQdinhDcc desc, bkch.nam desc")
    Page<DcnbBangKeCanHangHdrDTO> searchPage(@Param("param")SearchBangKeCanHang req, Pageable pageable);

    Optional<DcnbBangKeCanHangHdr> findFirstBySoBangKe(String soBangKe);


    List<DcnbBangKeCanHangHdr> findByIdIn(List<Long> ids);

    List<DcnbBangKeCanHangHdr> findAllByIdIn(List<Long> idList);
}
