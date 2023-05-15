package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DcnbKeHoachDcDtlRepository extends JpaRepository<DcnbKeHoachDcDtl, Long> {

    List<DcnbKeHoachDcDtl> findByDcnbKeHoachDcHdrId(Long idHdr);

    List<DcnbKeHoachDcDtl> findByDcnbKeHoachDcHdrIdIn(List<Long> ids);

    @Query(value ="SELECT distinct dtl FROM DcnbKeHoachDcDtl dtl left join DcnbKeHoachDcHdr hdr on hdr.id = dtl.hdrId " +
            "WHERE hdr.maDvi = ?1 AND hdr.trangThai = ?2 AND hdr.type = ?3 AND hdr.loaiDc = ?4 AND hdr.ngayTao <= ?5")
    List<DcnbKeHoachDcDtl> findByDonViAndTrangThaiChiCuc(String maDvi, String trangThai,String type,String loaiDieuChuyen, LocalDateTime thoiGianTongHop);

    @Query(nativeQuery = true,value ="SELECT * FROM DCNB_KE_HOACH_DC_DTL KHDTL\n" +
            "LEFT JOIN DCNB_KE_HOACH_DC_HDR KHHDR ON KHHDR.ID = KHDTL.HDR_ID\n" +
            "LEFT JOIN DM_DONVI dv ON dv.MA_DVI = hdr.MA_DVI \n" +
            "WHERE dv.MA_DVI_CHA = ?1 AND KHHDR.TRANG_THAI = ?2 AND (TO_DATE(TO_CHAR(KHHDR.NGAY_TAO,'YYYY-MM-DD'),'YYYY-MM-DD') <= TO_DATE(?3,'YYYY-MM-DD')) AND KHHDR.LOAI_DC = 'CHI_CUC' AND KHHDR.TYPE = 'DC'")
    List<DcnbKeHoachDcDtl> findByDonViChaAndTrangThaiChiCuc(String maDvi, String trangThai, String thoiGianTongHop);

    @Query(nativeQuery = true,value ="SELECT * FROM DCNB_KE_HOACH_DC_DTL KHDTL\n" +
            "LEFT JOIN DCNB_KE_HOACH_DC_HDR KHHDR ON KHHDR.ID = KHDTL.HDR_ID\n" +
            "WHERE KHHDR.MA_DVI = ?1 AND KHHDR.TRANG_THAI = ?2 AND (TO_DATE(TO_CHAR(KHHDR.NGAY_TAO,'YYYY-MM-DD'),'YYYY-MM-DD') <= TO_DATE(?3,'YYYY-MM-DD')) AND KHHDR.LOAI_DC = 'CUC' AND KHHDR.TYPE = 'DC'")
    List<DcnbKeHoachDcDtl> findByDonViChaAndTrangThaiCuc(String maDvi,String trangThai, String thoiGianTongHop);

    List<DcnbKeHoachDcDtl> findByDcnbKeHoachDcHdrIdAndId(Long hdrId, Long id);

    @Query(value ="SELECT SUM(d.duToanKphi) FROM DcnbKeHoachDcDtl d " +
            "WHERE d.dcnbKeHoachDcHdr.maDviCuc = ?1 AND d.dcnbKeHoachDcHdr.type = ?2 AND d.dcnbKeHoachDcHdr.loaiDc = ?3 AND d.dcnbKeHoachDcHdr.trangThai = ?4 " +
            "AND d.dcnbKeHoachDcHdr.ngayTao <= ?5")
    Long findByMaDviCucAndTypeAndLoaiDcTongCucChiCuc(String maDVi, String type, String loaiDieuChuyen, String trangThai,LocalDateTime thoigianTongHop);

    @Query(value ="SELECT SUM(d.duToanKphi) FROM DcnbKeHoachDcDtl d " +
            "LEFT JOIN DcnbKeHoachDcHdr h ON h.id = d.hdrId " +
            "WHERE h.maDviCuc = ?1 AND h.maCucNhan = ?2 AND h.type = ?3 AND h.loaiDc = ?4 AND h.trangThai = ?5 " +
            "AND h.ngayTao <= ?6")
    Long findByMaDviCucAndTypeAndLoaiDcTongCucCuc(String maDVi,String cucNhan, String type, String loaiDieuChuyen, String trangThai, LocalDateTime thoigianTongHop);


    List<DcnbKeHoachDcDtl> findByIdIn(List<Long> idList);
    @Query(value ="SELECT SUM(d.duToanKphi) FROM DcnbKeHoachDcDtl d " +
            "LEFT JOIN DcnbKeHoachDcHdr h ON h.id = d.hdrId " +
            "WHERE h.maDviCuc = ?1 AND h.maCucNhan = ?2 AND h.trangThai = ?3 AND h.loaiDc = ?4 AND h.type = ?5 " +
            "AND h.ngayTao <= ?6")
    Long findByMaDviCucAndCucNhan(String maDVi, String maCucNhan, String daduyetLdcc, String giua2CucDtnnKv, String dieuChuyen, LocalDateTime thoiGianTongHop);

    @Query(value ="SELECT distinct dtl FROM DcnbKeHoachDcDtl dtl left join DcnbKeHoachDcHdr hdr on hdr.id = dtl.hdrId " +
            "WHERE dtl.maChiCucNhan IN ?1 AND hdr.parentId = ?2 AND hdr.type = ?3")
    List<DcnbKeHoachDcDtl> findByChiCucNhan(List<String> maChiCucNhan, Long dcnbHdrId, String type);

    @Query(value ="SELECT distinct dtl FROM DcnbKeHoachDcDtl dtl left join DcnbKeHoachDcHdr hdr on hdr.id = dtl.hdrId " +
            "WHERE dtl.hdrId = ?1 AND hdr.type = ?2 ")
    List<DcnbKeHoachDcDtl> findByDcnbKeHoachDcHdrIdAndType(Long dcKeHoachDcHdrId,String type);
}