package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.THKeHoachDieuChuyenNoiBoCucDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface THKeHoachDieuChuyenNoiBoCucDtlRepository extends JpaRepository<THKeHoachDieuChuyenNoiBoCucDtl, Long> {
    List<THKeHoachDieuChuyenNoiBoCucDtl> findByHdrId(Long hdrId);

    List<THKeHoachDieuChuyenNoiBoCucDtl> findAllByHdrIdIn(List<Long> ids);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM DCNB_TH_KE_HOACH_DCC_NBC_DTL d WHERE d.HDR.ID= ?1")
    void deleteByHdrId(Long id);

    @Query(value="FROM THKeHoachDieuChuyenNoiBoCucDtl d\n" +
            "LEFT JOIN THKeHoachDieuChuyenCucHdr h ON h.id = d.hdrId \n" +
            "LEFT JOIN DcnbKeHoachDcDtl dtl ON dtl.id = d.dcKeHoachDcDtlId \n" +
            "LEFT JOIN DcnbKeHoachDcHdr hdr ON hdr.id = d.dcKeHoachDcHdrId \n" +
            "WHERE h.maDvi = ?1 AND h.trangThai = ?2 AND h.loaiDieuChuyen = ?3 " +
            "AND (?4 IS NULL OR dtl.loaiVthh = ?4) \n" +
            "AND (?5 IS NULL OR dtl.cloaiVthh = ?5)\n" )
//            "AND h.ngaytao <= ?6 ")
    List<THKeHoachDieuChuyenNoiBoCucDtl> findByDonViAndTrangThaiTongCuc(String maDVi, String daduyetLdc, String giua2ChiCucTrong1Cuc, String loaiHangHoa, String chungLoaiHangHoa, LocalDateTime thoiGianTongHop);
}
