package com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.thongtin.ThongTinDauGiaReq;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface XhTcTtinBdgHdrRepository extends JpaRepository<XhTcTtinBdgHdr, Long> {

  @Query("SELECT c FROM XhTcTtinBdgHdr c " +
          " left join XhQdPdKhBdgDtl dtl on c.idQdPdDtl = dtl.id " +
          " left join XhQdPdKhBdg hdr on hdr.id = dtl.idQdHdr WHERE 1=1 " +
      "AND (:#{#param.maDvi} IS NULL OR dtl.maDvi = :#{#param.maDvi}) " +
      "AND (:#{#param.nam} IS NULL OR hdr.nam = :#{#param.nam}) " +
      "AND (:#{#param.loaiVthh } IS NULL OR LOWER(hdr.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) "
  )
  Page<XhTcTtinBdgHdr> search(@Param("param") ThongTinDauGiaReq param, Pageable pageable);

  void deleteAllByIdIn(List<Long> listId);

  List<XhTcTtinBdgHdr> findByIdIn(List<Long> ids);

  List<XhTcTtinBdgHdr> findByIdQdPdDtlOrderByLanDauGia(Long idQdPdDtl);
}