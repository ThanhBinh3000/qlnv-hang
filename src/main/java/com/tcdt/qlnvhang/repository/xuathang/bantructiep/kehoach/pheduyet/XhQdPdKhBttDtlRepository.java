package com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin.SearchXhTcTtinBttReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdPdKhBttDtlRepository extends JpaRepository<XhQdPdKhBttDtl , Long> {

    @Query("SELECT DISTINCT DTL FROM XhQdPdKhBttDtl DTL" +
            " LEFT JOIN XhQdPdKhBttHdr HDR on HDR.id = DTL.idHdr" +
            " LEFT JOIN XhQdPdKhBttDvi DVI on DTL.id = DVI.idDtl" +
            " LEFT JOIN XhTcTtinBtt TT on DTL.id = TT.idQdPdDtl WHERE 1=1" +
            "AND (:#{#param.dvql} IS NULL OR DTL.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.namKh} IS NULL OR HDR.namKh = :#{#param.namKh})" +
            "AND (:#{#param.ngayCgiaTu} IS NULL OR DTL.ngayNhanCgia >= :#{#param.ngayCgiaTu})" +
            "AND (:#{#param.ngayCgiaDen} IS NULL OR DTL.ngayNhanCgia <= :#{#param.ngayCgiaDen})" +
            "AND (:#{#param.tochucCanhan} IS NULL OR LOWER(TT.tochucCanhan) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.tochucCanhan}),'%')))" +
            "AND (:#{#param.maChiCuc} IS NULL OR DVI.maDvi LIKE CONCAT(:#{#param.maChiCuc},'%')) " +
            "AND (:#{#param.trangThai} IS NULL OR DTL.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.lastest} IS NULL OR LOWER(HDR.lastest) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.lastest}),'%')))" +
            "AND (:#{#param.loaiVthh } IS NULL OR LOWER(HDR.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%'))" +
            "AND (:#{#param.pthucBanTrucTiep == null || #param.pthucBanTrucTiep.isEmpty() } = TRUE OR DTL.pthucBanTrucTiep IN :#{#param.pthucBanTrucTiep})"
    )
    Page<XhQdPdKhBttDtl> searchPage(@Param("param") SearchXhTcTtinBttReq param, Pageable pageable);

    void deleteAllByIdHdr(Long idHdr);

    List<XhQdPdKhBttDtl> findAllByIdHdr(Long idHdr);

    List<XhQdPdKhBttDtl> findByIdHdrIn(List<Long> listId);

    List<XhQdPdKhBttDtl> findByIdIn(List<Long> idDtlList);
}
