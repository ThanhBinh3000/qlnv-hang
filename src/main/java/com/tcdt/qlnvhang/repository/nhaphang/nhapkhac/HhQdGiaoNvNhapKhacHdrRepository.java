package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.nvnhap.HhQdGiaoNvuNhapHangKhacHdr;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhQdGiaoNvuNhapKhacSearch;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhQdPdNhapKhacSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HhQdGiaoNvNhapKhacHdrRepository extends JpaRepository<HhQdGiaoNvuNhapHangKhacHdr, Long> {
    List<HhQdGiaoNvuNhapHangKhacHdr> findAllByIdIn (List<Long> ids);
    @Query(
            value = "SELECT qdnk " +
                    "FROM HhQdGiaoNvuNhapHangKhacHdr qdnk " +
                    " WHERE (:#{#req.nam} IS NULL OR qdnk.nam = :#{#req.nam}) " +
                    "  AND (:#{#req.soQd} IS NULL OR LOWER(qdnk.soQd) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.soQd}),'%'))) " +
                    "  AND (:#{#req.maDvi} IS NULL OR LOWER(qdnk.maDvi) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.maDvi}),'%')))" +
                    "  AND (:#{#req.trichYeu} IS NULL OR LOWER(qdnk.trichYeu) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.trichYeu}),'%')))" +
                    "  AND (:#{#req.loaiVthh} IS NULL OR LOWER(qdnk.loaiVthh) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.loaiVthh}),'%')))" +
                    "  AND (:#{#req.tuNgayQdStr} IS NULL OR qdnk.ngayQd >= TO_DATE(:#{#req.tuNgayQdStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.denNgayQdStr} IS NULL OR qdnk.ngayQd <= TO_DATE(:#{#req.denNgayQdStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.trangThai} IS NULL OR qdnk.trangThai = :#{#req.trangThai}) "+
                    "  ORDER BY qdnk.ngaySua desc , qdnk.ngayTao desc, qdnk.id desc"
            )
    Page<HhQdGiaoNvuNhapHangKhacHdr> search(HhQdGiaoNvuNhapKhacSearch req, Pageable pageable);
    Optional<HhQdGiaoNvuNhapHangKhacHdr> findById(Long id);
    List<HhQdGiaoNvuNhapHangKhacHdr> findBySoQd(String soQd);
}
