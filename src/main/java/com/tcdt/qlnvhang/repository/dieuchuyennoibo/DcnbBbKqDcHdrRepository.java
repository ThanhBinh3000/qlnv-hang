package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbKqDcSearch;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbKqDcDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbKqDcHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbBbKqDcHdrRepository extends JpaRepository<DcnbBbKqDcHdr, Long> {
    @Query(value = "SELECT distinct c FROM DcnbBbKqDcHdr c LEFT JOIN QlnvDmDonvi dvi ON dvi.maDvi = c.maDvi WHERE 1=1 " +
            "AND (:#{#param.soBc} IS NULL OR LOWER(c.soBc) LIKE CONCAT('%',LOWER(:#{#param.soBc}),'%')) " +
            "AND ((:#{#param.maDvi} IS NULL OR c.maDvi = :#{#param.maDvi})) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR c.ngayBc >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR c.ngayBc <= :#{#param.denNgay}) ) " +
            "AND (:#{#param.soQdinhCuc} IS NULL OR LOWER(c.soQdDcCuc) LIKE CONCAT('%',LOWER(:#{#param.soQdinhCuc}),'%')) " +
            "ORDER BY c.soQdDcCuc desc , c.nam desc, c.id desc")
    Page<DcnbBbKqDcHdr> searchPage(@Param("param") DcnbBbKqDcSearch req, Pageable pageable);

    @Query(value = "SELECT distinct c FROM DcnbBbKqDcHdr c LEFT JOIN QlnvDmDonvi dvi ON dvi.maDvi = c.maDvi WHERE 1=1 " +
            "AND (:#{#param.soBc} IS NULL OR LOWER(c.soBc) LIKE CONCAT('%',LOWER(:#{#param.soBc}),'%')) " +
            "AND ((:#{#param.maDvi} IS NULL OR c.maDvi = :#{#param.maDvi})) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR c.ngayBc >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR c.ngayBc <= :#{#param.denNgay}) ) " +
            "AND (:#{#param.soQdinhCuc} IS NULL OR LOWER(c.soQdDcCuc) LIKE CONCAT('%',LOWER(:#{#param.soQdinhCuc}),'%')) " +
            "ORDER BY c.soQdDcCuc desc , c.nam desc, c.id desc")
    List<DcnbBbKqDcHdr> searchList(@Param("param")DcnbBbKqDcSearch req);

    Optional<DcnbBbKqDcHdr> findFirstBySoBc(String soBc);
}
