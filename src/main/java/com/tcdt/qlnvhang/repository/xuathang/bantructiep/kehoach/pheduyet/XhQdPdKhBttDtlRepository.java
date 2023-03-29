package com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin.SearchXhTcTtinBttReq;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdPdKhBttDtlRepository extends JpaRepository<XhQdPdKhBttDtl , Long> {

    @Query("SELECT DISTINCT dtl FROM XhQdPdKhBttDtl dtl " +
            " left join XhQdPdKhBttHdr hdr on hdr.id = dtl.idQdHdr " +
            " left join XhQdPdKhBttDvi dvi on dtl.id = dvi.idQdDtl WHERE 1=1 " +
            "AND (:#{#param.namKh} IS NULL OR hdr.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.maDvi} IS NULL OR dtl.maDvi = :#{#param.maDvi}) " +
            "AND (:#{#param.ngayCgiaTu} IS NULL OR dtl.ngayNhanCgia >= :#{#param.ngayCgiaTu}) " +
            "AND (:#{#param.ngayCgiadDen} IS NULL OR dtl.ngayNhanCgia <= :#{#param.ngayCgiadDen}) " +
            "AND (:#{#param.maDviChiCuc} IS NULL OR dvi.maDvi = :#{#param.maDviChiCuc}) " +
            "AND (:#{#param.trangThai} IS NULL OR dtl.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.lastest} IS NULL OR LOWER(hdr.lastest) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.lastest}),'%'))) " +
            "AND (:#{#param.loaiVthh } IS NULL OR LOWER(hdr.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) "
    )
    Page<XhQdPdKhBttDtl> search(@Param("param") SearchXhTcTtinBttReq param, Pageable pageable);


    List<XhQdPdKhBttDtl> findAllByIdQdHdr (Long idQdHdr);

    void deleteAllByIdQdHdr(Long idQdHdr);
}
