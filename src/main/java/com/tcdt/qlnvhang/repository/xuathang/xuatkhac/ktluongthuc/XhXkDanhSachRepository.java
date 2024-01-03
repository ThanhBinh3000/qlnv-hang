package com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktluongthuc;

import com.tcdt.qlnvhang.request.xuathang.xuatkhac.XhXkDanhSachRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkDanhSachHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface XhXkDanhSachRepository extends JpaRepository<XhXkDanhSachHdr, Long> {
  @Query("SELECT c FROM XhXkDanhSachHdr c WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
      "AND ((:#{#param.ngayDeXuatTu}  IS NULL OR c.ngayDeXuat >= :#{#param.ngayDeXuatTu})" +
      "AND (:#{#param.ngayDeXuatDen}  IS NULL OR c.ngayDeXuat <= :#{#param.ngayDeXuatDen}) ) " +
      "AND (:#{#param.loai} IS NULL OR c.loai = :#{#param.loai}) " +
      "AND (:#{#param.type} IS NULL OR ('TH' = :#{#param.type} AND c.maTongHop IS NULL AND c.idTongHop IS NULL))" +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<XhXkDanhSachHdr> searchPage(@Param("param") XhXkDanhSachRequest param, Pageable pageable);

  @Query(value = "SELECT" +
      "    CASE" +
      "        WHEN LENGTH(:ma) = 14 THEN kk.nam_nhap" +
      "        WHEN LENGTH(:ma) = 16 THEN kl.nam_nhap" +
      "        ELSE NULL " +
      "    END AS nam_nhap_result " +
      " FROM" +
      "    xh_xk_danh_sach_hdr yt" +
      " LEFT JOIN" +
      "    kt_ngan_kho kk ON yt.ma_dia_diem = kk.ma_ngankho" +
      " LEFT JOIN" +
      "    kt_ngan_lo kl ON yt.ma_dia_diem = kl.ma_nganlo where yt.id = :id " ,nativeQuery = true)
  Integer getNamNhap(String ma, Long id);


  void deleteAllByIdIn(List<Long> listId);

  List<XhXkDanhSachHdr> findByIdIn(List<Long> ids);

  List<XhXkDanhSachHdr> findAllByIdTongHop(Long ids);

  List<XhXkDanhSachHdr> findAllByIdTongHopTc(Long ids);

  List<XhXkDanhSachHdr> findAllByIdTongHopIn(List<Long> ids);
}
