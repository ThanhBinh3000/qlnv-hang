package com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.entities.quanlyphieukiemtrachatluonghangluongthuc.QlpktclhPhieuKtChatLuong;
import com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QlPhieuNhapKhoLtRepository extends BaseRepository<QlPhieuNhapKhoLt, Long>, QlPhieuNhapKhoLtRepositoryCustom {

    @Query(
            value = "SELECT * FROM QL_PHIEU_NHAP_KHO_LT PNKLT " +
                    "WHERE (:soPhieu IS NULL OR PNKLT.SO_PHIEU = TO_NUMBER(:soPhieu))" +
                    "  AND (:ngayNhapKho IS NULL OR TO_CHAR(PNKLT.NGAY_PHE_DUYET,'YYYY-MM-DD') = :ngayNhapKho)" +
                    "  AND (:maDiemKho IS NULL OR LOWER(PNKLT.MA_DIEM_KHO) = LOWER(:maDiemKho))" +
                    "  AND (:maNganLo IS NULL OR LOWER(PNKLT.MA_NGAN_LO) = LOWER(:maNganLo))" +
                    "  AND (:ngayTaoPhieu IS NULL OR TO_CHAR(PNKLT.NGAY_TAO,'YYYY-MM-DD') = :ngayTaoPhieu)" +
                    "  AND (:thoiGianGiaoNhan IS NULL OR TO_CHAR(PNKLT.THOI_GIAN_GIAO_NHAN,'YYYY-MM-DD') = :thoiGianGiaoNhan)" +
                    "  AND (:maNhaKho IS NULL OR LOWER(PNKLT.MA_NHA_KHO) = LOWER(:maNhaKho))" +
                    "  AND (:nguoiGiaoHang IS NULL OR LOWER(PNKLT.NGUOI_GIAO_HANG) LIKE LOWER(CONCAT(CONCAT('%', :nguoiGiaoHang), '%')))",
            nativeQuery = true)
    Page<QlPhieuNhapKhoLt> select(Long soPhieu, String ngayNhapKho, String maDiemKho, String maNganLo, String ngayTaoPhieu, String thoiGianGiaoNhan, String maNhaKho,String nguoiGiaoHang, Pageable pageable);

}
