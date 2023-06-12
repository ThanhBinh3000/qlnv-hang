package com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.tonghop;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxKhMttThopDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdgDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface XhThopDxKhBdgDtlRepository extends JpaRepository<XhThopDxKhBdgDtl,Long> {


    List<XhThopDxKhBdgDtl> findByIdThopHdr(Long idThopHdr);
    @Transactional
    @Modifying
    void deleteAllByIdIn(List<HhDxKhMttThopDtl> ids);

    @Query(value = " SELECT dtl.GIA_QD FROM KH_PAG_TONG_HOP_CTIET dtl " +
            "JOIN KH_PAG_GCT_QD_TCDTNN hdr ON dtl.QD_TCDTNN_ID = hdr.ID " +
            " WHERE hdr.TRANG_THAI = '29' AND hdr.LOAI_GIA = 'LG03'  AND hdr.CLOAI_VTHH = :cloaiVthh AND dtl.MA_DVI = :maDvi AND hdr.NAM_KE_HOACH = :namKhoach AND hdr.NGAY_HIEU_LUC <= SYSDATE " +
            " FETCH FIRST 1 ROWS ONLY ",
            nativeQuery = true)
    BigDecimal getDonGiaVatLt(String cloaiVthh, String maDvi, Integer namKhoach);

    @Query(value = " SELECT dtl.GIA_QD FROM KH_PAG_TT_CHUNG dtl " +
            "JOIN KH_PAG_GCT_QD_TCDTNN hdr ON dtl.QD_TCDTNN_ID = hdr.ID " +
            " WHERE hdr.TRANG_THAI = '29' AND hdr.LOAI_GIA = 'LG03'  AND dtl.CLOAI_VTHH = :cloaiVthh AND hdr.NAM_KE_HOACH = :namKhoach AND hdr.NGAY_HIEU_LUC <= SYSDATE " +
            " FETCH FIRST 1 ROWS ONLY ",
            nativeQuery = true)
    BigDecimal getDonGiaVatVt(String cloaiVthh, Integer namKhoach);

}
