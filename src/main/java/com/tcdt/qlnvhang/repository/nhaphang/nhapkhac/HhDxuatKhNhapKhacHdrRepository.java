package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhDxuatKhNhapKhacHdr;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhDxuatKhNhapKhacSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface HhDxuatKhNhapKhacHdrRepository  extends JpaRepository<HhDxuatKhNhapKhacHdr, Long> {
    Optional<HhDxuatKhNhapKhacHdr> findBySoDxuat (String soDx);
    List<HhDxuatKhNhapKhacHdr> findAllByIdIn (List<Long> ids);
    List<HhDxuatKhNhapKhacHdr> findAllByThopId (Long id);
    List<HhDxuatKhNhapKhacHdr> findAllByNamKhoachAndLoaiHinhNxAndLoaiVthhAndTrangThaiInAndTrangThaiTh (Integer nam, String kieuNx, String loaiVthh, List<String> trangThai, String trangThaiTh);
    @Query(
            value = "SELECT khnk " +
                    "FROM HhDxuatKhNhapKhacHdr khnk " +
                    " WHERE (:#{#req.namKhoach} IS NULL OR khnk.namKhoach = :#{#req.namKhoach}) " +
                    "  AND (:#{#req.soDxuat} IS NULL OR LOWER(khnk.soDxuat) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.soDxuat}),'%'))) " +
                    "  AND (:#{#req.dvql} IS NULL OR LOWER(khnk.maDviDxuat) LIKE CONCAT(:#{#req.dvql},'%'))" +
                    "  AND (:#{#req.maDviDxuat} IS NULL OR LOWER(khnk.maDviDxuat) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.maDviDxuat}),'%')))" +
                    "  AND (:#{#req.tuNgayDxuatStr} IS NULL OR khnk.ngayDxuat >= TO_DATE(:#{#req.tuNgayDxuatStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.denNgayDxuatStr} IS NULL OR khnk.ngayDxuat <= TO_DATE(:#{#req.denNgayDxuatStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.tuNgayDuyetStr} IS NULL OR khnk.ngayPduyet >= TO_DATE(:#{#req.tuNgayDuyetStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.denNgayDuyetStr} IS NULL OR khnk.ngayPduyet <= TO_DATE(:#{#req.denNgayDuyetStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.trangThai} IS NULL OR khnk.trangThai = :#{#req.trangThai}) "+
                    "  ORDER BY khnk.trangThai asc, khnk.ngaySua desc , khnk.ngayTao desc"
            )
    Page<HhDxuatKhNhapKhacHdr> search(HhDxuatKhNhapKhacSearch req, Pageable pageable);

    @Transactional()
    @Modifying
    @Query(value = "UPDATE HH_DX_KHNK_HDR SET TRANG_THAI_TH=:trangThaiTh WHERE SO_DXUAT IN :soDxuatList", nativeQuery = true)
    void updateStatusInList(List<String> soDxuatList, String trangThaiTh);

    List<HhDxuatKhNhapKhacHdr> findAllByTrangThaiInAndTrangThaiTh(List<String> trangThai, String trangThaiTh);
}
