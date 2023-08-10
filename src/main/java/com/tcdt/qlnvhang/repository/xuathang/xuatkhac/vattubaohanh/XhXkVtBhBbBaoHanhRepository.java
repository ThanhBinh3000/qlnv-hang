package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh;


import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhBbBaoHanhRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhBbBaoHanh;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhXkVtBhBbBaoHanhRepository extends JpaRepository<XhXkVtBhBbBaoHanh, Long> {

    @Query("SELECT c FROM XhXkVtBhBbBaoHanh c WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBienBan} IS NULL OR LOWER(c.soBienBan) LIKE CONCAT('%',LOWER(:#{#param.soBienBan}),'%')) " +
            "AND (:#{#param.soCanCu} IS NULL OR LOWER(c.soCanCu) LIKE CONCAT('%',LOWER(:#{#param.soCanCu}),'%')) " +
            "AND ((:#{#param.ngayKdclTu}  IS NULL OR c.ngayKdcl >= :#{#param.ngayKdclTu})" +
            "AND (:#{#param.ngayKdclDen}  IS NULL OR c.ngayKdcl <= :#{#param.ngayKdclDen}) ) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
    )
    Page<XhXkVtBhBbBaoHanh> search(@Param("param") XhXkVtBhBbBaoHanhRequest param, Pageable pageable);

    Optional<XhXkVtBhBbBaoHanh> findBySoBienBan(String soBienBan);

    List<XhXkVtBhBbBaoHanh> findByIdIn(List<Long> ids);

    List<XhXkVtBhBbBaoHanh> findAllByIdIn(List<Long> idList);

}
