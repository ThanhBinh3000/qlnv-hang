package com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat;

import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaPhanLo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

public interface XhDxKhBanDauGiaPhanLoRepository extends JpaRepository<XhDxKhBanDauGiaPhanLo,Long> {

    void deleteAllByIdDtl(Long idDtl);

    List<XhDxKhBanDauGiaPhanLo> findByIdDtl(Long idDtl);

    @Transactional
    void deleteAllByIdDtlIn(List<Long> idDtl);

    @Query(value = " SELECT dtl.GIA_QD FROM KH_PAG_TONG_HOP_CTIET dtl " +
            "JOIN KH_PAG_GCT_QD_TCDTNN hdr ON dtl.QD_TCDTNN_ID = hdr.ID " +
            " WHERE hdr.TRANG_THAI = '29' AND hdr.LOAI_GIA = 'LG04'  AND hdr.CLOAI_VTHH = :cloaiVthh AND dtl.MA_DVI = :maDvi AND hdr.NAM_KE_HOACH = :namKhoach AND hdr.NGAY_HIEU_LUC <= SYSDATE " +
            " FETCH FIRST 1 ROWS ONLY ",
            nativeQuery = true)
    BigDecimal getDonGiaVatLt(String cloaiVthh, String maDvi, Integer namKhoach);

    @Query(value = " SELECT dtl.GIA_QD FROM KH_PAG_TT_CHUNG dtl " +
            "JOIN KH_PAG_GCT_QD_TCDTNN hdr ON dtl.QD_TCDTNN_ID = hdr.ID " +
            " WHERE hdr.TRANG_THAI = '29' AND hdr.LOAI_GIA = 'LG04'  AND dtl.CLOAI_VTHH = :cloaiVthh AND hdr.NAM_KE_HOACH = :namKhoach AND hdr.NGAY_HIEU_LUC <= SYSDATE " +
            " FETCH FIRST 1 ROWS ONLY ",
            nativeQuery = true)
    BigDecimal getDonGiaVatVt(String cloaiVthh, Integer namKhoach);


}
