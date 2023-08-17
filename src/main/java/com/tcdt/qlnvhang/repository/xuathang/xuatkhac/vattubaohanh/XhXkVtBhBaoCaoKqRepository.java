package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh;


import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhBaoCaoKqRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhBaoCaoKqHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhXkVtBhBaoCaoKqRepository extends JpaRepository<XhXkVtBhBaoCaoKqHdr, Long> {

  @Query("SELECT c FROM XhXkVtBhBaoCaoKqHdr c WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.soBaoCao} IS NULL OR LOWER(c.soBaoCao) LIKE CONCAT('%',LOWER(:#{#param.soBaoCao}),'%')) " +
      "AND (:#{#param.soCanCu} IS NULL OR LOWER(c.soCanCu) LIKE CONCAT('%',LOWER(:#{#param.soCanCu}),'%')) " +
      "AND ((:#{#param.ngayBaoCaoTu}  IS NULL OR c.ngayBaoCao >= :#{#param.ngayBaoCaoTu})" +
      "AND (:#{#param.ngayBaoCaoDen}  IS NULL OR c.ngayBaoCao <= :#{#param.ngayBaoCaoDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhXkVtBhBaoCaoKqHdr> search(@Param("param") XhXkVtBhBaoCaoKqRequest param, Pageable pageable);

  Optional<XhXkVtBhBaoCaoKqHdr> findBySoBaoCao(String soBaoCao);

  List<XhXkVtBhBaoCaoKqHdr> findByIdIn(List<Long> ids);

  List<XhXkVtBhBaoCaoKqHdr> findAllByIdIn(List<Long> idList);

}
