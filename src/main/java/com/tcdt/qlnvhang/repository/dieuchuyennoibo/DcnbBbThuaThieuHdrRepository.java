package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbThuaThieuHdrReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbThuaThieuHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DcnbBbThuaThieuHdrRepository extends JpaRepository<DcnbBbThuaThieuHdr, Long> {
    @Query(value = "SELECT distinct c FROM DcnbBbThuaThieuHdr c LEFT JOIN QlnvDmDonvi dvi ON dvi.maDvi = c.maDvi WHERE 1=1 " +
            "AND (:#{#param.soBb} IS NULL OR LOWER(c.soBb) LIKE CONCAT('%',LOWER(:#{#param.soBb}),'%')) " +
            "AND ((:#{#param.maDvi} IS NULL OR LOWER(c.maDvi) LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%'))) " +
            "AND ((:#{#param.nam} IS NULL OR c.nam = :#{#param.nam})) " +
            "AND ((:#{#param.soBcKetQuaDc} IS NULL OR LOWER(c.soBcKetQuaDc)  LIKE CONCAT('%',LOWER(:#{#param.soBcKetQuaDc}),'%'))) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "AND ((:#{#param.tuNgayLap}  IS NULL OR c.ngayLap >= :#{#param.tuNgayLap})" +
            "AND (:#{#param.denNgayLap}  IS NULL OR c.ngayLap <= :#{#param.denNgayLap}) ) " +
            "AND (:#{#param.soQdDcCuc} IS NULL OR LOWER(c.soQdDcCuc) LIKE CONCAT('%',LOWER(:#{#param.soQdDcCuc}),'%')) " +
            "AND ((:#{#param.tuNgayBc}  IS NULL OR c.ngayLapBcKetQuaDc >= :#{#param.tuNgayBc})" +
            "AND (:#{#param.denNgayBc}  IS NULL OR c.ngayLapBcKetQuaDc <= :#{#param.denNgayBc}) ) " +
            "ORDER BY c.soQdDcCuc desc , c.nam desc, c.id desc")
    Page<DcnbBbThuaThieuHdr> searchPage(@Param("param") DcnbBbThuaThieuHdrReq req, Pageable pageable);
}
