package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.THKeHoachDieuChuyenCucKhacCucDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface THKeHoachDieuChuyenCucKhacCucDtlRepository extends JpaRepository<THKeHoachDieuChuyenCucKhacCucDtl,Long> {
    List<THKeHoachDieuChuyenCucKhacCucDtl> findByHdrId(Long id);

    List<THKeHoachDieuChuyenCucKhacCucDtl> findAllByHdrIdIn(List<Long> idList);

    @Query(nativeQuery = true, value="SELECT d.* FROM DCNB_TH_KE_HOACH_DCC_KC_DTL d\n" +
            "LEFT JOIN DCNB_KE_HOACH_DC_HDR h ON to_char(h.id) = d.dcnb_Ke_Hoach_Dc_Hdr_Id \n" +
            "LEFT JOIN DCNB_KE_HOACH_DC_DTL dtl ON dtl.hdr_Id = h.id \n" +
            "LEFT JOIN DCNB_TH_KE_HOACH_DCC_HDR hdr ON hdr.id = d.hdr_Id \n" +
            "WHERE hdr.ma_Dvi = ?1 AND hdr.trang_Thai = ?2 AND hdr.loai_Dc = ?3 " +
            "AND (?4 IS NULL OR dtl.loai_Vthh = ?4) \n" +
            "AND (?5 IS NULL OR dtl.cloai_Vthh = ?5)\n"+
            "AND (hdr.ngay_tao <= ?6)")
    List<THKeHoachDieuChuyenCucKhacCucDtl> findByDonViAndTrangThaiAndLoaiDcCuc(String maDVi, String trangThai, String loaiDieuChuyen, String loaiHangHoa, String chungLoaiHangHoa, LocalDate thoiGianTongHop);

    List<THKeHoachDieuChuyenCucKhacCucDtl> findAllByHdrIdAndId(Long thKhDcHdrId, Long thKhDcDtlId);
}
