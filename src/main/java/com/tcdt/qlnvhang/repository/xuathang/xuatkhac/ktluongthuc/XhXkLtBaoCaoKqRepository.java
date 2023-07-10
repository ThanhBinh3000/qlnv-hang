package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktluongthuc;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktluongthuc.XhXkLtBaoCaoKqRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktluongthuc.XhXkLtBaoCaoKq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhXkLtBaoCaoKqRepository extends JpaRepository<XhXkLtBaoCaoKq,Long> {

  @Query("SELECT c FROM XhXkLtBaoCaoKq c WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.tenBaoCao} IS NULL OR LOWER(c.tenBaoCao) LIKE CONCAT('%',LOWER(:#{#param.tenBaoCao}),'%')) " +
      "AND (:#{#param.soBaoCao} IS NULL OR LOWER(c.soBaoCao) LIKE CONCAT('%',LOWER(:#{#param.soBaoCao}),'%')) " +
      "AND (:#{#param.maDanhSach} IS NULL OR LOWER(c.maDanhSach) LIKE CONCAT('%',LOWER(:#{#param.maDanhSach}),'%')) " +
      "AND ((:#{#param.ngayBaoCaoTu}  IS NULL OR c.ngayBaoCao >= :#{#param.ngayBaoCaoTu})" +
      "AND (:#{#param.ngayBaoCaoDen}  IS NULL OR c.ngayBaoCao <= :#{#param.ngayBaoCaoDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhXkLtBaoCaoKq> search(@Param("param") XhXkLtBaoCaoKqRequest param, Pageable pageable);

  Optional<XhXkLtBaoCaoKq> findBySoBaoCao(String soBaoCao);

  List<XhXkLtBaoCaoKq> findByIdIn(List<Long> ids);

  List<XhXkLtBaoCaoKq> findAllByIdIn(List<Long> idList);

}
