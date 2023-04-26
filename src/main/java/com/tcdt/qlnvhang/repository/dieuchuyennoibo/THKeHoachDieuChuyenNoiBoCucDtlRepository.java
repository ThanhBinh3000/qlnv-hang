package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.TongHopKeHoachDieuChuyen.THKeHoachDieuChuyenNoiBoCucDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface THKeHoachDieuChuyenNoiBoCucDtlRepository extends JpaRepository<THKeHoachDieuChuyenNoiBoCucDtl, Long> {
    List<THKeHoachDieuChuyenNoiBoCucDtl> findByHdrId(Long hdrId);

    List<THKeHoachDieuChuyenNoiBoCucDtl> findAllByHdrIdIn(List<Long> ids);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM DCNB_TH_KE_HOACH_DCC_NBC_DTL d WHERE d.HDR.ID= ?1")
    void deleteByHdrId(Long id);
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE DCNB_TH_KE_HOACH_DCC_NBC_DTL SET DA_XDINH_DIEM_NHAP=1 WHERE DCNB_KE_HOACH_DC_DTL_ID = ?1")
    void  updateByDcKeHoachDcDtlId(Long id);
}
