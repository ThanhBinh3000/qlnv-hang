package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBbGiaoNhanHdr;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkBbGiaoNhanHdrReq;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBbGiaoNhanHdrDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HhNkBbGiaoNhanHdrRepository extends JpaRepository<HhNkBbGiaoNhanHdr, Long> {

    @Query(value = "SELECT distinct c FROM HhNkBbGiaoNhanHdr c LEFT JOIN QlnvDmDonvi dvi ON dvi.maDvi = c.maDvi WHERE 1=1 " +
            "AND (:#{#param.soBb} IS NULL OR LOWER(c.soBb) LIKE CONCAT('%',LOWER(:#{#param.soBb}),'%')) " +
            "AND ((:#{#param.maDvi} IS NULL OR c.maDvi = :#{#param.maDvi}) " +
            "OR (:#{#param.maDvi} IS NULL OR dvi.parent.maDvi = :#{#param.maDvi}))" +
            "AND (:#{#param.nam} IS NULL OR c.nam = :#{#param.nam}) " +
            "AND (:#{#param.soQdPdNk} IS NULL OR LOWER(c.soQdPdNk) LIKE CONCAT('%',LOWER(:#{#param.soQdPdNk}),'%')) " +
            "ORDER BY c.soQdPdNk desc , c.nam desc, c.id desc")
    Page<HhNkBbGiaoNhanHdr> search(@Param("param") HhNkBbGiaoNhanHdrReq req, Pageable pageable);

    Optional<HhNkBbGiaoNhanHdr> findFirstBySoBb(String soBb);


    List<HhNkBbGiaoNhanHdr> findByIdIn(List<Long> ids);

    List<HhNkBbGiaoNhanHdr> findAllByIdIn(List<Long> idList);

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBbGiaoNhanHdrDTO(" +
            "bbgn.id,bbgn.ngayLap, qdgnv.id,qdgnv.soQd,qdgnv.nam,qdgnv.tgianNkMnhat,dtl.maNhaKho,dmdvnhakho.tenDvi, dtl.maDiemKho,dmdvdiemkho.tenDvi,dtl.maLoKho," +
            "dmdvlokho.tenDvi,dtl.maNganKho,dmdvngankho.tenDvi,hdr.loaiVthh,dmvt.ten, dtl.cloaiVthh, dmvt.loaiHang,hdr.tongSlNhap, hdr.dvt, " +
            "bbgn.soBb,bbktnk.soBb,bbktnk.id, bbktnk.ngayKetThucNhap,pnkh.id,pnkh.soPhieuNhapKho, pnkh.ngayLap, bbktnk.ngayKetThucNhap,bblm.soBienBan,bblm.id, bbgn.trangThai, bbgn.trangThai) " +
            "FROM HhQdGiaoNvuNhapHangKhacHdr qdgnv " +
            "LEFT JOIN HhQdPdNhapKhacHdr hdr ON hdr.id = qdgnv.idQdPdNk " +
            "LEFT JOIN HhQdPdNhapKhacDtl dtl ON hdr.id = dtl.idHdr " +
            "LEFT JOIN HhNkPhieuNhapKhoHdr pnkh On pnkh.qdGiaoNvId = qdgnv.id " +
            "and ((dtl.maLoKho is not null and dtl.maLoKho = pnkh.maLoKho and dtl.maNganKho = pnkh.maNganKho) or (dtl.maLoKho is null and dtl.maNganKho = pnkh.maNganKho)) " +
            "LEFT JOIN HhNkBbGiaoNhanHdr bbgn On bbgn.qdPdNkId = qdgnv.id " +
            "and ((dtl.maLoKho is not null and dtl.maLoKho = bbgn.maLoKho and dtl.maNganKho = bbgn.maNganKho) or (dtl.maLoKho is null and dtl.maNganKho = bbgn.maNganKho)) " +
            "LEFT JOIN HhNkBBKetThucNKHdr bbktnk On bbktnk.qdPdNkId = qdgnv.id and bbgn.idBbKtNhapKho =  bbktnk.id " +
            "LEFT JOIN HhQdGiaoNvuNhapHangKhacHdr bblmh ON bblmh.idQdPdNk = qdgnv.id " +
            "LEFT JOIN BienBanLayMauKhac bblm ON bblmh.id = bblm.idQdGiaoNvNh " +
            "and ((bblm.maLoKho is not null and bblm.maNganKho = bbgn.maNganKho and bblm.maLoKho = bbgn.maLoKho) or (bblm.maLoKho is null and bblm.maNganKho = bbgn.maNganKho)) " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = dtl.cloaiVthh " +
            "LEFT JOIN QlnvDmDonvi dmdvnhakho On dmdvnhakho.maDvi = dtl.maNhaKho " +
            "LEFT JOIN QlnvDmDonvi dmdvdiemkho On dmdvdiemkho.maDvi = dtl.maDiemKho " +
            "LEFT JOIN QlnvDmDonvi dmdvlokho On dmdvlokho.maDvi = dtl.maLoKho " +
            "LEFT JOIN QlnvDmDonvi dmdvngankho On dmdvngankho.maDvi = dtl.maNganKho " +
            "WHERE 1 =1 " +
            "AND qdgnv.trangThai = '29'" +
            "AND ((:#{#param.maDvi} IS NULL OR qdgnv.maDvi LIKE CONCAT('%',LOWER(:#{#param.maDvi}),'%')))" +
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) " +
            "AND (:#{#param.nam} IS NULL OR qdgnv.nam = :#{#param.nam}) " +
            "AND (:#{#param.soBb} IS NULL OR LOWER(bbgn.soBb) LIKE CONCAT('%',LOWER(:#{#param.soBb}),'%')) " +
            "AND (:#{#param.soQdPdNk} IS NULL OR LOWER(qdgnv.soQd) LIKE CONCAT('%',LOWER(:#{#param.soQdPdNk}),'%')) " +
            "AND ((:#{#param.tuNgayKtnk}  IS NULL OR bbgn.ngayKtNhap >= :#{#param.tuNgayKtnk})" +
            "AND (:#{#param.denNgayKtnk}  IS NULL OR bbgn.ngayKtNhap <= :#{#param.denNgayKtnk}) ) " +
            "AND ((:#{#param.tuNgayThoiHanNhap}  IS NULL OR qdgnv.tgianNkMnhat >= :#{#param.tuNgayThoiHanNhap})" +
            "AND (:#{#param.denNgayThoiHanNhap}  IS NULL OR qdgnv.tgianNkMnhat <= :#{#param.denNgayThoiHanNhap}) ) " +
            "GROUP BY bbgn.id,bbgn.ngayLap, qdgnv.id,qdgnv.soQd,qdgnv.nam,qdgnv.tgianNkMnhat,dtl.maNhaKho,dmdvnhakho.tenDvi, dtl.maDiemKho,dmdvdiemkho.tenDvi,dtl.maLoKho,dmdvlokho.tenDvi,dtl.maNganKho,dmdvngankho.tenDvi,hdr.loaiVthh,dmvt.ten, dtl.cloaiVthh, dmvt.loaiHang,hdr.tongSlNhap, hdr.dvt,bbgn.soBb,bbktnk.soBb,bbktnk.id, bbktnk.ngayKetThucNhap,pnkh.id,pnkh.soPhieuNhapKho, pnkh.ngayLap, bbktnk.ngayKetThucNhap,bblm.soBienBan,bblm.id, bbgn.trangThai, bbgn.trangThai " +
//            "dmdvlokho.tenDvi,dtl.maNganKho,dmdvngankho.tenDvi,hdr.loaiVthh,dmvt.ten, dtl.cloaiVthh, dmvt.loaiHang, qdgnv.dvt, qdgnv.dvt ," +
//            "bbgn.soBb,bbktnk.soBb,bbktnk.id, bbktnk.ngayKetThucNhap,pnkh.id,pnkh.soPhieuNhapKho, pnkh.ngayLap, bbktnk.ngayKetThucNhap,bblm.soBienBan,bblm.id, bbgn.trangThai, bbgn.trangThai "+
            "ORDER BY qdgnv.soQd DESC")
    Page<HhNkBbGiaoNhanHdrDTO> searchPage(@Param("param") HhNkBbGiaoNhanHdrReq req, Pageable pageable);

}
