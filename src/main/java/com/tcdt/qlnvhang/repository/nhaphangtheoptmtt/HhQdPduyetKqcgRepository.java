package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdPduyetKqcgHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HhQdPduyetKqcgRepository extends JpaRepository<HhQdPduyetKqcgHdr, Long> {


    @Query(value = "select * from HH_QD_PDUYET_KQCG_HDR PD where (:namKh IS NULL OR PD.NAM_KH = TO_NUMBER(:namKh)) " +
            " AND (:ngayCgiaTu IS NULL OR PD.NGAY_KY >=  TO_DATE(:ngayCgiaTu,'yyyy-MM-dd')) " +
            " AND (:ngayCgiaDen IS NULL OR PD.NGAY_KY <= TO_DATE(:ngayCgiaDen,'yyyy-MM-dd'))" +
            " AND (:trangThai IS NULL OR PD.TRANG_THAI = :trangThai) " +
            " AND (:maDvi IS NULL OR LOWER(PD.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    Page<HhQdPduyetKqcgHdr> searchPage(Integer namKh, String ngayCgiaTu, String ngayCgiaDen, String trangThai, String maDvi, Pageable pageable);

    Optional<HhQdPduyetKqcgHdr> findAllBySoQdPdCg(String soQdPdCg);

    List<HhQdPduyetKqcgHdr> findAllByIdIn(List<Long> ids);

    HhQdPduyetKqcgHdr findAllById(Long id);

}
