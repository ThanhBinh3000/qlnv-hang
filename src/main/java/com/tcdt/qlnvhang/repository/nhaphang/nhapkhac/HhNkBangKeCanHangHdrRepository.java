package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBangKeCanHangHdr;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.SearchBangKeCanHang;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBangKeCanHangHdrDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HhNkBangKeCanHangHdrRepository extends JpaRepository<HhNkBangKeCanHangHdr, Long> {
    @Query(value = "SELECT new com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBangKeCanHangHdrDTO(" +
            "bkch.id,qdgnv.id,pnk.id,qdgnv.soQd,qdgnv.nam,qdgnv.tgianNkMnhat, dtl.maDiemKho,dmdvdiemkho.tenDvi,dtl.maLoKho," +
            "dmdvlokho.tenDvi,bkch.soBangKe,pnk.soPhieuNhapKho, pnk.ngayLap ,bkch.trangThai,bkch.trangThai,hdr.loaiVthh," +
            "dmvt.ten,dtl.cloaiVthh,dmvt.loaiHang,dtl.maNhaKho,dmdvnhakho.tenDvi,hdr.dvt," +
            "dtl.maNganKho,dmdvngankho.tenDvi) " +
            "FROM HhQdGiaoNvuNhapHangKhacHdr qdgnv " +
            "LEFT JOIN HhQdPdNhapKhacHdr hdr ON hdr.id = qdgnv.idQdPdNk " +
            "LEFT JOIN HhQdPdNhapKhacDtl dtl ON hdr.id = dtl.idHdr " +
            "LEFT JOIN HhNkPhieuNhapKhoHdr pnk On pnk.qdGiaoNvId = qdgnv.id " +
            "and ((dtl.maLoKho is not null and pnk.maLoKho = dtl.maLoKho and pnk.maNganKho = dtl.maNganKho) or (dtl.maLoKho is null and pnk.maNganKho = dtl.maNganKho)) " +
            "LEFT JOIN HhNkPhieuKtcl pktcl On pktcl.id = pnk.idPhieuKtraCluong " +
            "LEFT JOIN HhNkBangKeCanHangHdr bkch ON bkch.idQdPdNk = qdgnv.id  " +
            "and ((dtl.maLoKho is not null and bkch.maLoKho = dtl.maLoKho and bkch.maNganKho = dtl.maNganKho) or (dtl.maLoKho is null and bkch.maNganKho = dtl.maNganKho)) " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = dtl.cloaiVthh " +
            "LEFT JOIN QlnvDmDonvi dmdvnhakho On dmdvnhakho.maDvi = dtl.maNhaKho " +
            "LEFT JOIN QlnvDmDonvi dmdvdiemkho On dmdvdiemkho.maDvi = dtl.maDiemKho " +
            "LEFT JOIN QlnvDmDonvi dmdvlokho On dmdvlokho.maDvi = dtl.maLoKho " +
            "LEFT JOIN QlnvDmDonvi dmdvngankho On dmdvngankho.maDvi = dtl.maNganKho " +
            "WHERE 1 =1 " +
            "AND qdgnv.trangThai = '29' " +
            "AND (:#{#param.loaiVthh} IS NULL OR dmvt.ma LIKE CONCAT('',LOWER(:#{#param.loaiVthh}),'%')) " +
            "AND ((:#{#param.maDvi} IS NULL OR dtl.maChiCuc LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND (:#{#param.nam} IS NULL OR qdgnv.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBangKe} IS NULL OR LOWER(bkch.soBangKe) LIKE CONCAT('%',LOWER(:#{#param.soBangKe}),'%')) " +
            "AND (:#{#param.soQdPdNk} IS NULL OR LOWER(qdgnv.soQd) LIKE CONCAT('%',LOWER(:#{#param.soQdPdNk}),'%')) " +
            "AND (:#{#param.maLoKho} IS NULL OR bkch.maLoKho = :#{#param.maLoKho}) " +
            "AND (:#{#param.maNganKho} IS NULL OR bkch.maNganKho = :#{#param.maNganKho}) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR pnk.ngayLap >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR pnk.ngayLap <= :#{#param.denNgay}) ) " +
            "GROUP BY bkch.id,qdgnv.id,pnk.id,qdgnv.soQd,qdgnv.nam,qdgnv.tgianNkMnhat, dtl.maDiemKho,dmdvdiemkho.tenDvi,dtl.maLoKho," +
            "dmdvlokho.tenDvi,bkch.soBangKe,pnk.soPhieuNhapKho, pnk.ngayLap ,bkch.trangThai,bkch.trangThai,hdr.loaiVthh," +
            "dmvt.ten,dtl.cloaiVthh,dmvt.loaiHang,dtl.maNhaKho,dmdvnhakho.tenDvi,hdr.dvt,hdr.dvt," +
            "dtl.maNganKho,dmdvngankho.tenDvi "+
            "ORDER BY qdgnv.soQd DESC")
    Page<HhNkBangKeCanHangHdrDTO> searchPage(@Param("param") SearchBangKeCanHang req, Pageable pageable);

    Optional<HhNkBangKeCanHangHdr> findFirstBySoBangKe(String soBangKe);


    List<HhNkBangKeCanHangHdr> findByIdIn(List<Long> ids);

    List<HhNkBangKeCanHangHdr> findAllByIdIn(List<Long> idList);

}
