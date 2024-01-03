package com.tcdt.qlnvhang.repository;


import com.tcdt.qlnvhang.table.HhDchinhDxKhLcntDsgthau;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface HhDchinhDxKhLcntDsgthauRepository extends CrudRepository<HhDchinhDxKhLcntDsgthau, Long> {

    List<HhDchinhDxKhLcntDsgthau> findAllByIdDcDxDtl(Long idDcDxDtl);
    HhDchinhDxKhLcntDsgthau findFirstByIdDcDxDtlAndGoiThau(Long idDcDxDtl, String goiThau);
    List<HhDchinhDxKhLcntDsgthau> findAllByIdDcDxDtlOrderByGoiThau(Long idDcDxDtl);
    List<HhDchinhDxKhLcntDsgthau> findByIdQdPdHsmtOrderByGoiThauAsc(Long idQdPdHsmt);
    List<HhDchinhDxKhLcntDsgthau> findAllByIdQdPdHsmtIn(List<Long> idQdPdHsmt);
    List<HhDchinhDxKhLcntDsgthau> findAllByIdDcDxHdr(Long idDcDxHdr);
    List<HhDchinhDxKhLcntDsgthau> findAllByIdDcDxHdrOrderByGoiThau(Long idDcDxHdr);
    List<HhDchinhDxKhLcntDsgthau> findAllByIdDcDxDtlIn(List<Long> ids);
    @Transactional()
    @Modifying
    @Query(value = "UPDATE HH_DC_DX_LCNT_DSGTHAU SET TGIAN_BDAU_TCHUC = CASE WHEN :tgianBdauTchuc IS NULL THEN NULL ELSE :tgianBdauTchuc END," +
            " TGIAN_MTHAU = CASE WHEN :tgianMthau IS NULL THEN NULL ELSE :tgianMthau END, " +
            " TGIAN_DTHAU = CASE WHEN :tgianDthau IS NULL THEN NULL ELSE :tgianDthau END" +
            " WHERE ID_QD_PD_HSMT = TO_NUMBER(:idQdPdHsmt) ", nativeQuery = true)
    void updateThongTinQdPdHsmt(@Temporal Date tgianBdauTchuc,@Temporal Date tgianMthau,@Temporal Date tgianDthau, Long idQdPdHsmt);
    void deleteAllByIdDcDxDtl(Long idDcDxDtl);
    void deleteAllByIdDcDxHdr(Long idDcDxHdr);

    long countByIdDcDxDtl(Long idDcDxDtl);
    @Query(value = "SELECT dc.* FROM HH_DC_DX_LCNT_DSGTHAU dc" +
            " JOIN HH_DC_DX_LCNT_HDR hdr ON dc.ID_DC_DX_HDR = hdr.ID " +
            " WHERE 1 = 1 " +
            " AND (:cloaiVthh IS NULL OR dc.CLOAI_VTHH = :cloaiVthh)" +
            " AND (:loaiVthh IS NULL OR dc.LOAI_VTHH = :loaiVthh)" +
            " AND (:namKh IS NULL OR hdr.NAM_KHOACH = :namKh)" +
            " AND dc.TRANG_THAI_DT IN ('41', '36')"
            , nativeQuery = true)
    List<HhDchinhDxKhLcntDsgthau> danhSachGthauTruotVt (String cloaiVthh, String loaiVthh, Integer namKh);
}
