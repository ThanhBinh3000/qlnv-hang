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

    @Query(value="FROM THKeHoachDieuChuyenCucKhacCucDtl d\n" +
            "LEFT JOIN DcnbKeHoachDcHdr h ON h.id = d.dcnbKeHoachDcHdrId \n" +
            "LEFT JOIN DcnbKeHoachDcDtl dtl ON dtl.hdrId = h.id \n" +
            "LEFT JOIN THKeHoachDieuChuyenCucHdr hdr ON hdr.id = d.hdrId \n" +
            "WHERE hdr.maDvi = ?1 AND hdr.trangThai = ?2 AND hdr.loaiDieuChuyen = ?3 " +
            "AND (?4 IS NULL OR dtl.loaiVthh = ?4) \n" +
            "AND (?5 IS NULL OR dtl.cloaiVthh = ?5)\n"+
            "AND (hdr.ngaytao <= ?6)")
    List<THKeHoachDieuChuyenCucKhacCucDtl> findByDonViAndTrangThaiAndLoaiDcCuc(String maDVi, String trangThai, String loaiDieuChuyen, String loaiHangHoa, String chungLoaiHangHoa, LocalDate thoiGianTongHop);
}
