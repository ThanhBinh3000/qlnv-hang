package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.SearchXhTlToChuc;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlToChucHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhTlToChucRepository extends JpaRepository<XhTlToChucHdr, Long> {

    @Query("SELECT DISTINCT  TC FROM XhTlToChucHdr TC " +
            "LEFT JOIN XhTlQuyetDinhHdr QD on TC.idQdTl = QD.id WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR TC.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.nam} IS NULL OR QD.nam = :#{#param.nam}) " +
            "AND (:#{#param.trangThai} IS NULL OR TC.trangThai = :#{#param.trangThai}) "
    )
    Page<XhTlToChucHdr> search(@Param("param") SearchXhTlToChuc param, Pageable pageable);

    List<XhTlToChucHdr> findByIdIn(List<Long> ids);

    List<XhTlToChucHdr> findByIdQdTlDtlOrderByLanDauGia(Long idQdTlDtl);
}
