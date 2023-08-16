package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtQuyetDinhPdHdr;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtTongHopHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtVtQuyetDinhPdHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhCtVtQdPdHdrRepository extends JpaRepository<XhCtVtQuyetDinhPdHdr, Long> {
  @Query("SELECT DISTINCT  c FROM XhCtVtQuyetDinhPdHdr c  join c.quyetDinhPdDtl e join e.quyetDinhPdDx dx" +
      " WHERE 1=1 " +
      "AND ((:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) OR (:#{#param.maDviGiao} IS NULL OR dx.maDviCuc = :#{#param.maDviGiao})) " +
      "AND (:#{#param.maDviDx} IS NULL OR e.maDviDx LIKE CONCAT(:#{#param.maDviDx},'%')) " +
      "AND (:#{#param.type} IS NULL OR c.type = :#{#param.type}) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.soDx} IS NULL OR LOWER(e.soDx) LIKE CONCAT('%',LOWER(:#{#param.soDx}),'%')) " +
      "AND ((:#{#param.ngayDxTu}  IS NULL OR e.ngayDx >= :#{#param.ngayDxTu})" +
      "AND (:#{#param.ngayDxDen}  IS NULL OR e.ngayDx <= :#{#param.ngayDxDen}) ) " +
      "AND ((:#{#param.ngayKetThucDxTu}  IS NULL OR e.ngayKetThucDx >= :#{#param.ngayKetThucDxTu})" +
      "AND (:#{#param.ngayKetThucDxDen}  IS NULL OR e.ngayKetThucDx <= :#{#param.ngayKetThucDxDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "AND (:#{#param.xuatCap} IS NULL OR c.xuatCap = :#{#param.xuatCap}) " +
      "AND (:#{#param.soQd} IS NULL OR LOWER(c.soQd) LIKE CONCAT('%',LOWER(:#{#param.soQd}),'%')) " +
      "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
          "AND ((:#{#param.ngayKyTu}  IS NULL OR c.ngayKy >= :#{#param.ngayKyTu})" +
          "AND (:#{#param.ngayKyDen}  IS NULL OR c.ngayKy <= :#{#param.ngayKyDen}) ) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhCtVtQuyetDinhPdHdr> search(@Param("param") SearchXhCtvtQuyetDinhPdHdr param, Pageable pageable);


  void deleteAllByIdIn(List<Long> listId);

  List<XhCtVtQuyetDinhPdHdr> findByIdIn(List<Long> ids);

  List<XhCtVtQuyetDinhPdHdr> findAllByIdIn(List<Long> listId);

  Optional<XhCtVtQuyetDinhPdHdr> findBySoQd(String soQd);

  @Query("SELECT DISTINCT  c FROM XhCtVtQuyetDinhPdHdr c " +
          " WHERE 1=1 " +
//          "AND ((:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) OR (:#{#param.maDviGiao} IS NULL OR dx.maDviCuc = :#{#param.maDviGiao})) " +
//          "AND (:#{#param.maDviDx} IS NULL OR e.maDviDx LIKE CONCAT(:#{#param.maDviDx},'%')) " +
          "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
          "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
          "AND (:#{#param.xuatCap} IS NULL OR c.xuatCap = :#{#param.xuatCap}) " +
          "AND c.id NOT IN (SELECT d.qdPaXuatCapId FROM XhCtvtQdXuatCapHdr d WHERE d.qdPaXuatCapId IS NOT NULL) " +
          "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  List<XhCtVtQuyetDinhPdHdr> searchQdPaXuatCap(@Param("param") SearchXhCtvtQuyetDinhPdHdr param);
}
