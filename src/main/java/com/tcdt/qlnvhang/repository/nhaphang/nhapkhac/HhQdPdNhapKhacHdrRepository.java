package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.qdpdnk.HhQdPdNhapKhacHdr;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhQdPdNhapKhacSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HhQdPdNhapKhacHdrRepository extends JpaRepository<HhQdPdNhapKhacHdr, Long> {
    Optional<HhQdPdNhapKhacHdr> findBySoDxuat (String soDx);
    List<HhQdPdNhapKhacHdr> findAllByIdIn (List<Long> ids);
    @Query(
            value = "SELECT qdnk " +
                    "FROM HhQdPdNhapKhacHdr qdnk " +
                    " WHERE (:#{#req.namKhoach} IS NULL OR qdnk.namKhoach = :#{#req.namKhoach}) " +
                    "  AND (:#{#req.soQd} IS NULL OR LOWER(qdnk.soQd) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.soQd}),'%'))) " +
                    "  AND (:#{#req.maDvi} IS NULL OR LOWER(qdnk.maDvi) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.maDvi}),'%')))" +
                    "  AND (:#{#req.lastest} IS NULL OR LOWER(qdnk.lastest) = :#{#req.lastest}) " +
                    "  AND (:#{#req.tuNgayQdPdStr} IS NULL OR qdnk.ngayKyQd >= TO_DATE(:#{#req.tuNgayQdPdStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.denNgayQdPdStr} IS NULL OR qdnk.ngayKyQd <= TO_DATE(:#{#req.denNgayQdPdStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.tuNgayDuyetStr} IS NULL OR qdnk.ngayPduyet >= TO_DATE(:#{#req.tuNgayDuyetStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.denNgayDuyetStr} IS NULL OR qdnk.ngayPduyet <= TO_DATE(:#{#req.denNgayDuyetStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.trangThai} IS NULL OR qdnk.trangThai = :#{#req.trangThai}) "+
                    "  ORDER BY qdnk.id desc, qdnk.ngayTao desc, qdnk.ngaySua desc"
            )
    Page<HhQdPdNhapKhacHdr> search(HhQdPdNhapKhacSearch req, Pageable pageable);
    Optional<HhQdPdNhapKhacHdr> findById(Long id);
    List<HhQdPdNhapKhacHdr> findBySoQd(String soQd);
    Optional<HhQdPdNhapKhacHdr> findByIdTh (Long idTh);
}
