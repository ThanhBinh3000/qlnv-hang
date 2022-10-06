package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhHdongBkePmuahangHdr;
import com.tcdt.qlnvhang.util.Contains;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HhHdongBkePmuahangRepository extends JpaRepository<HhHdongBkePmuahangHdr,Long> {

    @Query(value = "SELECT * FROM HH_HDONG_BKE_PMUAHANG_HDR HD " +
            " LEFT JOIN HH_THONG_TIN_DVI_DTU_CCAP DVI ON HD.ID = DVI.ID_HDR AND DVI.TYPE = '" + Contains.CUNG_CAP + "'" +
            " WHERE (:namHd IS NULL OR HD.NAM_HD=TO_NUMBER(:namHd)) " +
            " AND (:soHdong IS NULL OR LOWER(HD.SO_HDONG) LIKE LOWER(CONCAT(CONCAT('%',:soHdong),'%'))) " +
            " AND (:dviMua IS NULL OR LOWER(DVI.TEN_DVI) LIKE LOWER(CONCAT(CONCAT('%',:dviMua),'%'))) " +
            " AND (:tenHdong IS NULL OR LOWER(HD.TEN_HDONG) LIKE LOWER(CONCAT(CONCAT('%',:tenHdong),'%')))" +
            " AND (:ngayKyHdTu IS NULL OR HD.NGAY_HLUC <= TO_DATE(:ngayKyHdTu,'yyyy-MM-dd'))" +
            " AND (:ngayKyHdDen IS NULL OR HD.NGAY_HLUC >=  TO_DATE(:ngayKyHdDen,'yyyy-MM-dd')) " +
            " AND (:trangThai IS NULL OR HD.TRANG_THAI = :trangThai)" +
            " AND (:maDvi IS NULL OR LOWER(HD.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    Page<HhHdongBkePmuahangHdr> searchPage (Integer namHd, String soHdong ,String dviMua,String tenHdong, String ngayKyHdTu, String ngayKyHdDen,  String trangThai, String maDvi, Pageable pageable);

    Optional<HhHdongBkePmuahangHdr> findAllBySoHdong(String soHdong);

    List<HhHdongBkePmuahangHdr> findAllByIdIn(List<Long> ids);
}
