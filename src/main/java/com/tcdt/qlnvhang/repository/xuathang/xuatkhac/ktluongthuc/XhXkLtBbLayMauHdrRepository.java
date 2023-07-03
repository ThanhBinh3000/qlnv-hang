package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktluongthuc;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktluongthuc.XhXkLtBbLayMauRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktluongthuc.XhXkLtBbLayMauHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhXkLtBbLayMauHdrRepository extends JpaRepository<XhXkLtBbLayMauHdr,Long> {

  @Query("SELECT c FROM XhXkLtBbLayMauHdr c WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.type} IS NULL OR c.type = :#{#param.type}) " +
      "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
      "AND (:#{#param.soBienBan} IS NULL OR LOWER(c.soBienBan) LIKE CONCAT('%',LOWER(:#{#param.soBienBan}),'%')) " +
      "AND (:#{#param.maDanhSach} IS NULL OR LOWER(c.maDanhSach) LIKE CONCAT('%',LOWER(:#{#param.maDanhSach}),'%')) " +
      "AND (:#{#param.dviKiemNghiem} IS NULL OR LOWER(c.dviKiemNghiem) LIKE CONCAT('%',LOWER(:#{#param.dviKiemNghiem}),'%')) " +
      "AND ((:#{#param.ngayLayMauTu}  IS NULL OR c.ngayLayMau >= :#{#param.ngayLayMauTu})" +
      "AND (:#{#param.ngayLayMauDen}  IS NULL OR c.ngayLayMau <= :#{#param.ngayLayMauDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhXkLtBbLayMauHdr> search(@Param("param") XhXkLtBbLayMauRequest param, Pageable pageable);

  Optional<XhXkLtBbLayMauHdr> findBySoBienBan(String soBienBan);

  List<XhXkLtBbLayMauHdr> findByIdIn(List<Long> ids);

  List<XhXkLtBbLayMauHdr> findAllByIdIn(List<Long> idList);

}
