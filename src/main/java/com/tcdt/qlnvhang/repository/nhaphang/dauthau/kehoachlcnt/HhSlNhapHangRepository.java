package com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.HhSlNhapHang;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface HhSlNhapHangRepository extends BaseRepository<HhSlNhapHang, Long> {
    @Query(value = " SELECT NVL(SUM(sl.SO_LUONG),0) FROM HH_SL_NHAP_HANG sl " +
            " WHERE 1 = 1 " +
            " AND sl.NAM_KHOACH = :namKh " +
            " AND sl.MA_DVI LIKE CONCAT(:maDvi,'%') " +
            " AND sl.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%') " +
            " AND sl.ID_DX_KHLCNT IS NOT NULL",
            nativeQuery = true)
    BigDecimal countSLDalenKh(Integer namKh, String loaiVthh, String maDvi);

    @Query(value = " SELECT NVL(SUM(sl.SO_LUONG),0) FROM HH_SL_NHAP_HANG sl " +
            " WHERE 1 = 1 " +
            " AND sl.NAM_KHOACH = :namKh " +
            " AND sl.MA_DVI LIKE CONCAT(:maDvi,'%') " +
            " AND sl.LOAI_VTHH LIKE CONCAT(:loaiVthh,'%') " +
            " AND sl.ID_QD_KHLCNT IS NOT NULL",
            nativeQuery = true)
    BigDecimal countSLDalenQd(Integer namKh, String loaiVthh, String maDvi);

    List<HhSlNhapHang> findAllByIdQdKhlcnt(Long id);
}
