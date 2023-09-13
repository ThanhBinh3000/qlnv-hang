package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.request.nhaphangtheoptt.SearchHhPthucTkhaiReq;
import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttDx;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HhQdPheduyetKhMttDxRepository extends JpaRepository<HhQdPheduyetKhMttDx, Long> {

    @Query("SELECT DISTINCT dtl FROM HhQdPheduyetKhMttDx dtl " +
            " left join HhQdPheduyetKhMttHdr hdr on hdr.id = dtl.idQdHdr " +
            " left join HhChiTietTTinChaoGia cg on dtl.id = cg.idQdPdSldd " +
            " left join HhQdPheduyetKhMttSLDD qdsldd on dtl.id = qdsldd.idQdDtl " +
            " left join HhDcQdPduyetKhmttSldd dcsldd on dtl.idDxHdr = dcsldd.idDxKhmtt " +
            "WHERE 1=1 " +
            "AND (:#{#param.namKh} IS NULL OR hdr.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.ngayCgiaTu} IS NULL OR dtl.ngayNhanCgia >= :#{#param.ngayCgiaTu}) " +
            "AND (:#{#param.ngayCgiadDen} IS NULL OR dtl.ngayNhanCgia <= :#{#param.ngayCgiadDen}) " +
            "AND (:#{#param.canhanTochuc} IS NULL OR LOWER(cg.canhanTochuc) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.canhanTochuc}),'%'))) " +
            "AND (:#{#param.maDvi} IS NULL OR dtl.maDvi = :#{#param.maDvi}) " +
            "AND (:#{#param.maCuc} IS NULL OR LOWER(qdsldd.maDvi) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.maCuc}),'%'))) " +
            "AND (:#{#param.maCuc} IS NULL OR LOWER(dcsldd.maDvi) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.maCuc}),'%'))) " +
            "AND (:#{#param.trangThai} IS NULL OR dtl.trangThai = :#{#param.trangThai}) " +
            "AND (:#{#param.pthucMuaTrucTiep} IS NULL OR " +
            "    (:#{#param.pthucMuaTrucTiep} = '01' AND dtl.pthucMuaTrucTiep = '01') OR " +
            "    (:#{#param.pthucMuaTrucTiep} != '01' AND dtl.pthucMuaTrucTiep != '01')) " +
            "AND (:#{#param.lastest} IS NULL OR LOWER(hdr.lastest) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.lastest}),'%'))) " +
            "AND (:#{#param.loaiVthh } IS NULL OR LOWER(hdr.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh},'%')) "
    )
    Page<HhQdPheduyetKhMttDx> search(@Param("param") SearchHhPthucTkhaiReq param, Pageable pageable);


    List<HhQdPheduyetKhMttDx> findAllByIdQdHdr (Long idQdHdr);

    void deleteAllByIdQdHdr(Long idQdHdr);

}
