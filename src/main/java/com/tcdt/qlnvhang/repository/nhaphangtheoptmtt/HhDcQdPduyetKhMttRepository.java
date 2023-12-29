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
    @Query(value = "select DISTINCT DC.* from HH_DC_QD_PDUYET_KHMTT_HDR DC LEFT JOIN HH_DC_QD_PDUYET_KHMTT_DX DX ON DC.ID = DX.ID_DC_HDR where (:namKh IS NULL OR DC.NAM_KH =TO_NUMBER(:namKh))" +
            " AND (:soQdDc IS NULL OR LOWER(DC.SO_QD_DC) LIKE LOWER(CONCAT(CONCAT('%', :soQdDc),'%'))) "+
            " AND (:trichYeu IS NULL OR LOWER(DC.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%'))) "+
            " AND (:ngayKyDcTu IS NULL OR DC.NGAY_KY_DC >=  TO_DATE(:ngayKyDcTu,'yyyy-MM-dd')) " +
            " AND (:ngayKyDcDen IS NULL OR DC.NGAY_KY_DC <= TO_DATE(:ngayKyDcDen,'yyyy-MM-dd'))" +
            " AND (:trangThai IS NULL OR DC.TRANG_THAI = :trangThai) " +
            " AND (:maDvi IS NULL OR LOWER(DX.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    Page<HhDcQdPduyetKhmttHdr> searchPage(Integer namKh, String soQdDc, String trichYeu, String ngayKyDcTu, String ngayKyDcDen, String trangThai, String maDvi, Pageable pageable);

    Optional<HhDcQdPduyetKhmttHdr> findBySoQdDc(String soQdDc);
    Optional<HhDcQdPduyetKhmttHdr> findBySoToTrinh(String soToTrinh);

    List<HhDcQdPduyetKhmttHdr> findAllByIdIn(List<Long> listId);

    List<HhDcQdPduyetKhmttHdr> findAllByIdQdGocOrderByIdDesc(Long id);

    HhDcQdPduyetKhmttHdr findBySoLanDieuChinh(Long soLanDc);

    @Query(value = "select * from HH_DC_QD_PDUYET_KHMTT_HDR" +
            " where 1=1 "+
            " AND TRANG_THAI = 29 "+
            " AND so_lan_dieu_chinh IN (select b.so_lan_dieu_chinh from ( "+
            " SELECT ID_QD_GOC, MAX(so_lan_dieu_chinh) as so_lan_dieu_chinh " +
            " FROM HH_DC_QD_PDUYET_KHMTT_HDR" +
            " where 1=1" +
            " AND TRANG_THAI = 29" +
            " GROUP BY ID_QD_GOC ) b) "
            ,nativeQuery = true)
    List<HhDcQdPduyetKhmttHdr> searchDsLastest();
}
