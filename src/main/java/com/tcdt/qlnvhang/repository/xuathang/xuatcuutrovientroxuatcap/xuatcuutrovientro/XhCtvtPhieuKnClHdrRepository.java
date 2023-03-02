package com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtPhieuKnCl;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuKnClHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhCtvtPhieuKnClHdrRepository extends JpaRepository<XhCtvtPhieuKnClHdr,Long> {

  @Query("SELECT c FROM XhCtvtPhieuKnClHdr c WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.type} IS NULL OR c.type = :#{#param.type}) " +
      "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.soPhieu} IS NULL OR LOWER(c.soPhieu) LIKE CONCAT('%',LOWER(:#{#param.soPhieu}),'%')) " +
      "AND (:#{#param.soQdGiaoNvXh} IS NULL OR LOWER(c.soQdGiaoNvXh) LIKE CONCAT('%',LOWER(:#{#param.soQdGiaoNvXh}),'%')) " +
      "AND (:#{#param.soBbXuatDocKho} IS NULL OR LOWER(c.soBbXuatDocKho) LIKE CONCAT('%',LOWER(:#{#param.soBbXuatDocKho}),'%')) " +
      "AND ((:#{#param.ngayKnTu}  IS NULL OR c.ngayLayMau >= :#{#param.ngayKnTu})" +
      "AND (:#{#param.ngayKnDen}  IS NULL OR c.ngayLayMau <= :#{#param.ngayKnDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhCtvtPhieuKnClHdr> search(@Param("param") SearchXhCtvtPhieuKnCl param, Pageable pageable);

  Optional<XhCtvtPhieuKnClHdr> findBySoPhieu(String soBienBan);

  List<XhCtvtPhieuKnClHdr> findByIdIn(List<Long> ids);

  List<XhCtvtPhieuKnClHdr> findAllByIdIn(List<Long> idList);


}
