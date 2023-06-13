package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbDataLinkHdr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DcnbDataLinkHdrRepository extends JpaRepository<DcnbDataLinkHdr, Long> {
    @Query(value = "SELECT hdr FROM DcnbDataLinkHdr hdr " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd ON khdcd.id = hdr.keHoachDcDtlId "+
            "LEFT JOIN DcnbKeHoachDcHdr khdch ON khdch.id = hdr.keHoachDcHdrId " +
            "LEFT JOIN DcnbQuyetDinhDcCHdr qdch On qdch.id = hdr.qdCcId " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdch.id = qdcd.hdrId " +
            "WHERE 1 =1 "+
            "AND (khdch.maDvi = ?1 )"+
            "AND (khdch.id = ?2) " +
            "AND ((khdcd.maLoKho IS NULL OR khdcd.maLoKho = ?3) OR  (khdcd.maNganKho = ?4)) " +
            "ORDER BY khdch.soQdDc desc, khdch.nam desc")
    DcnbDataLinkHdr findDataLinkChiCuc(String maDvi, Long qDinhDccId, String maNganKho, String maLoKho);
    @Query(value = "SELECT hdr FROM DcnbDataLinkHdr hdr " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd ON khdcd.id = hdr.keHoachDcDtlParentId "+
            "LEFT JOIN DcnbKeHoachDcHdr khdch ON khdch.id = hdr.keHoachDcHdrParentId " +
            "LEFT JOIN DcnbQuyetDinhDcCHdr qdch On qdch.id = hdr.qdCcParentId " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdch.id = qdcd.hdrId " +
            "WHERE 1 =1 "+
            "AND (khdch.maDvi = ?1 )"+
            "AND (khdch.id = ?2) " +
            "AND ((khdcd.maLoKho IS NULL OR khdcd.maLoKho = ?3) OR  (khdcd.maNganKho = ?4)) " +
            "ORDER BY khdch.soQdDc desc, khdch.nam desc")
    DcnbDataLinkHdr findDataLinkCuc(String maDvi, Long qdDcId, String maNganKho, String maLoKho);
}
