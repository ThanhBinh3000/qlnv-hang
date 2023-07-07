package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdrReq;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdrDTO;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdrListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhNkPhieuNhapKhoHdrRepository extends JpaRepository<HhNkPhieuNhapKhoHdr, Long> {
    @Query(value = "SELECT new com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdrListDTO(" +
            "pnk.id,pnk.soPhieuNhapKho,pnk.ngayLap) " +
            "FROM HhNkPhieuNhapKhoHdr pnk " +
            "LEFT JOIN HhQdGiaoNvuNhapHangKhacHdr qdgnv ON pnk.qdNhapHangId = qdgnv.id "+
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = pnk.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdgnv.trangThai = '29'" +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND ((:#{#param.maDvi} IS NULL OR pnk.maDvi LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND (:#{#param.nam} IS NULL OR qdgnv.nam = :#{#param.nam}) " +
            "ORDER BY pnk.soPhieuNhapKho desc, pnk.nam desc")
    List<HhNkPhieuNhapKhoHdrListDTO> searchList(@Param("param")HhNkPhieuNhapKhoHdrReq objReq);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdrListDTO(" +
            "pnk.id,pnk.soPhieuNhapKho,pnk.ngayLap) " +
            "FROM HhNkPhieuNhapKhoHdr pnk " +
            "LEFT JOIN HhQdGiaoNvuNhapHangKhacHdr qdgnv ON pnk.qdNhapHangId = qdgnv.id "+
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = pnk.cloaiVthh " +
            "WHERE 1 =1 " +
            "AND qdgnv.trangThai = '29'" +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND ((:#{#param.maDvi} IS NULL OR pnk.maDvi LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND (:#{#param.nam} IS NULL OR qdgnv.nam = :#{#param.nam}) " +
            "ORDER BY pnk.soPhieuNhapKho desc, pnk.nam desc")
    List<HhNkPhieuNhapKhoHdrListDTO> searchListChung(@Param("param")HhNkPhieuNhapKhoHdrReq objReq);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdrDTO(" +
            "pnk.id,qdgnv.id,qdgnv.soQd,qdgnv.ngayQd,qdgnv.nam,qdcd.tgianNkho,dtl.maNhaKho,dmdvnhakho.tenDvi, dtl.maDiemKho,dmdvdiemkho.tenDvi,dtl.maLoKho," +
            "dmdvlokho.tenDvi,dtl.maNganKho,dmdvngankho.tenDvi,pnk.soPhieuNhapKho, pnk.ngayLap,  bknvt.id, bknvt.soBangKe," +
            "bknvt.loaiVthh,dmvt.ten,dtl.cloaiVthh, dmvt.loaiHang,qdgnv.dvt, dtl.tongSlNhap, " +
            "pnk.idPhieuKtraCluong, pnk.soPhieuKtraCluong, pktcl.ngayGdinh, " +
            "pnk.trangThai, pnk.trangThai) " +
            "FROM HhQdGiaoNvuNhapHangKhacHdr qdgnv " +
            "LEFT JOIN HhQdPdNhapKhacHdr hdr ON hdr.id = qdgnv.idQdPdNk " +
            "LEFT JOIN HhQdPdNhapKhacDtl dtl ON hdr.id = dtl.idHdr " +
            "LEFT JOIN HhQdGiaoNvuNhapHangKhacDtl qdcd ON qdcd.idHdr =  qdgnv.id " +
            "LEFT JOIN HhNkPhieuNhapKhoHdr pnk On pnk.qdNhapHangId = qdgnv.id " +
            "LEFT JOIN HhNkPhieuKtcl pktcl On pktcl.id = pnk.idPhieuKtraCluong " +
            "LEFT JOIN HhNkBangKeNhapVTHdr bknvt On bknvt.phieuNhapKhoId = pnk.id " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = dtl.cloaiVthh " +
            "LEFT JOIN QlnvDmDonvi dmdvnhakho On dmdvnhakho.maDvi = dtl.maNhaKho " +
            "LEFT JOIN QlnvDmDonvi dmdvdiemkho On dmdvnhakho.maDvi = dtl.maDiemKho " +
            "LEFT JOIN QlnvDmDonvi dmdvlokho On dmdvnhakho.maDvi = dtl.maLoKho " +
            "LEFT JOIN QlnvDmDonvi dmdvngankho On dmdvnhakho.maDvi = dtl.maNganKho " +
            "WHERE 1 =1 " +
            "AND qdgnv.trangThai = '29'" +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND ((:#{#param.maDvi} IS NULL OR dtl.maChiCuc LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND (:#{#param.nam} IS NULL OR qdgnv.nam = :#{#param.nam}) " +
            "ORDER BY pnk.soPhieuNhapKho desc, pnk.nam desc")
    Page<HhNkPhieuNhapKhoHdrDTO> searchPage(@Param("param") HhNkPhieuNhapKhoHdrReq req, Pageable pageable);
}
