package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhQdKhlcntDtl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface HhQdKhlcntDtlRepository extends JpaRepository<HhQdKhlcntDtl, Long> {

    void deleteAllByIdQdHdr(Long idQdHdr);

    List<HhQdKhlcntDtl> findAllByIdQdHdr (Long idQdHdr);
    List<HhQdKhlcntDtl> findAllByIdQdHdrIn (List<Long> ids);

    HhQdKhlcntDtl findByIdQdHdr(Long idQdHdr);

    @Query(value = "SELECT ID_QD_HDR,count(*) as c  from HH_QD_KHLCNT_DTL where ID_QD_HDR in (:qdIds) group by ID_QD_HDR",
            nativeQuery = true)
    List<Object[]> countAllBySoGthau(Collection<Long> qdIds);

    @Query(value = "SELECT ID_QD_HDR,NVL(SUM(DTL.TONG_TIEN),0) FROM HH_QD_KHLCNT_DTL DTL WHERE ID_QD_HDR in (:qdIds) group by ID_QD_HDR ",
            nativeQuery = true)
    List<Object[]> sumTongTienByIdHdr(Collection<Long> qdIds);

    @Query(value = " SELECT DTL.* FROM HH_QD_KHLCNT_DTL DTL " +
            " LEFT JOIN HH_QD_KHLCNT_HDR HDR ON HDR.ID = DTL.ID_QD_HDR " +
            " WHERE (:namKh IS NULL OR HDR.NAM_KHOACH = TO_NUMBER(:namKh)) " +
            " AND (:loaiVthh IS NULL OR HDR.LOAI_VTHH = :loaiVthh) " +
            " AND (:maDvi IS NULL OR DTL.MA_DVI = :maDvi)" +
            " AND HDR.TRANG_THAI = :trangThai AND HDR.LASTEST = 1 ",nativeQuery = true)
    Page<HhQdKhlcntDtl> selectPage(Integer namKh , String loaiVthh, String maDvi, String trangThai, Pageable pageable);


}
