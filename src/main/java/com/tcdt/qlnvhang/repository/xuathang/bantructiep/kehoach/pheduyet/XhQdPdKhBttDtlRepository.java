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
            " left join XhQdPdKhBttDvi dvi on dtl.id = dvi.idQdDtl " +
            " left join XhTcTtinBtt cg on dtl.id = cg.idQdPdDtl WHERE 1=1 " +
            "AND (:#{#param.namKh} IS NULL OR hdr.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.maDvi} IS NULL OR dtl.maDvi = :#{#param.maDvi}) " +
            "AND (:#{#param.soDxuat} IS NULL OR LOWER(dtl.soDxuat) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soDxuat}),'%' ) ) )" +
            "AND (:#{#param.ngayCgiaTu} IS NULL OR dtl.ngayNhanCgia >= :#{#param.ngayCgiaTu}) " +
            "AND (:#{#param.ngayCgiaDen} IS NULL OR dtl.ngayNhanCgia <= :#{#param.ngayCgiaDen}) " +
            "AND (:#{#param.ngayTaoTu} IS NULL OR hdr.ngayTao >= :#{#param.ngayTaoTu}) " +
            "AND (:#{#param.ngayTaoDen} IS NULL OR hdr.ngayTao <= :#{#param.ngayTaoDen}) " +
            "AND (:#{#param.ngayDuyetTu} IS NULL OR hdr.ngayPduyet >= :#{#param.ngayDuyetTu}) " +
            "AND (:#{#param.ngayDuyetDen} IS NULL OR hdr.ngayPduyet <= :#{#param.ngayDuyetDen}) " +
            "AND (:#{#param.maDviChiCuc} IS NULL OR dvi.maDvi = :#{#param.maDviChiCuc}) " +
            "AND (:#{#param.tochucCanhan} IS NULL OR LOWER(cg.tochucCanhan) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.tochucCanhan}),'%'))) " +
            "AND (:#{#param.pthucBanTrucTiep == null || #param.pthucBanTrucTiep.isEmpty() } = TRUE OR dtl.pthucBanTrucTiep IN :#{#param.pthucBanTrucTiep}) "+
            "AND (:#{#param.trangThai} IS NULL OR dtl.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.typeSoQdKq} IS NULL OR LOWER(dtl.typeSoQdKq) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.typeSoQdKq}),'%'))) " +
            "AND (:#{#param.lastest} IS NULL OR LOWER(hdr.lastest) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.lastest}),'%'))) " +
            "AND (:#{#param.loaiVthh } IS NULL OR LOWER(hdr.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) "
    )
    Page<XhQdPdKhBttDtl> search(@Param("param") SearchXhTcTtinBttReq param, Pageable pageable);


    List<XhQdPdKhBttDtl> findAllByIdQdHdr (Long idQdHdr);

    void deleteAllByIdQdHdr(Long idQdHdr);
}
