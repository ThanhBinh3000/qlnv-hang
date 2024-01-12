package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhQdGiaonvXnRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhQdGiaonvXnHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhXkVtBhQdGiaonvXnRepository extends JpaRepository<XhXkVtBhQdGiaonvXnHdr, Long> {
  @Query("SELECT distinct c FROM XhXkVtBhQdGiaonvXnHdr c WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.loai} IS NULL OR c.loai = :#{#param.loai}) " +
      "AND (:#{#param.loaiXn} IS NULL OR c.loaiXn = :#{#param.loaiXn}) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "AND (:#{#param.listTrangThaiXh.size() }  = 0 OR c.trangThaiXh in :#{#param.listTrangThaiXh}) " +
      "AND ((:#{#param.ngayKyQdTu}  IS NULL OR c.ngayKy >= :#{#param.ngayKyQdTu})" +
      "AND  (:#{#param.ngayKyQdDen}  IS NULL OR c.ngayKy <= :#{#param.ngayKyQdDen})) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhXkVtBhQdGiaonvXnHdr> searchPage(@Param("param") XhXkVtBhQdGiaonvXnRequest param, Pageable pageable);

  @Query("SELECT DISTINCT qd FROM XhXkVtBhQdGiaonvXnHdr qd" +
      " LEFT JOIN XhXkVtBhPhieuKdclHdr pkd ON qd.id IN (pkd.idQdGiaoNvXh) WHERE 1=1 " +
      " AND (pkd.idBaoCaoKdm IS NULL) " +
      " AND (:#{#param.dvql} IS NULL OR qd.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      " AND (:#{#param.nam} IS NULL OR qd.nam = :#{#param.nam}) " +
      " AND (:#{#param.loai} IS NULL OR qd.loai = :#{#param.loai}) " +
      " AND (:#{#param.loaiXn} IS NULL OR qd.loaiXn = :#{#param.loaiXn}) " +
      " AND (:#{#param.trangThai} IS NULL OR qd.trangThai = :#{#param.trangThai}) " +
      " ORDER BY qd.ngaySua DESC, qd.ngayTao DESC, qd.id DESC"
  )
  List<XhXkVtBhQdGiaonvXnHdr> listQuyetDinhChuaTaoBaoCao(@Param("param") XhXkVtBhQdGiaonvXnRequest param);



  void deleteAllByIdIn(List<Long> listId);

  List<XhXkVtBhQdGiaonvXnHdr> findByIdIn(List<Long> ids);
  List<XhXkVtBhQdGiaonvXnHdr> findByIdBbKtNhapKho(Long id);

  List<XhXkVtBhQdGiaonvXnHdr> findByIdCanCuIn(List<Long> ids);

  Optional<XhXkVtBhQdGiaonvXnHdr> findBySoQuyetDinh(String soQd);

}
