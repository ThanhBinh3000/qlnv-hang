package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhThopKhNhapKhac;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhThopKhNhapKhacSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HhThopKhNhapKhacRepository extends JpaRepository<HhThopKhNhapKhac, Long> {
    @Query(
            value = " SELECT thop " +
                    " FROM HhThopKhNhapKhac thop " +
                    "  WHERE (:#{#req.namKhoach} IS NULL OR thop.namKhoach = :#{#req.namKhoach}) " +
                    "  AND (:#{#req.maTh} IS NULL OR LOWER(thop.maTh) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.maTh}),'%'))) " +
                    "  AND (:#{#req.tuNgayThStr} IS NULL OR thop.ngayTao >= TO_DATE(:#{#req.tuNgayThStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.denNgayThStr} IS NULL OR thop.ngayTao <= TO_DATE(:#{#req.denNgayThStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.tuNgayKyStr} IS NULL OR thop.ngayPduyet >= TO_DATE(:#{#req.tuNgayKyStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.denNgayKyStr} IS NULL OR thop.ngayPduyet <= TO_DATE(:#{#req.denNgayKyStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.trangThai} IS NULL OR thop.trangThai = :#{#req.trangThai}) " +
                    "  ORDER BY thop.ngaySua desc , thop.ngayTao desc, thop.id desc"
    )
    Page<HhThopKhNhapKhac> search(HhThopKhNhapKhacSearch req, Pageable pageable);
    List<HhThopKhNhapKhac> findAllByIdIn(List<Long> ids);
    List<HhThopKhNhapKhac> findAllBySoQdIsNull();
    List<HhThopKhNhapKhac> findAllByTrangThai(String trangThai);

    @Query(value = "UPDATE HH_THOP_KHNK SET TRANG_THAI =:trangThai WHERE ID = :idThHdr", nativeQuery = true)
    @Modifying
    @Transactional
    void updateTrangThai(Long idThHdr, String trangThai);
}
