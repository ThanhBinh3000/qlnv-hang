package com.tcdt.qlnvhang.repository.khoahoccongnghebaoquan;

import com.tcdt.qlnvhang.entities.khcn.quychuankythuat.QuyChuanQuocGiaDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuyChuanQuocGiaDtlRepository extends JpaRepository<QuyChuanQuocGiaDtl, Long> {

    List<QuyChuanQuocGiaDtl> findAllByIdHdr(Long ids);

    List<QuyChuanQuocGiaDtl> findAllByIdHdrIn(List<Long> ids);

    @Query(value = "select distinct dtl.cloai_vthh from KHCN_QUY_CHUAN_QG_DTL dtl, KHCN_QUY_CHUAN_QG_HDR hdr where dtl.id_hdr = hdr.id" +
            " and hdr.trang_thai_hl = :trangThaiHl" +
            " and ( :hdrId is null or dtl.id_hdr <> to_number(:hdrId))",
            nativeQuery = true)
    List<String> findAllCloaiHoatDong(String trangThaiHl, Long hdrId);

    @Query(value = "select  dtl.* from KHCN_QUY_CHUAN_QG_DTL dtl,KHCN_QUY_CHUAN_QG_HDR hdr where dtl.id_hdr = hdr.id  " +
            " and hdr.trang_thai = '29'" +
            " and (dtl.cloai_vthh = :cloaiVthh OR dtl.loai_vthh = :cloaiVthh)" +
            " and trunc(hdr.ngay_hieu_luc) < trunc(sysdate) order by THU_TU_HT asc ", nativeQuery = true)
    List<QuyChuanQuocGiaDtl> getAllQuyChuanByCloaiVthh(String cloaiVthh);


    @Query(value = "select distinct dtl from QuyChuanQuocGiaDtl dtl,QuyChuanQuocGiaHdr hdr where hdr.id = dtl.idHdr and hdr.id in (?2) " +
            " and (dtl.cloaiVthh = ?1 or dtl.loaiVthh = ?1 ) order by dtl.thuTuHt asc ")
    List<QuyChuanQuocGiaDtl> getAllQuyChuanByCloaiVthhApDung(String cloaiVthh, List<Long> idsHdr);
}
