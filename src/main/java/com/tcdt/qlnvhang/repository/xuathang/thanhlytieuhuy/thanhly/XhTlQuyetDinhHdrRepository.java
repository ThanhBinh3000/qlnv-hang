package com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.SearchXhTlQuyetDinh;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlHoSoReq;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlHoSoHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhTlQuyetDinhHdrRepository extends JpaRepository<XhTlQuyetDinhHdr,Long> {

  @Query("SELECT DISTINCT  c FROM XhTlQuyetDinhHdr c " +
      " LEFT JOIN XhTlHoSoHdr hdr on c.idHoSo = hdr.id " +
      " WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR hdr.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.soQd} IS NULL OR LOWER(c.soQd) LIKE CONCAT('%',LOWER(:#{#param.soQd}),'%')) " +
      "AND (:#{#param.soHoSo} IS NULL OR LOWER(c.soHoSo) LIKE CONCAT('%',LOWER(:#{#param.soHoSo}),'%')) " +
      "AND ((:#{#param.ngayKyTu}  IS NULL OR c.ngayKy >= :#{#param.ngayKyTu})" +
      "AND (:#{#param.ngayKyDen}  IS NULL OR c.ngayKy <= :#{#param.ngayKyDen}) ) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhTlQuyetDinhHdr> search (@Param("param") SearchXhTlQuyetDinh param, Pageable pageable);

  @Query(value = "SELECT c FROM XhTlQuyetDinhHdr c " +
          " LEFT JOIN XhTlBaoCaoKqHdr qd on c.id = qd.idQd " +
          " WHERE 1 = 1 " +
          " AND qd.id is null " +
          " AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
          " ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc "
  )
  List<XhTlQuyetDinhHdr> listTaoBaoCaoThanhLy(@Param("param") XhTlHoSoReq param);


  void deleteAllByIdIn(List<Long> listId);

  List<XhTlQuyetDinhHdr> findByIdIn(List<Long> ids);

  List<XhTlQuyetDinhHdr> findAllByIdIn(List<Long> listId);

  Optional<XhTlQuyetDinhHdr> findBySoQd(String soQd);

  Optional<XhTlQuyetDinhHdr> findByIdHoSo(Long idHoSo);


}
