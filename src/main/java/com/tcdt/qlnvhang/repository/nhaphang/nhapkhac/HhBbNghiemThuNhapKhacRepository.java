package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhBbNghiemThuNhapKhac;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhBbNghiemThuNhapKhacSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HhBbNghiemThuNhapKhacRepository extends JpaRepository<HhBbNghiemThuNhapKhac, Long> {
    Optional<HhBbNghiemThuNhapKhac> findBySoBbNtBq (String soBb);
    @Query(
            value = "SELECT khnk " +
                    "FROM HhBbNghiemThuNhapKhac khnk " +
                    " WHERE (:#{#req.namKhoach} IS NULL OR khnk.namKhoach = :#{#req.namKhoach}) " +
                    "  AND (:#{#req.soBbNtBq} IS NULL OR LOWER(khnk.soBbNtBq) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.soBbNtBq}),'%'))) " +
                    "  AND (:#{#req.soQd} IS NULL OR LOWER(khnk.soQdGiaoNvNh) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.soQd}),'%')))" +
                    "  AND (:#{#req.tuNgayLPStr} IS NULL OR khnk.ngayTao >= TO_DATE(:#{#req.tuNgayLPStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.denNgayLPStr} IS NULL OR khnk.ngayTao <= TO_DATE(:#{#req.denNgayLPStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.tuNgayKTStr} IS NULL OR khnk.ngayNghiemThu >= TO_DATE(:#{#req.tuNgayKTStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.denNgayKTStr} IS NULL OR khnk.ngayNghiemThu <= TO_DATE(:#{#req.denNgayKTStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.trangThai} IS NULL OR khnk.trangThai = :#{#req.trangThai}) "+
                    "  AND (:#{#req.loaiVthh} IS NULL OR khnk.loaiVthh = :#{#req.loaiVthh}) "+
                    "  ORDER BY khnk.ngaySua desc , khnk.ngayTao desc, khnk.id desc"
    )
    Page<HhBbNghiemThuNhapKhac> search(HhBbNghiemThuNhapKhacSearch req, Pageable pageable);
    List<HhBbNghiemThuNhapKhac> findByIdQdGiaoNvNhAndMaLoKhoAndMaNganKho(Long idQdGiaoNvNh, String maLoKho, String maNganKho);
}
