package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.HhQdPduyetKqlcntHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdPduyetKqcgHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface HhQdPduyetKqcgRepository extends JpaRepository<HhQdPduyetKqcgHdr, Long> {


//    @Query(value = "select * from HH_QD_PDUYET_KQCG_HDR PD where (:namKh IS NULL OR PD.NAM_KH = TO_NUMBER(:namKh)) " +
//            " AND (:ngayCgiaTu IS NULL OR PD.NGAY_KY >=  TO_DATE(:ngayCgiaTu,'yyyy-MM-dd')) " +
//            " AND (:ngayCgiaDen IS NULL OR PD.NGAY_KY <= TO_DATE(:ngayCgiaDen,'yyyy-MM-dd'))" +
//            " AND (:trangThai IS NULL OR PD.TRANG_THAI = :trangThai) " +
//            " AND (:maDvi IS NULL OR LOWER(PD.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
//            ,nativeQuery = true)
//    Page<HhQdPduyetKqcgHdr> searchPage(Integer namKh, String ngayCgiaTu, String ngayCgiaDen, String trangThai, String maDvi, Pageable pageable);

    Optional<HhQdPduyetKqcgHdr> findBySoQd(String soQd);

    List<HhQdPduyetKqcgHdr> findByIdIn(List<Long> id);

    HhQdPduyetKqcgHdr findAllById(Long id);

    @Transactional
    void deleteAllByIdIn(List<Long> ids);


    @Query(value = " SELECT * FROM HH_QD_PDUYET_KQCG_HDR QDPD "+
            " WHERE (:namKh IS NULL OR QDPD.NAM_KH = TO_NUMBER(:namKh)) "+
            " AND (:ngayCgiaTu IS NULL OR QDPD.NGAY_KY >= TO_DATE(:ngayCgiaTu, 'yyyy-MM-dd')) "+
            " AND (:ngayCgiaDen IS NULL OR QDPD.NGAY_KY <= TO_DATE(:ngayCgiaDen, 'yyyy-MM-dd')) "+
            " AND (:maDvi IS NULL OR QDPD.MA_DVI = :maDvi) "+
            " AND (:trangThai IS NULL OR QDPD.TRANG_THAI = :trangThai) ",
            countQuery = " SELECT COUNT(1) FROM HH_QD_PDUYET_KQCG_HDR QDPD "+
                    " WHERE (:namKh IS NULL OR QDPD.NAM_KH = TO_NUMBER(:namKh)) "+
                    " AND (:ngayCgiaTu IS NULL OR QDPD.NGAY_KY >= TO_DATE(:ngayCgiaTu, 'yyyy-MM-dd')) "+
                    " AND (:ngayCgiaDen IS NULL OR QDPD.NGAY_KY <= TO_DATE(:ngayCgiaDen, 'yyyy-MM-dd')) "+
                    " AND (:maDvi IS NULL OR QDPD.MA_DVI = :maDvi) "+
                    " AND (:trangThai IS NULL OR QDPD.TRANG_THAI = :trangThai) ",
            nativeQuery = true)
    Page<HhQdPduyetKqcgHdr> selectPage(Integer namKh, String ngayCgiaTu, String ngayCgiaDen,String maDvi, String trangThai, Pageable pageable);


}
