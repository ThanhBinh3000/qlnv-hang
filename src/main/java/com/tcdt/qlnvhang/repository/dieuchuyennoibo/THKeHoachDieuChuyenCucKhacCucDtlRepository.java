package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.TongHopKeHoachDieuChuyen.THKeHoachDieuChuyenCucKhacCucDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface THKeHoachDieuChuyenCucKhacCucDtlRepository extends JpaRepository<THKeHoachDieuChuyenCucKhacCucDtl,Long> {
    List<THKeHoachDieuChuyenCucKhacCucDtl> findByHdrId(Long id);

    List<THKeHoachDieuChuyenCucKhacCucDtl> findAllByHdrIdIn(List<Long> idList);

    @Query(nativeQuery = true, value="SELECT * FROM DCNB_TH_KE_HOACH_DCC_KC_DTL d\n" +
            "LEFT JOIN DCNB_KE_HOACH_DC_HDR h ON h.ID = d.DCNB_KE_HOACH_DC_HDR_ID \n" +
            "LEFT JOIN DCNB_KE_HOACH_DC_DTL dtl ON dtl.HDR_ID = h.ID \n" +
            "LEFT JOIN DCNB_TH_KE_HOACH_DCC_HDR hdr ON hdr.ID = d.HDR_ID \n" +
            "WHERE hdr.MA_DVI = ?1 AND hdr.TRANG_THAI = ?2 AND hdr.LOAI_DC = ?3 AND dtl.LOAI_VTHH = ?4 AND dtl.CLOAI_VTHH = ?5 AND hdr.NGAY_TAO AND (TO_DATE(TO_CHAR(hdr.NGAY_TAO ,'YYYY-MM-DD HH24:MI:SS'),'YYYY-MM-DD HH24:MI:SS') <= TO_DATE(?6,'YYYY-MM-DD HH24:MI:SS'))")
    List<THKeHoachDieuChuyenCucKhacCucDtl> findByDonViAndTrangThaiAndLoaiDcCuc(String maDVi, String trangThai, String loaiDieuChuyen, String loaiHangHoa, String chungLoaiHangHoa, String thoiGianTongHop);
}
