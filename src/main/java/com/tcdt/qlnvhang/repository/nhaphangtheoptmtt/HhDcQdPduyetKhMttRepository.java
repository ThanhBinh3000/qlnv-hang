package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;


import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDcQdPduyetKhmttHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HhDcQdPduyetKhMttRepository extends JpaRepository<HhDcQdPduyetKhmttHdr ,Long> {
    @Query(value = "select * from HH_DC_QD_PDUYET_KHMTT_HDR DC where (:namKh IS NULL OR DC.NAM_KH =TO_NUMBER(:namKh))" +
            " AND (:soQdDc IS NULL OR LOWER(DC.SO_QD_DC) LIKE LOWER(CONCAT(CONCAT('%', :soQdDc),'%'))) "+
            " AND (:trichYeu IS NULL OR LOWER(DC.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%'))) "+
            " AND (:ngayKyDcTu IS NULL OR DC.NGAY_KY_DC >=  TO_DATE(:ngayKyDcTu,'yyyy-MM-dd')) " +
            " AND (:ngayKyDcDen IS NULL OR DC.NGAY_KY_DC <= TO_DATE(:ngayKyDcDen,'yyyy-MM-dd'))" +
            " AND (:trangThai IS NULL OR DC.TRANG_THAI = :trangThai) " +
            " AND (:maDvi IS NULL OR LOWER(DC.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    Page<HhDcQdPduyetKhmttHdr> searchPage(Integer namKh, String soQdDc, String trichYeu, String ngayKyDcTu, String ngayKyDcDen, String trangThai, String maDvi, Pageable pageable);

    Optional<HhDcQdPduyetKhmttHdr> findBySoQdDc(String soQdDc);

    List<HhDcQdPduyetKhmttHdr> findAllByIdIn(List<Long> listId);
}
