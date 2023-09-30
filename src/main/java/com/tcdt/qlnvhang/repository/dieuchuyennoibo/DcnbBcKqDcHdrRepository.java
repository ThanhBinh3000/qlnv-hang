package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbKqDcSearch;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBcKqDcHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbBcKqDcHdrRepository extends JpaRepository<DcnbBcKqDcHdr, Long> {
    @Query(value = "SELECT distinct c FROM DcnbBcKqDcHdr c LEFT JOIN QlnvDmDonvi dvi ON dvi.maDvi = c.maDvi WHERE 1=1 " +
            "AND (:#{#param.soBc} IS NULL OR LOWER(c.soBc) LIKE CONCAT('%',LOWER(:#{#param.soBc}),'%')) " +
            "AND ((:#{#param.maDvi} IS NULL OR  LOWER(c.maDvi) LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%'))) " +
            "AND ((:#{#param.type} IS NULL OR c.type = :#{#param.type})) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR c.ngayBc >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR c.ngayBc <= :#{#param.denNgay}) ) " +
            "AND (:#{#param.soQdinhCuc} IS NULL OR LOWER(c.soQdDcCuc) LIKE CONCAT('%',LOWER(:#{#param.soQdinhCuc}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR c.ngayBc >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR c.ngayBc <= :#{#param.denNgay}) ) " +
            "ORDER BY c.soQdDcCuc desc , c.nam desc, c.id desc")
    Page<DcnbBcKqDcHdr> searchPage(@Param("param") DcnbBbKqDcSearch req, Pageable pageable);

    @Query(value = "SELECT distinct c FROM DcnbBcKqDcHdr c LEFT JOIN QlnvDmDonvi dvi ON dvi.maDvi = c.maDvi WHERE 1=1 " +
            "AND (:#{#param.soBc} IS NULL OR LOWER(c.soBc) LIKE CONCAT('%',LOWER(:#{#param.soBc}),'%')) " +
            "AND ((:#{#param.maDvi} IS NULL OR LOWER(c.maDvi) LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%'))) " +
            "AND ((:#{#param.type} IS NULL OR c.type = :#{#param.type})) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR c.ngayBc >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR c.ngayBc <= :#{#param.denNgay}) ) " +
            "AND (:#{#param.soQdinhCuc} IS NULL OR LOWER(c.soQdDcCuc) LIKE CONCAT('%',LOWER(:#{#param.soQdinhCuc}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR c.ngayBc >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR c.ngayBc <= :#{#param.denNgay}) ) " +
            "AND c.id not in (select tt.bcKetQuaDcId from DcnbBbThuaThieuHdr tt where 1 =1 AND (:#{#param.bbThuaThieuHdrId}  IS NULL OR tt.id != :#{#param.bbThuaThieuHdrId}) )  " +
            "ORDER BY c.soQdDcCuc desc , c.nam desc, c.id desc")
    List<DcnbBcKqDcHdr> searchList(@Param("param")DcnbBbKqDcSearch req);

    Optional<DcnbBcKqDcHdr> findFirstBySoBc(String soBc);
}
