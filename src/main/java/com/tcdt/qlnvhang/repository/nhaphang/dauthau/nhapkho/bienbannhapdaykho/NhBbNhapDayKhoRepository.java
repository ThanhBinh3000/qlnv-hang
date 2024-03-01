package com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bienbannhapdaykho;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bienbanchuanbikho.NhBienBanChuanBiKho;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKho;
import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhBbNhapDayKhoRepository extends BaseRepository<NhBbNhapDayKho, Long> {

//    @Query(
//            value = "Select BBNDKLT.* \n" +
//                    "FROM NH_BB_NHAP_DAY_KHO_LT BBNDKLT\n" +
//                    "WHERE (:soBienBan IS NULL OR LOWER(BBNDKLT.SO_BIEN_BAN) LIKE LOWER(CONCAT(CONCAT('%', :soBienBan), '%')))\n" +
//                    "  AND ((:ngayBatDauNhap IS NULL OR BBNDKLT.NGAY_BAT_DAU_NHAP >= TO_DATE(:ngayBatDauNhap,'yyyy-MM-dd'))\n" +
//                    "  OR (:ngayKetThucNhap IS NULL OR BBNDKLT.NGAY_KET_THUC_NHAP <= TO_DATE(:ngayKetThucNhap,'yyyy-MM-dd')))\n" +
//                    "  AND (:ngayNhapDayKhoTu IS NULL OR BBNDKLT.NGAY_NHAP_DAY_KHO >= TO_DATE(:ngayNhapDayKhoTu,'yyyy-MM-dd'))\n" +
//                    "  AND (:ngayNhapDayKhoDen IS NULL OR BBNDKLT.NGAY_NHAP_DAY_KHO <= TO_DATE(:ngayNhapDayKhoDen,'yyyy-MM-dd'))\n" +
//                    "  AND (:diemKho IS NULL OR BBNDKLT.MA_DIEM_KHO = :diemKho)\n" +
//                    "  AND (:nhaKho IS NULL OR BBNDKLT.MA_NHA_KHO = :nhaKho)\n" +
//                    "  AND (:maKhoNganLo IS NULL OR BBNDKLT.MA_KHO_NGAN_LO = :maKhoNganLo)\n" +
//                    "  AND (:kyThuatVien IS NULL OR LOWER(BBNDKLT.KY_THUAT_VIEN) LIKE LOWER(CONCAT(CONCAT('%', :kyThuatVien), '%')))",
//            nativeQuery = true)
//    Page<NhBbNhapDayKho> select(String soBienBan, String ngayBatDauNhap, String ngayKetThucNhap, String ngayNhapDayKhoTu, String ngayNhapDayKhoDen, String diemKho, String nhaKho, String maKhoNganLo, String kyThuatVien, Pageable pageable);
//
//    @Transactional
//    @Modifying
//    void deleteByIdIn(Collection<Long> ids);
//
//    Optional<NhBbNhapDayKho> findFirstBySoBienBan(String soBb);

    List<NhBbNhapDayKho> findByIdQdGiaoNvNhAndMaDvi(Long idQdGiaoNvNh,String maDvi);

    NhBbNhapDayKho findByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh);

    @Query(
            value = " SELECT BB FROM NhBbNhapDayKho BB " +
                    " WHERE 1 = 1 " +
                    " AND (:#{#req.tuNgayNhapDayKhoStr} IS NULL OR BB.ngayKetThucNhap >= TO_DATE(:#{#req.tuNgayNhapDayKhoStr}, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:#{#req.denNgayNhapDayKhoStr} IS NULL OR BB.ngayKetThucNhap <= TO_DATE(:#{#req.denNgayNhapDayKhoStr}, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:#{#req.idDdiemGiaoNvNh} IS NULL OR BB.idDdiemGiaoNvNh = :#{#req.idDdiemGiaoNvNh}) " ,
            countQuery = " SELECT COUNT * FROM ( SELECT BB FROM NhBbNhapDayKho BB " +
                    " WHERE 1 = 1 " +
                    " AND (:#{#req.tuNgayNhapDayKhoStr} IS NULL OR BB.ngayKetThucNhap >= TO_DATE(:#{#req.tuNgayNhapDayKhoStr}, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:#{#req.denNgayNhapDayKhoStr} IS NULL OR BB.ngayKetThucNhap <= TO_DATE(:#{#req.denNgayNhapDayKhoStr}, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:#{#req.idDdiemGiaoNvNh} IS NULL OR BB.idDdiemGiaoNvNh = :#{#req.idDdiemGiaoNvNh}) "
    )
    NhBbNhapDayKho timKiemByDiaDiemNh (HhQdNhapxuatSearchReq req);
}
