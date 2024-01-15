package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy.SearchXhThQuyetDinh;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThBaoCaoKqHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhThBaoCaoKqHdrRepository extends JpaRepository<XhThBaoCaoKqHdr,Long> {

  @Query("SELECT DISTINCT  c FROM XhThBaoCaoKqHdr c " +
      " LEFT JOIN XhThBaoCaoKqDtl dtl on c.id = dtl.idHdr " +
      " LEFT JOIN XhThDanhSachHdr ds on dtl.idDsHdr = ds.id " +
      " WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR ds.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.soQd} IS NULL OR LOWER(c.soQd) LIKE CONCAT('%',LOWER(:#{#param.soQd}),'%')) " +
      "AND (:#{#param.soBaoCao} IS NULL OR LOWER(c.soBaoCao) LIKE CONCAT('%',LOWER(:#{#param.soBaoCao}),'%')) " +
      "AND ((:#{#param.ngayBaoCaoTu}  IS NULL OR c.ngayBaoCao >= :#{#param.ngayBaoCaoTu})" +
      "AND (:#{#param.ngayBaoCaoDen}  IS NULL OR c.ngayBaoCao <= :#{#param.ngayBaoCaoDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhThBaoCaoKqHdr> search (@Param("param") SearchXhThQuyetDinh param, Pageable pageable);


  void deleteAllByIdIn(List<Long> listId);

  List<XhThBaoCaoKqHdr> findByIdIn(List<Long> ids);

  List<XhThBaoCaoKqHdr> findAllByIdIn(List<Long> listId);

  Optional<XhThBaoCaoKqHdr> findBySoBaoCao(String soQd);
}
