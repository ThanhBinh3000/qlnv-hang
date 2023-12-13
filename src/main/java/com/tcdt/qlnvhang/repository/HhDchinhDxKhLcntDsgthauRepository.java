package com.tcdt.qlnvhang.repository;


import com.tcdt.qlnvhang.table.HhDchinhDxKhLcntDsgthau;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HhDchinhDxKhLcntDsgthauRepository extends CrudRepository<HhDchinhDxKhLcntDsgthau, Long> {

    List<HhDchinhDxKhLcntDsgthau> findAllByIdDcDxDtl(Long idDcDxDtl);
    List<HhDchinhDxKhLcntDsgthau> findAllByIdDcDxDtlOrderByGoiThau(Long idDcDxDtl);
    List<HhDchinhDxKhLcntDsgthau> findAllByIdDcDxHdr(Long idDcDxHdr);
    List<HhDchinhDxKhLcntDsgthau> findAllByIdDcDxHdrOrderByGoiThau(Long idDcDxHdr);
    List<HhDchinhDxKhLcntDsgthau> findAllByIdDcDxDtlIn(List<Long> ids);

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
