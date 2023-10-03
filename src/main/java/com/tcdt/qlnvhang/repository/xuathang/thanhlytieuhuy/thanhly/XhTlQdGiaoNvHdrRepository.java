package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlQdGiaoNvHdrReq;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQdGiaoNvHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhTlQdGiaoNvHdrRepository extends JpaRepository<XhTlQdGiaoNvHdr, Long> {

    @Query("SELECT c FROM XhTlQdGiaoNvHdr c " +
            " WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBbQd} IS NULL OR LOWER(c.soBbQd) LIKE CONCAT('%',LOWER(:#{#param.soBbQd}),'%')) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(c.trichYeu) LIKE CONCAT('%',LOWER(:#{#param.trichYeu}),'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<XhTlQdGiaoNvHdr> search(@Param("param") XhTlQdGiaoNvHdrReq param, Pageable pageable);

    Optional<XhTlQdGiaoNvHdr> findBySoBbQd(String soBbQd);

    List<XhTlQdGiaoNvHdr> findByIdIn(List<Long> ids);

    List<XhTlQdGiaoNvHdr> findAllByIdIn(List<Long> listId);
}