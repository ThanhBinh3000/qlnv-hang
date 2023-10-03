package com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBangKeHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgPhieuXuatKho;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.SearchXhDgBangKeReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.XhDgBangKeReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.XhDgPhieuXuatKhoReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhDgBangKeHdrRepository extends JpaRepository<XhDgBangKeHdr, Long> {



  @Query("SELECT DISTINCT XK FROM XhDgBangKeHdr XK " +
          "WHERE (:#{#param.dvql} IS NULL OR XK.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
          "AND (:#{#param.nam} IS NULL OR XK.nam = :#{#param.nam}) " +
          "AND (:#{#param.soBangKeHang} IS NULL OR XK.soBangKeHang = :#{#param.soBangKeHang}) " +
          "AND (:#{#param.soPhieuXuatKho} IS NULL OR XK.soPhieuXuatKho = :#{#param.soPhieuXuatKho}) " +
          "AND (:#{#param.soQdNv} IS NULL OR XK.soQdNv = :#{#param.soQdNv}) " +
          "AND (:#{#param.loaiVthh} IS NULL OR XK.loaiVthh LIKE CONCAT(:#{#param.loaiVthh}, '%')) " +
          "AND (:#{#param.ngayLapBangKeTu} IS NULL OR XK.ngayLapBangKe >= :#{#param.ngayLapBangKeTu}) " +
          "AND (:#{#param.ngayLapBangKeDen} IS NULL OR XK.ngayLapBangKe <= :#{#param.ngayLapBangKeDen}) " +
          "AND (:#{#param.trangThai} IS NULL OR XK.trangThai = :#{#param.trangThai}) " +
          "ORDER BY XK.ngaySua DESC, XK.ngayTao DESC, XK.id DESC")
  Page<XhDgBangKeHdr> searchPage(@Param("param") XhDgBangKeReq param, Pageable pageable);

  boolean existsBySoBangKeHang(String soBangKeHang);

  boolean existsBySoBangKeHangAndIdNot(String soBangKeHang, Long id);

  List<XhDgBangKeHdr> findByIdIn(List<Long> idList);

  List<XhDgBangKeHdr> findAllByIdIn(List<Long> listId);
}
