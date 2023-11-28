package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeCanHangDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbPhieuNhapKhoDtlRepository extends JpaRepository<DcnbPhieuNhapKhoDtl, Long> {
    List<DcnbPhieuNhapKhoDtl> findByHdrId(Long id);

    List<DcnbPhieuNhapKhoDtl> findByHdrIdIn(List<Long> listId);

    void deleteAllByHdrId(long hdrId);
    @Query(value = "SELECT dpxkd.* \n" +
            "FROM DCNB_KE_HOACH_DC_DTL dtl\n" +
            "JOIN DCNB_KE_HOACH_DC_HDR hdr ON hdr.id = dtl.HDR_ID\n" +
            "JOIN DCNB_QUYET_DINH_DC_C_DTL qdcd ON qdcd.DCNB_KE_HOACH_DC_HDR_ID = hdr.id\n" +
            "JOIN DCNB_QUYET_DINH_DC_C_HDR qdch ON qdch.ID = qdcd.HDR_ID\n" +
            "JOIN DCNB_PHIEU_NHAP_KHO_HDR dpxkh ON dpxkh.KE_HOACH_DC_DTL_ID = dtl.ID\n" +
            "JOIN DCNB_PHIEU_NHAP_KHO_DTL dpxkd ON dpxkd.HDR_ID  = dpxkh.ID\n" +
            "WHERE dpxkh.TRANG_THAI = '17' AND qdch.SO_QDINH = ?1\n" +
            "AND dtl.ID IN (SELECT dtlv.ID \n" +
            "FROM DCNB_KE_HOACH_DC_DTL dtlv\n" +
            "WHERE dtlv.PARENT_ID = (SELECT dtlvv.PARENT_ID \n" +
            "FROM DCNB_KE_HOACH_DC_DTL dtlvv\n" +
            "WHERE dtlvv.ID = ?2) )", nativeQuery = true)
    List<DcnbPhieuNhapKhoDtl> findBySoQuyetDinhAndKeHoachDtlId(String soQdinhCuc, Long id);
}
