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
            "LEFT JOIN THKeHoachDieuChuyenCucHdr hdr ON hdr.id = d.hdrId \n" +
            "WHERE hdr.maDvi = ?1 AND hdr.trangThai = ?2 AND hdr.loaiDieuChuyen = ?3 " +
            "AND hdr.id not in (select distinct qdtc.idDxuat from DcnbQuyetDinhDcTcHdr qdtc where 1 =1 AND qdtc.idDxuat is not null )" +
            "AND hdr.ngayTao <= ?4 AND (hdr.idThTongCuc is null)")
    List<THKeHoachDieuChuyenCucKhacCucDtl> findByDonViAndTrangThaiAndLoaiDcCuc(String maDVi, String trangThai, String loaiDieuChuyen, LocalDateTime thoiGianTongHop);

    List<THKeHoachDieuChuyenCucKhacCucDtl> findAllByHdrIdAndId(Long thKhDcHdrId, Long thKhDcDtlId);
}
