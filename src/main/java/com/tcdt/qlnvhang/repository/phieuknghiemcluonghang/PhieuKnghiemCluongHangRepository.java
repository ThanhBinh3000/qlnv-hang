package com.tcdt.qlnvhang.repository.phieuknghiemcluonghang;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bblaymaubangiaomau.BienBanLayMau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuknghiemcl.PhieuKnghiemCluongHang;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PhieuKnghiemCluongHangRepository extends BaseRepository<PhieuKnghiemCluongHang, Long> {

    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    List<PhieuKnghiemCluongHang> findBySoQdGiaoNvNhAndMaDvi(String idQdGiaoNvNh, String maDvi);

    @Query(value = " SELECT PKN.* " +
            " FROM NH_PHIEU_KNGHIEM_CLUONG PKN " +
            " WHERE 1 = 1 " +
            "  AND (:soPhieuKncl IS NULL OR LOWER(PKN.SO_PHIEU_KIEM_NGHIEM_CL) LIKE LOWER(CONCAT(CONCAT('%', :soPhieuKncl),'%'))) " +
            "  AND (:soBbBanGiao IS NULL OR LOWER(PKN.SO_BB_LAY_MAU) LIKE LOWER(CONCAT(CONCAT('%', :soBbBanGiao),'%'))) " +
            "  AND (:soBbNhapDayKho IS NULL OR LOWER(PKN.SO_BB_NHAP_DAY_KHO) LIKE LOWER(CONCAT(CONCAT('%', :soBbNhapDayKho),'%'))) " +
            "  AND (:tuNgayKncl IS NULL OR PKN.NGAY_LAY_MAU >= TO_DATE(:tuNgayKncl, 'YYYY-MM-DD HH24:MI:SS')) " +
            "  AND (:denNgayKncl IS NULL OR PKN.NGAY_LAY_MAU <= TO_DATE(:denNgayKncl, 'YYYY-MM-DD HH24:MI:SS')) " +
            " AND (:maDviUser IS NULL OR PKN.MA_DVI LIKE CONCAT(:maDviUser, '%'))  " +
            " AND (:maDvi IS NULL OR PKN.MA_DVI LIKE CONCAT(:maDvi, '%'))  " +
            " AND (:idQdGiaoNvNh IS NULL OR PKN.ID_QD_GIAO_NV_NH = :idQdGiaoNvNh)  ",
            countQuery = " SELECT COUNT(1) " +
                    " FROM NH_PHIEU_KNGHIEM_CLUONG PKN " +
                    " WHERE 1 = 1 " +
                    "  AND (:soPhieuKncl IS NULL OR LOWER(PKN.SO_PHIEU_KIEM_NGHIEM_CL) LIKE LOWER(CONCAT(CONCAT('%', :soPhieuKncl),'%'))) " +
                    "  AND (:soBbBanGiao IS NULL OR LOWER(PKN.SO_BB_LAY_MAU) LIKE LOWER(CONCAT(CONCAT('%', :soBbBanGiao),'%'))) " +
                    "  AND (:soBbNhapDayKho IS NULL OR LOWER(PKN.SO_BB_NHAP_DAY_KHO) LIKE LOWER(CONCAT(CONCAT('%', :soBbNhapDayKho),'%'))) " +
                    "  AND (:tuNgayKncl IS NULL OR PKN.NGAY_LAY_MAU >= TO_DATE(:tuNgayKncl, 'YYYY-MM-DD HH24:MI:SS')) " +
                    "  AND (:denNgayKncl IS NULL OR PKN.NGAY_LAY_MAU <= TO_DATE(:denNgayKncl, 'YYYY-MM-DD HH24:MI:SS')) " +
                    " AND (:maDviUser IS NULL OR PKN.MA_DVI LIKE CONCAT(:maDviUser, '%'))  " +
                    " AND (:maDvi IS NULL OR PKN.MA_DVI LIKE CONCAT(:maDvi, '%'))  " +
                    " AND (:idQdGiaoNvNh IS NULL OR PKN.ID_QD_GIAO_NV_NH = :idQdGiaoNvNh)  ",
            nativeQuery = true
    )
    List<PhieuKnghiemCluongHang> getDanhSachPhieuKncl(Long idQdGiaoNvNh, String soPhieuKncl, String soBbBanGiao,String soBbNhapDayKho,String tuNgayKncl,String denNgayKncl, String maDvi, String maDviUser);


//    Optional<PhieuKnghiemCluongHang> findFirstBySoPhieu(String soPhieu);
}
