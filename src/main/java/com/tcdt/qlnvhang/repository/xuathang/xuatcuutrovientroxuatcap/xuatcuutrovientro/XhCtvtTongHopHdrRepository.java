package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtTongHopHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtTongHopHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhCtvtTongHopHdrRepository extends JpaRepository<XhCtvtTongHopHdr, Long> {
  @Query("SELECT DISTINCT  c FROM XhCtvtTongHopHdr c left join c.deXuatCuuTro e" +
      " WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.maDviDx} IS NULL OR e.maDvi LIKE CONCAT(:#{#param.maDviDx},'%')) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.soDx} IS NULL OR LOWER(e.soDx) LIKE CONCAT('%',LOWER(:#{#param.soDx}),'%')) " +
      "AND ((:#{#param.ngayTaoTu}  IS NULL OR c.ngayTao >= :#{#param.ngayTaoTu})" +
      "AND (:#{#param.ngayTaoDen}  IS NULL OR c.ngayTao <= :#{#param.ngayTaoDen}) ) " +
    /*  "AND ((:#{#param.ngayKetThucDxTu}  IS NULL OR e.ngayKetThucDx >= :#{#param.ngayKetThucDxTu})" +
      "AND (:#{#param.ngayKetThucDxDen}  IS NULL OR e.ngayKetThucDx <= :#{#param.ngayKetThucDxDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "AND (:#{#param.idQdPdList.isEmpty() } = true OR c.idQdPd IN :#{#param.idQdPdList}) " +
      "AND (:#{#param.idQdPdNull } = false OR (:#{#param.idQdPdNull } = true AND c.idQdPd IS NULL)) " +*/
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhCtvtTongHopHdr> search(@Param("param") SearchXhCtvtTongHopHdr param, Pageable pageable);

  void deleteAllByIdIn(List<Long> listId);

  List<XhCtvtTongHopHdr> findByIdIn(List<Long> ids);

  List<XhCtvtTongHopHdr> findAllByIdIn(List<Long> listId);


}
