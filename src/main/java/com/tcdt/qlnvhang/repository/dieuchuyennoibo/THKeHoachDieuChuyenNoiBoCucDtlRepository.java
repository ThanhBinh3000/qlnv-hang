package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.THKeHoachDieuChuyenNoiBoCucDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface THKeHoachDieuChuyenNoiBoCucDtlRepository extends JpaRepository<THKeHoachDieuChuyenNoiBoCucDtl, Long> {
    List<THKeHoachDieuChuyenNoiBoCucDtl> findByHdrId(Long hdrId);

    List<THKeHoachDieuChuyenNoiBoCucDtl> findAllByHdrIdIn(List<Long> ids);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM DCNB_TH_KE_HOACH_DCC_NBC_DTL d WHERE d.HDR.ID= ?1")
    void deleteByHdrId(Long id);

    @Query(nativeQuery = true, value="SELECT * FROM DCNB_TH_KE_HOACH_DCC_NBC_DTL d\n" +
            "LEFT JOIN DCNB_KE_HOACH_DC_HDR h ON h.ID = d.DCNB_KE_HOACH_DC_HDR_ID \n" +
            "LEFT JOIN DCNB_KE_HOACH_DC_DTL dtl ON dtl.HDR_ID = h.ID \n" +
            "LEFT JOIN DCNB_TH_KE_HOACH_DCC_HDR hdr ON hdr.ID = d.HDR_ID \n" +
            "WHERE hdr.MA_DVI = ?1 AND hdr.TRANG_THAI = ?2 AND hdr.LOAI_DC = ?3 " +
            "AND (?4 IS NULL OR dtl.LOAI_VTHH = ?4) \n" +
            "AND (?5 IS NULL OR dtl.CLOAI_VTHH = ?5)\n" +
            "AND (TO_DATE(TO_CHAR(hdr.NGAY_TAO ,'YYYY-MM-DD HH24:MI:SS'),'YYYY-MM-DD HH24:MI:SS') <= TO_DATE(?6,'YYYY-MM-DD HH24:MI:SS'))")
    List<THKeHoachDieuChuyenNoiBoCucDtl> findByDonViAndTrangThaiTongCuc(String maDVi, String daduyetLdc, String giua2ChiCucTrong1Cuc, String loaiHangHoa, String chungLoaiHangHoa, String format);
}
