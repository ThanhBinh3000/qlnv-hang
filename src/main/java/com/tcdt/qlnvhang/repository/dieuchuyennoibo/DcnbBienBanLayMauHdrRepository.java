package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbBienBanLayMau;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanLayMauHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbBienBanLayMauHdrRepository extends JpaRepository<DcnbBienBanLayMauHdr, Long> {

    @Query(value = "SELECT distinct hdr FROM DcnbBienBanLayMauHdr hdr WHERE 1=1 " +
            "AND (:#{#param.maDvi} IS NULL OR hdr.maDvi LIKE CONCAT(:#{#param.maDvi},'%')) " +
            "AND (:#{#param.DViKiemNghiem} IS NULL OR hdr.dViKiemNghiem LIKE CONCAT(:#{#param.DViKiemNghiem},'%')) " +
            "AND (:#{#param.nam} IS NULL OR hdr.nam = :#{#param.nam}) " +
            "AND LOWER(hdr.soBbLayMau) LIKE CONCAT('%',LOWER(:#{#param.soBbLayMau}),'%') " +
            "AND LOWER(hdr.soQdinhDcc) LIKE CONCAT('%',LOWER(:#{#param.soBbLayMau}),'%') " +
            "AND ((:#{#param.tuNgay}  IS NULL OR hdr.ngayLayMau >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR hdr.ngayLayMau <= :#{#param.denNgay}) ) " +
            "GROUP BY hdr.soQdinhDcc, hdr.nam")
    Page<DcnbBienBanLayMauHdr> search(@Param("param")SearchDcnbBienBanLayMau param, Pageable pageable);

    Optional<DcnbBienBanLayMauHdr> findFirstBySoBbLayMau (String soBbLayMau);

    List<DcnbBienBanLayMauHdr> findByIdIn(List<Long> ids);

    List<DcnbBienBanLayMauHdr> findAllByIdIn(List<Long> idList);
}
