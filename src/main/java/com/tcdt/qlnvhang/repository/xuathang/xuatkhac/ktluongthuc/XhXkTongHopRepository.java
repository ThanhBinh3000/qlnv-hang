package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktluongthuc;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.XhXkTongHopRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface XhXkTongHopRepository extends JpaRepository<XhXkTongHopHdr, Long> {
  @Query("SELECT distinct c FROM XhXkTongHopHdr c left join c.tongHopDtl h WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
      "AND (:#{#param.maDanhSach} IS NULL OR LOWER(c.maDanhSach) LIKE CONCAT('%',LOWER(:#{#param.maDanhSach}),'%')) " +
      "AND (:#{#param.maCuc} IS NULL OR substring(h.maDiaDiem,0,6) = :#{#param.maCuc}) " +
      "AND (:#{#param.maChiCuc} IS NULL OR substring(h.maDiaDiem,0,8) = :#{#param.maChiCuc}) " +
      "AND ((:#{#param.ngayTaoTu}  IS NULL OR c.ngayTao >= :#{#param.ngayTaoTu})" +
      "AND  (:#{#param.ngayTaoDen}  IS NULL OR c.ngayTao <= :#{#param.ngayTaoDen})) " +
      "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
      "AND (:#{#param.loai} IS NULL OR c.loai = :#{#param.loai}) " +
      "AND (:#{#param.capTh} IS NULL OR c.capTh = :#{#param.capTh}) " +
      "AND (:#{#param.listId == null} = true OR c.id in :#{#param.listId}) " +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhXkTongHopHdr> searchPage(@Param("param") XhXkTongHopRequest param, Pageable pageable);
  @Query(value = "SELECT" +
      "    CASE" +
      "        WHEN LENGTH(:ma) = 14 THEN kk.nam_nhap" +
      "        WHEN LENGTH(:ma) = 16 THEN kl.nam_nhap" +
      "        ELSE NULL " +
      "    END AS nam_nhap_result " +
      " FROM" +
      "    xh_xk_tong_hop_dtl yt" +
      " LEFT JOIN" +
      "    kt_ngan_kho kk ON yt.ma_dia_diem = kk.ma_ngankho" +
      " LEFT JOIN" +
      "    kt_ngan_lo kl ON yt.ma_dia_diem = kl.ma_nganlo where yt.id = :id " ,nativeQuery = true)
  Integer getNamNhap(String ma, Long id);

  void deleteAllByIdIn(List<Long> listId);

  List<XhXkTongHopHdr> findByIdIn(List<Long> ids);

  Optional<XhXkTongHopHdr> findByMaDanhSach(String maDs);
}
