package com.tcdt.qlnvhang.repository.quanlybienbannhapdaykholuongthuc;

import com.tcdt.qlnvhang.entities.nhaphang.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface QlBienBanNhapDayKhoLtRepository extends BaseRepository<QlBienBanNhapDayKhoLt, Long>, QlBienBanNhapDayKhoLtRepositoryCustom {

    @Query(
            value = "Select BBNDKLT.* \n" +
                    "FROM NH_BB_NHAP_DAY_KHO_LT BBNDKLT\n" +
                    "WHERE (:soBienBan IS NULL OR LOWER(BBNDKLT.SO_BIEN_BAN) LIKE LOWER(CONCAT(CONCAT('%', :soBienBan), '%')))\n" +
                    "  AND ((:ngayBatDauNhap IS NULL OR BBNDKLT.NGAY_BAT_DAU_NHAP >= TO_DATE(:ngayBatDauNhap,'yyyy-MM-dd'))\n" +
                    "  OR (:ngayKetThucNhap IS NULL OR BBNDKLT.NGAY_KET_THUC_NHAP <= TO_DATE(:ngayKetThucNhap,'yyyy-MM-dd')))\n" +
                    "  AND (:ngayNhapDayKhoTu IS NULL OR BBNDKLT.NGAY_NHAP_DAY_KHO >= TO_DATE(:ngayNhapDayKhoTu,'yyyy-MM-dd'))\n" +
                    "  AND (:ngayNhapDayKhoDen IS NULL OR BBNDKLT.NGAY_NHAP_DAY_KHO <= TO_DATE(:ngayNhapDayKhoDen,'yyyy-MM-dd'))\n" +
                    "  AND (:diemKho IS NULL OR BBNDKLT.MA_DIEM_KHO = :diemKho)\n" +
                    "  AND (:nhaKho IS NULL OR BBNDKLT.MA_NHA_KHO = :nhaKho)\n" +
                    "  AND (:maKhoNganLo IS NULL OR BBNDKLT.MA_KHO_NGAN_LO = :maKhoNganLo)\n" +
                    "  AND (:kyThuatVien IS NULL OR LOWER(BBNDKLT.KY_THUAT_VIEN) LIKE LOWER(CONCAT(CONCAT('%', :kyThuatVien), '%')))",
            nativeQuery = true)
    Page<QlBienBanNhapDayKhoLt> select(String soBienBan, String ngayBatDauNhap, String ngayKetThucNhap, String ngayNhapDayKhoTu, String ngayNhapDayKhoDen, String diemKho, String nhaKho, String maKhoNganLo,String kyThuatVien, Pageable pageable);

    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<QlBienBanNhapDayKhoLt> findFirstBySoBienBan(String soBb);
}
