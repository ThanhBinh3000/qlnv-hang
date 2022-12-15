package com.tcdt.qlnvhang.repository;


import com.tcdt.qlnvhang.table.HhDchinhDxKhLcntHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface HhDchinhDxKhLcntHdrRepository extends CrudRepository<HhDchinhDxKhLcntHdr, Long> {

    Optional<HhDchinhDxKhLcntHdr> findBySoQdDc(String soQd);

    List<HhDchinhDxKhLcntHdr> findAllByIdIn (List<Long> ids);

    @Transactional
    @Modifying
    void deleteAllByIdIn(List<Long> ids);


    @Query(value = " SELECT * FROM HH_DC_DX_LCNT_HDR  HDR"+
            " WHERE (:namKh IS NULL OR HDR.NAM_KH = TO_NUMBER(:namKh)) "+
            " AND (:soQd IS NULL OR LOWER(HDR.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :soQd),'%'))) "+
            " AND (:trichYeu IS NULL OR LOWER(HDR.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%', :trichYeu),'%'))) "+
            " AND (:tuNgayQd IS NULL OR HDR.NGAY_QD >= TO_DATE(:tuNgayQd, 'yyyy-MM-dd')) "+
            " AND (:denNgayQd IS NULL OR HDR.NGAY_QD <= TO_DATE(:denNgayQd, 'yyyy-MM-dd')) ",
            nativeQuery = true)
    Page<HhDchinhDxKhLcntHdr> selectPage(String namKh, String soQd,String trichYeu, String tuNgayQd, String denNgayQd, Pageable pageable);

//    @Query(value = "SELECT * FROM HH_QD_KHLCNT_HDR DC_DX " +
//            " WHERE (:namKh IS NULL OR DC_DX.NAM_KHOACH = TO_NUMBER(:namKh)) "+
//            " AND (:loaiVthh IS NULL OR DC_DX.LOAI_VTHH = :loaiVthh) "+
//            " AND (:soQd IS NULL OR DC_DX.SO_QD = :soQd) "+
//            " AND (:tuNgayQd IS NULL OR DC_DX.NGAY_QD >= TO_DATE(:tuNgayQd, 'yyyy-MM-dd')) "+
//            " AND (:denNgayQd IS NULL OR DC_DX.NGAY_QD <= TO_DATE(:denNgayQd, 'yyyy-MM-dd')) " +
//            " AND (:trangThai IS NULL OR DC_DX.TRANG_THAI = :trangThai) ",
//            nativeQuery = true)
//    List<HhDchinhDxKhLcntHdr> selectAll(String namKh, String loaiVthh, String soQd, String tuNgayQd, String denNgayQd, String trangThai);

}
