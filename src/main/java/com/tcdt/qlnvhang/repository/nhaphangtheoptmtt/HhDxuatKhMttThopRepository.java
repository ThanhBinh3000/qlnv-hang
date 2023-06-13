package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.request.nhaphangtheoptt.SearchHhDxKhMttThopReq;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxKhMttThopHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface HhDxuatKhMttThopRepository extends JpaRepository<HhDxKhMttThopHdr ,Long> {

    @Query("SELECT DISTINCT TH FROM HhDxKhMttThopHdr TH " +
            " left join HhQdPheduyetKhMttHdr QD on TH.id = QD.idThHdr WHERE 1=1 " +
            "AND (:#{#param.namKh} IS NULL OR TH.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR TH.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.cloaiVthh} IS NULL OR TH.cloaiVthh LIKE CONCAT(:#{#param.cloaiVthh},'%')) " +
            "AND (:#{#param.noiDungThop} IS NULL OR LOWER(TH.noiDungThop) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.noiDungThop}),'%'))) " +
            "AND (:#{#param.ngayThopTu} IS NULL OR TH.ngayThop >= :#{#param.ngayThopTu}) " +
            "AND (:#{#param.ngayThopDen} IS NULL OR TH.ngayThop <= :#{#param.ngayThopDen}) " +
            "AND (:#{#param.ngayQdTu} IS NULL OR QD.ngayQd >= :#{#param.ngayQdTu}) " +
            "AND (:#{#param.ngayQdDen} IS NULL OR QD.ngayQd <= :#{#param.ngayQdDen}) " +
            "AND (:#{#param.trangThai} IS NULL OR TH.trangThai = :#{#param.trangThai})")
    Page<HhDxKhMttThopHdr> searchPage(@Param("param") SearchHhDxKhMttThopReq param, Pageable pageable);



    List<HhDxKhMttThopHdr> findAllByIdIn(List<Long> ids);

    @Transactional
    @Modifying
    void deleteAllByIdIn(List<Long> ids);

    @Transactional()
    @Modifying
    @Query(value = "UPDATE HH_DX_KHMTT_THOP_HDR SET TRANG_THAI =:trangThai WHERE ID = :idThHdr", nativeQuery = true)
    void updateTrangThai(Long idThHdr, String trangThai);

    @Transactional()
    @Modifying
    @Query(value = "UPDATE HH_DX_KHMTT_THOP_HDR SET TRANG_THAI =:trangThai, SO_QD_PDUYET = null WHERE ID = :idThHdr", nativeQuery = true)
    void updateTrangThaiAndSoQd(Long idThHdr, String trangThai);

}
