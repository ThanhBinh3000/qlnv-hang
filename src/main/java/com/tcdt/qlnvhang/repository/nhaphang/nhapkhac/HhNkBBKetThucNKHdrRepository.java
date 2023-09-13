package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBBKetThucNKHdr;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkBBKetThucNKReq;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBBKetThucNKHdrDTO;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBBKetThucNKHdrListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HhNkBBKetThucNKHdrRepository extends JpaRepository<HhNkBBKetThucNKHdr, Long> {
    Optional<HhNkBBKetThucNKHdr> findFirstBySoBb(String soBb);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBBKetThucNKHdrDTO(" +
            "bbkt.id,qdgnv.id,qdgnv.soQd,qdgnv.nam,qdgnv.tgianNkMnhat,dtl.maNhaKho,dmdvnhakho.tenDvi, dtl.maDiemKho,dmdvdiemkho.tenDvi,dtl.maLoKho," +
            "dmdvlokho.tenDvi,dtl.maNganKho,dmdvngankho.tenDvi,hdr.loaiVthh,dmvt.ten, dtl.cloaiVthh, dmvt.ten,hdr.dvt," +
            "bbkt.soBb, bbkt.ngayKetThucNhap, bbktd.soPhieuNhapKho, bbktd.phieuNhapKhoId,bbktd.ngayNhap, bblm.soBienBan,bblm.id," +
            "bblm.soBienBan,bblm.id, bbkt.trangThai) " +
            "FROM HhQdGiaoNvuNhapHangKhacHdr qdgnv " +
            "LEFT JOIN HhQdPdNhapKhacHdr hdr ON hdr.id = qdgnv.idQdPdNk " +
            "LEFT JOIN HhQdPdNhapKhacDtl dtl ON hdr.id = dtl.idHdr " +
            "LEFT JOIN HhNkBBKetThucNKHdr bbkt On bbkt.qdPdNkId = qdgnv.id " +
            "and ((dtl.maLoKho is not null and bbkt.maLoKho = dtl.maLoKho and bbkt.maNganKho = dtl.maNganKho) or (dtl.maLoKho is null and  bbkt.maNganKho = dtl.maNganKho)) " +
            "LEFT JOIN HhNkBBKetThucNKDtl bbktd On bbktd.hdrId = bbkt.id " +
            "LEFT JOIN HhQdGiaoNvuNhapHangKhacHdr bblmh ON bblmh.idQdPdNk = qdgnv.id " +
            "LEFT JOIN BienBanLayMauKhac bblm ON bblmh.id = bblm.idQdGiaoNvNh " +
            "and ((bblm.maLoKho is not null and bblm.maNganKho = bbkt.maNganKho and bblm.maLoKho = bbkt.maLoKho) or (bblm.maLoKho is null and bblm.maNganKho = bbkt.maNganKho)) " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = dtl.cloaiVthh " +
            "LEFT JOIN QlnvDmDonvi dmdvnhakho On dmdvnhakho.maDvi = dtl.maNhaKho " +
            "LEFT JOIN QlnvDmDonvi dmdvdiemkho On dmdvdiemkho.maDvi = dtl.maDiemKho " +
            "LEFT JOIN QlnvDmDonvi dmdvlokho On dmdvlokho.maDvi = dtl.maLoKho " +
            "LEFT JOIN QlnvDmDonvi dmdvngankho On dmdvngankho.maDvi = dtl.maNganKho " +
            "WHERE 1 =1 " +
            "AND qdgnv.trangThai = '29'" +
            "AND ((:#{#param.maDvi} IS NULL OR dtl.maChiCuc LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND (:#{#param.soQdPdNk} IS NULL OR LOWER(qdgnv.soQd) LIKE CONCAT('%',LOWER(:#{#param.soQdPdNk}),'%')) " +
            "AND (:#{#param.soBb} IS NULL OR LOWER(bbkt.soBb) LIKE CONCAT('%',LOWER(:#{#param.soBb}),'%')) " +
            "AND (:#{#param.nam} IS NULL OR qdgnv.nam = :#{#param.nam}) " +
            "AND ((:#{#param.tuNgayKtnk}  IS NULL OR bbkt.ngayKetThucNhap >= :#{#param.tuNgayKtnk})" +
            "AND (:#{#param.denNgayKtnk}  IS NULL OR bbkt.ngayKetThucNhap <= :#{#param.denNgayKtnk}) ) " +
            "GROUP BY bbkt.id,qdgnv.id,qdgnv.soQd,qdgnv.nam,qdgnv.tgianNkMnhat,dtl.maNhaKho,dmdvnhakho.tenDvi, dtl.maDiemKho,dmdvdiemkho.tenDvi,dtl.maLoKho," +
            "dmdvlokho.tenDvi,dtl.maNganKho,dmdvngankho.tenDvi,hdr.loaiVthh,dmvt.ten, dtl.cloaiVthh, dmvt.ten,hdr.dvt, hdr.dvt ," +
            "bbkt.soBb, bbkt.ngayKetThucNhap, bbktd.soPhieuNhapKho, bbktd.phieuNhapKhoId,bbktd.ngayNhap, bblm.soBienBan,bblm.id," +
            "bblm.soBienBan,bblm.id, bbkt.trangThai, bbkt.trangThai "+
            "ORDER BY qdgnv.soQd DESC")
    Page<HhNkBBKetThucNKHdrDTO> searchPage(@Param("param") HhNkBBKetThucNKReq req, Pageable pageable);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBBKetThucNKHdrListDTO(" +
            "bbkt.id,qdgnv.id,bbkt.soBb) " +
            "FROM HhNkBBKetThucNKHdr bbkt " +
            "LEFT JOIN HhQdGiaoNvuNhapHangKhacHdr qdgnv On bbkt.qdPdNkId = qdgnv.id " +
            "WHERE 1 =1 " +
            "AND qdgnv.trangThai = '29'" +
            "AND ((:#{#param.qdPdNkId} IS NULL OR qdgnv.idQdPdNk = :#{#param.qdPdNkId})) " +
            "AND ((:#{#param.maDvi} IS NULL OR bbkt.maDvi LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND (:#{#param.nam} IS NULL OR qdgnv.nam = :#{#param.nam}) " +
            "ORDER BY bbkt.soBb desc, bbkt.nam desc")
    List<HhNkBBKetThucNKHdrListDTO> searchList(@Param("param") HhNkBBKetThucNKReq req);
}
