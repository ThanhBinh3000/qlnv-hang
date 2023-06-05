package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchBangKeCanHang;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchPhieuXuatKho;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeCanHangHdr;
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

    @Query(value = "SELECT distinct c FROM DcnbPhieuXuatKhoHdr c LEFT JOIN QlnvDmDonvi dvi ON dvi.maDvi = c.maDvi WHERE 1=1 " +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(c.soPhieuXuatKho) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
//            "AND ((:#{#param.maDvi} IS NULL OR c.maDvi = :#{#param.maDvi}) " +
//            "OR (:#{#param.maDvi} IS NULL OR dvi.parent.maDvi = :#{#param.maDvi}))" +
//            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
//            "AND ((:#{#param.tuNgay}  IS NULL OR c.ngayXuatKho >= :#{#param.tuNgay})" +
//            "AND (:#{#param.denNgay}  IS NULL OR c.ngayXuatKho <= :#{#param.denNgay}) ) " +
//            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(c.soQdinhDcc) LIKE CONCAT('%',LOWER(:#{#param.soBbLayMau}),'%')) " +
//            "AND (:#{#param.loaiDc} IS NULL OR c.loaiDc = :#{#param.loaiDc}) " +
            "ORDER BY c.soPhieuXuatKho desc , c.nam desc, c.id desc")
    Page<DcnbPhieuXuatKhoHdr> search(@Param("param") SearchPhieuXuatKho req, Pageable pageable);

    Optional<DcnbPhieuXuatKhoHdr> findBySoPhieuXuatKho(String soPhieuXuatKho);
//
//
    List<DcnbPhieuXuatKhoHdr> findByIdIn(List<Long> ids);
//
//    List<DcnbBangKeCanHangHdr> findAllByIdIn(List<Long> idList);
}
