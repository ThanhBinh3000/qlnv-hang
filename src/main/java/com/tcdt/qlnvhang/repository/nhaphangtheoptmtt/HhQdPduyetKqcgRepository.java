package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.request.nhaphangtheoptt.SearchHhQdPduyetKqcg;
import com.tcdt.qlnvhang.table.HhQdPduyetKqlcntHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdPduyetKqcgHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HhQdPduyetKqcgRepository extends JpaRepository<HhQdPduyetKqcgHdr, Long> {


    @Query("SELECT DISTINCT QD FROM HhQdPduyetKqcgHdr QD " +
            " LEFT JOIN HhQdPheduyetKhMttDx DTL on DTL.id = QD.idPdKhDtl " +
            " WHERE 1 = 1 " +
            "AND (:#{#param.namKh} IS NULL OR QD.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.ngayCgiaTu} IS NULL OR DTL.ngayNhanCgia >= :#{#param.ngayCgiaTu}) " +
            "AND (:#{#param.ngayCgiaDen} IS NULL OR DTL.ngayNhanCgia <= :#{#param.ngayCgiaDen}) " +
            "AND (:#{#param.loaiVthh } IS NULL OR LOWER(QD.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.maDvi} IS NULL OR QD.maDvi = :#{#param.maDvi}) "
    )
    Page<HhQdPduyetKqcgHdr> searchPage(@Param("param") SearchHhQdPduyetKqcg param, Pageable pageable);

    Optional<HhQdPduyetKqcgHdr> findBySoQdKq(String soQdKq);
}
