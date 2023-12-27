package com.tcdt.qlnvhang.repository.xuathang.suachuahang;

import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachRequest;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScDanhSachHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuNhapKhoHdr;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScDanhSachRepository extends JpaRepository<ScDanhSachHdr, Long> {

  @Query("SELECT c FROM ScDanhSachHdr c WHERE 1=1 " +
      "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
      "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh = :#{#param.loaiVthh}) " +
      "AND ((:#{#param.ngayDeXuatTu}  IS NULL OR c.ngayDeXuat >= :#{#param.ngayDeXuatTu})" +
      "AND (:#{#param.ngayDeXuatDen}  IS NULL OR c.ngayDeXuat <= :#{#param.ngayDeXuatDen}) ) " +
      "AND (:#{#param.type} IS NULL OR ('TH' = :#{#param.type} AND c.maTongHop IS NULL))" +
      "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  Page<ScDanhSachHdr> searchPage(@Param("param") XhTlDanhSachRequest param, Pageable pageable);

  @Query(value = "SELECT" +
          "    CASE" +
          "        WHEN LENGTH(:ma) = 14 THEN kk.nam_nhap" +
          "        WHEN LENGTH(:ma) = 16 THEN kl.nam_nhap" +
          "        ELSE NULL " +
          "    END AS nam_nhap_result " +
          " FROM" +
          "    sc_danh_sach_hdr yt" +
          " LEFT JOIN" +
          "    kt_ngan_kho kk ON yt.ma_dia_diem = kk.ma_ngankho" +
          " LEFT JOIN" +
          "    kt_ngan_lo kl ON yt.ma_dia_diem = kl.ma_nganlo where yt.id = :id " ,nativeQuery = true)
  Integer getNamNhap(String ma, Long id);

  @Query("SELECT c FROM ScDanhSachHdr c  " +
          " LEFT JOIN ScTongHopDtl th on c.id = th.idDsHdr WHERE 1=1 " +
          " AND th.id is null " +
          " AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
          "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  List<ScDanhSachHdr> listTongHop(@Param("param") XhTlDanhSachRequest param);

  @Query("SELECT c FROM ScDanhSachHdr c  " +
          " LEFT JOIN ScQuyetDinhNhapHangDtl qdDtl on c.id = qdDtl.idDsHdr " +
          " LEFT JOIN ScBienBanKtHdr bbKt on c.id = bbKt.idScDanhSachHdr " +
          " WHERE 1=1 " +
          " AND bbKt.id is null " +
          " AND (:#{#param.idQdNh} IS NULL OR qdDtl.idHdr = :#{#param.idQdNh}) " +
          "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc"
  )
  List<ScDanhSachHdr> searchListTaoBienBanKt(@Param("param") XhTlDanhSachRequest param);


  void deleteAllByIdIn(List<Long> listId);

  List<ScDanhSachHdr> findByIdIn(List<Long> ids);
}
