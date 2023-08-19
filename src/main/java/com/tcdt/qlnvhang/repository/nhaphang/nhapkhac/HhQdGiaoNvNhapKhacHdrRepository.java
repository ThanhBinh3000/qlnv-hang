package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.nvnhap.HhQdGiaoNvuNhapHangKhacHdr;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhBbNghiemThuNhapKhacSearch;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkPhieuKtclSearch;
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

    @Query(
            value = "SELECT qdgnv.* " +
                    "FROM hh_qd_giao_nvu_nhapxuat_khac qdgnv LEFT JOIN hh_qd_pdnk_dtl pdtdl ON pdtdl.id_dx_hdr = qdgnv.id_qd_pd_nk " +
                    " LEFT JOIN NH_BB_LAY_MAU_KHAC bblm ON bblm.ID_QD_GIAO_NV_NH = qdgnv.id " +
                    " WHERE 1=1" +
                    "  AND (:#{#req.maDvi} IS NULL OR LOWER(pdtdl.MA_CHI_CUC) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.maDvi}),'%')))" +
                    "  AND (:#{#req.loaiVthh} IS NULL OR LOWER(qdgnv.LOAI_VTHH) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.loaiVthh}),'%')))" +
                    "  AND (:#{#req.soQd} IS NULL OR LOWER(qdgnv.SO_QD) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.soQd}),'%')))" +
                    "  AND (:#{#req.soBienBan} IS NULL OR LOWER(bblm.SO_BIEN_BAN) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.soBienBan}),'%')))" +
                    "  AND (:#{#req.tuNgayLmStr} IS NULL OR bblm.NGAY_LAY_MAU >= TO_DATE(:#{#req.tuNgayLmStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.denNgayLmStr} IS NULL OR bblm.NGAY_LAY_MAU <= TO_DATE(:#{#req.denNgayLmStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.trangThai} IS NULL OR qdgnv.TRANG_THAI = :#{#req.trangThai}) "+
                    "  AND (:#{#req.checkIdBbLayMau} = 0 OR pdtdl.ID_BB_LAY_MAU IS NULL) " +
                    "  ORDER BY qdgnv.id desc", nativeQuery = true
    )
    Page<HhQdGiaoNvuNhapHangKhacHdr> dsQdNvuDuocLapBb(HhQdGiaoNvuNhapKhacSearch req, Pageable pageable);
    Optional<HhQdGiaoNvuNhapHangKhacHdr> findById(Long id);
    List<HhQdGiaoNvuNhapHangKhacHdr> findBySoQd(String soQd);
    @Query(
            value = " SELECT DISTINCT qdnk " +
                    " FROM HhQdGiaoNvuNhapHangKhacHdr qdnk " +
                    " LEFT JOIN HhQdPdNhapKhacDtl pdtdl ON pdtdl.idDxHdr = qdnk.idQdPdNk " +
                    " LEFT JOIN HhBbNghiemThuNhapKhac bbnt ON qdnk.id = bbnt.idQdGiaoNvNh" +
                    " WHERE (:#{#req.namKhoach} IS NULL OR qdnk.nam = :#{#req.namKhoach}) " +
                    "  AND (:#{#req.soQd} IS NULL OR LOWER(qdnk.soQd) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.soQd}),'%'))) " +
                    "  AND (:#{#req.maDvi} IS NULL OR LOWER(qdnk.maDvi) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.maDvi}),'%')))" +
                    "  AND (:#{#req.maDviChiCuc} IS NULL OR LOWER(pdtdl.maChiCuc) LIKE LOWER(CONCAT(:#{#req.maDviChiCuc},'%')))" +
                    "  AND (:#{#req.loaiVthh} IS NULL OR LOWER(qdnk.loaiVthh) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.loaiVthh}),'%')))" +
                    "  AND (:#{#req.tuNgayLPStr} IS NULL OR bbnt.ngayTao >= TO_DATE(:#{#req.tuNgayLPStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.denNgayLPStr} IS NULL OR bbnt.ngayTao <= TO_DATE(:#{#req.denNgayLPStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.tuNgayKTStr} IS NULL OR bbnt.ngayNghiemThu >= TO_DATE(:#{#req.tuNgayKTStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.denNgayKTStr} IS NULL OR bbnt.ngayNghiemThu <= TO_DATE(:#{#req.denNgayKTStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.soBbNtBq} IS NULL OR LOWER(bbnt.soBbNtBq) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.soBbNtBq}),'%'))) " +
                    "  AND (:#{#req.trangThai} IS NULL OR bbnt.trangThai = :#{#req.trangThai}) "+
                    "  AND (:#{#req.trangThaiQdnk} IS NULL OR qdnk.trangThai = :#{#req.trangThaiQdnk}) "+
                    "  ORDER BY qdnk.ngaySua desc , qdnk.ngayTao desc, qdnk.id desc"
    )
    Page<HhQdGiaoNvuNhapHangKhacHdr> searchBbNtBq(HhBbNghiemThuNhapKhacSearch req, Pageable pageable);

    @Query(
            value = " SELECT qdnk " +
                    " FROM HhQdGiaoNvuNhapHangKhacHdr qdnk " +
                    " LEFT JOIN HhQdPdNhapKhacDtl pdtdl ON pdtdl.idDxHdr = qdnk.idQdPdNk " +
                    " WHERE (:#{#req.maDvi} IS NULL OR LOWER(pdtdl.maChiCuc) LIKE LOWER(CONCAT(:#{#req.maDvi},'%')))" +
                    "  AND (:#{#req.loaiVthh} IS NULL OR LOWER(qdnk.loaiVthh) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.loaiVthh}),'%')))" +
                    "  AND (:#{#req.trangThai} IS NULL OR qdnk.trangThai = :#{#req.trangThai}) "+
                    "  ORDER BY qdnk.ngaySua desc , qdnk.ngayTao desc, qdnk.id desc"
    )
    List<HhQdGiaoNvuNhapHangKhacHdr> dsQdNvuDuocLapBbNtBqLd(HhBbNghiemThuNhapKhacSearch req);
    @Query(
            value = " SELECT qdnk " +
                    " FROM HhQdGiaoNvuNhapHangKhacHdr qdnk " +
                    " LEFT JOIN HhQdPdNhapKhacDtl pdtdl ON pdtdl.idDxHdr = qdnk.idQdPdNk " +
                    " LEFT JOIN HhNkPhieuKtcl ktcl ON qdnk.id = ktcl.idQdGiaoNvNh" +
                    " WHERE (:#{#req.namKhoach} IS NULL OR qdnk.nam = :#{#req.namKhoach}) " +
                    "  AND (:#{#req.soQd} IS NULL OR LOWER(qdnk.soQd) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.soQd}),'%'))) " +
                    "  AND (:#{#req.maDvi} IS NULL OR LOWER(qdnk.maDvi) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.maDvi}),'%')))" +
                    "  AND (:#{#req.maDviChiCuc} IS NULL OR LOWER(pdtdl.maChiCuc) LIKE LOWER(CONCAT(:#{#req.maDviChiCuc},'%')))" +
                    "  AND (:#{#req.loaiVthh} IS NULL OR LOWER(qdnk.loaiVthh) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.loaiVthh}),'%')))" +
                    "  AND (:#{#req.tuNgayLPStr} IS NULL OR ktcl.ngayTao >= TO_DATE(:#{#req.tuNgayLPStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.denNgayLPStr} IS NULL OR ktcl.ngayTao <= TO_DATE(:#{#req.denNgayLPStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.tuNgayGDStr} IS NULL OR ktcl.ngayGdinh >= TO_DATE(:#{#req.tuNgayGDStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.denNgayGDStr} IS NULL OR ktcl.ngayGdinh <= TO_DATE(:#{#req.denNgayGDStr}, 'YYYY-MM-DD HH24:MI:SS'))" +
                    "  AND (:#{#req.soPhieu} IS NULL OR LOWER(ktcl.soPhieu) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.soPhieu}),'%'))) " +
                    "  AND (:#{#req.kqDanhGia} IS NULL OR LOWER(ktcl.kqDanhGia) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.kqDanhGia}),'%'))) " +
                    "  AND (:#{#req.trangThai} IS NULL OR ktcl.trangThai = :#{#req.trangThai}) "+
                    "  ORDER BY qdnk.ngaySua desc , qdnk.ngayTao desc, qdnk.id desc"
    )
    Page<HhQdGiaoNvuNhapHangKhacHdr> searchPhieuKtcl(HhNkPhieuKtclSearch req, Pageable pageable);

    @Query(
            value = " SELECT qdnk " +
                    " FROM HhQdGiaoNvuNhapHangKhacHdr qdnk " +
                    " LEFT JOIN HhQdPdNhapKhacDtl pdtdl ON pdtdl.idDxHdr = qdnk.idQdPdNk " +
                    " WHERE (:#{#req.maDvi} IS NULL OR LOWER(pdtdl.maChiCuc) LIKE LOWER(CONCAT(:#{#req.maDvi},'%')))" +
                    "  AND (:#{#req.loaiVthh} IS NULL OR LOWER(qdnk.loaiVthh) LIKE LOWER(CONCAT(CONCAT('%', :#{#req.loaiVthh}),'%')))" +
                    "  AND (:#{#req.trangThai} IS NULL OR qdnk.trangThai = :#{#req.trangThai}) "+
                    "  ORDER BY qdnk.ngaySua desc , qdnk.ngayTao desc, qdnk.id desc"
    )
    List<HhQdGiaoNvuNhapHangKhacHdr> dsQdNvuDuocLapPhieuKtcl(HhNkPhieuKtclSearch req);
}
