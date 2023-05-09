package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.THKeHoachDieuChuyenCucKhacCucDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface THKeHoachDieuChuyenCucKhacCucDtlRepository extends JpaRepository<THKeHoachDieuChuyenCucKhacCucDtl,Long> {
    List<THKeHoachDieuChuyenCucKhacCucDtl> findByHdrId(Long id);

    List<THKeHoachDieuChuyenCucKhacCucDtl> findAllByHdrIdIn(List<Long> idList);

    @Query(nativeQuery = true, value="SELECT d.* FROM DCNB_TH_KE_HOACH_DCC_KC_DTL d\n" +
            "LEFT JOIN DCNB_TH_KE_HOACH_DCC_HDR hdr ON hdr.id = d.hdr_Id \n" +
            "WHERE hdr.ma_Dvi = ?1 AND hdr.trang_Thai = ?2 AND hdr.loai_Dc = ?3 " +
            "AND (hdr.ngay_tao <= ?4)")
    List<THKeHoachDieuChuyenCucKhacCucDtl> findByDonViAndTrangThaiAndLoaiDcCuc(String maDVi, String trangThai, String loaiDieuChuyen, LocalDate thoiGianTongHop);

    List<THKeHoachDieuChuyenCucKhacCucDtl> findAllByHdrIdAndId(Long thKhDcHdrId, Long thKhDcDtlId);
}
