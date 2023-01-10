package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XhTcTtinBdgHdrRepository extends JpaRepository<XhTcTtinBdgHdr, Long> {

//  @Query("SELECT c FROM XhTcTtinBdgHdr c WHERE 1=1 " +
//      "AND (:#{#param.maDvi} IS NULL OR c.maDvi = :#{#param.maDvi}) " +
//      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
//      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
//      "AND (:#{#param.soKhDx}  IS NULL OR LOWER(c.soKhDx) LIKE CONCAT('%',LOWER(:#{#param.soKhDx}),'%')) " +
//      "AND (:#{#param.soQdPdKh}  IS NULL OR LOWER(c.soQdPdKh) LIKE CONCAT('%',LOWER(:#{#param.soQdPdKh}),'%')) " +
//      "AND (:#{#param.soQdPdKq}  IS NULL OR LOWER(c.soQdPdKq) LIKE CONCAT('%',LOWER(:#{#param.soQdPdKq}),'%')) " +
//      "AND (:#{#param.loaiVthh}  IS NULL OR LOWER(c.loaiVthh) =:#{#param.loaiVthh}) " +
//      "AND (:#{#param.cloaiVthh}  IS NULL OR LOWER(c.cloaiVthh) =:#{#param.cloaiVthh}) " +
//      "AND (:#{#param.listTrangThai == null} = true OR c.trangThai in :#{#param.listTrangThai}) " +
//      "AND ((:#{#param.ngayQdPdKqBdg}  IS NULL OR c.ngayQdPdKqBdg >= :#{#param.ngayQdPdKqBdg}) AND (:#{#param.ngayQdPdKqBdg}  IS NULL OR c.ngayQdPdKqBdg <= :#{#param.ngayQdPdKqBdg}) ) " +
//      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
//  )
//  Page<XhTcTtinBdgHdr> search(@Param("param") ThongTinDauGiaReq param, Pageable pageable);

  void deleteAllByIdIn(List<Long> listId);

  List<XhTcTtinBdgHdr> findByIdIn(List<Long> ids);

  List<XhTcTtinBdgHdr> findByIdQdPdDtlOrderByLanDauGia(Long idQdPdDtl);
}