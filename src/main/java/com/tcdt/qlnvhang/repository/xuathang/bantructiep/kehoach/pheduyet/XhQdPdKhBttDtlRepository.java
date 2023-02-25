package com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdPdKhBttDtlRepository extends JpaRepository<XhQdPdKhBttDtl , Long> {

//    @Query("SELECT dtl FROM XhQdPdKhBttDtl dtl " +
//            " left join XhTcTtinBtt btt on btt.idDtl = dtl.id " +
//            " left join XhQdPdKhBttHdr hdr on hdr.id = dtl.idQdHdr WHERE 1=1 " +
//            "AND (:#{#param.namKh} IS NULL OR hdr.namKh = :#{#param.namKh}) " +
//            "AND (:#{#param.ngayCgiaTu} IS NULL OR btt.ngayChaoGia >= :#{#param.ngayCgiaTu}) " +
//            "AND (:#{#param.ngayCgiadDen} IS NULL OR btt.ngayChaoGia <= :#{#param.ngayCgiadDen}) " +
//            "AND (:#{#param.maDvi} IS NULL OR dtl.maDvi = :#{#param.maDvi}) " +
//            "AND (:#{#param.trangThai} IS NULL OR dtl.trangThai = :#{#param.trangThai}) " +
//            "AND (:#{#param.tochucCanhan} IS NULL OR LOWER(btt.tochucCanhan) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.tochucCanhan}),'%'))) " +
//            "AND (:#{#param.lastest} IS NULL OR LOWER(hdr.lastest) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.lastest}),'%'))) " +
//            "AND (:#{#param.loaiVthh } IS NULL OR LOWER(hdr.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) "
//    )
//    Page<XhQdPdKhBttDtl> search(@Param("param") SearchXhTcTtinBttReq param, Pageable pageable);


    List<XhQdPdKhBttDtl> findAllByIdQdHdr (Long idQdHdr);

    void deleteAllByIdQdHdr(Long idQdHdr);
}
