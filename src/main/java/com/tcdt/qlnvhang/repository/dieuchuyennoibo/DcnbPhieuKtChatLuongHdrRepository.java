package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbBienBanLayMau;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchPhieuKtChatLuong;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuKtChatLuongHdrDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuKtChatLuongHdr;
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
public interface DcnbPhieuKtChatLuongHdrRepository extends JpaRepository<DcnbPhieuKtChatLuongHdr, Long> {

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuKtChatLuongHdrDTO(" +
            "pktcl.id,bbntbq.id,qdc.id,qdc.soQdinh,bbntbq.soBban,khdcd.thoiGianDkDc,qdc.nam,pktcl.nhanXetKetLuan,khdcd.maNhaKho," +
            "khdcd.tenNhaKho,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho,khdcd.maNganKho,khdcd.tenNganKho, khdcd.thayDoiThuKho,pktcl.soPhieu,pktcl.ngayKiem,pktcl.nhanXetKetLuan,pnk.soPhieuNhapKho, pnk.ngayLap, " +
            "khdcd.maNhaKhoNhan,khdcd.tenNhaKhoNhan,khdcd.maDiemKhoNhan,khdcd.tenDiemKhoNhan,khdcd.maLoKhoNhan," +
            "khdcd.tenLoKhoNhan,khdcd.maNganKhoNhan,khdcd.tenNganKhoNhan,"+
            "pktcl.trangThai,pktcl.trangThai) " +
            "FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbDataLinkHdr dtlh On dtlh.qdCcParentId = qdc.id " +
            "LEFT JOIN DcnbPhieuKtChatLuongHdr pktcl On pktcl.qdDcId = qdc.id " +
            "LEFT JOIN DcnbBBNTBQHdr bbntbq On bbntbq.qdDcCucId = qdc.id " +
            "LEFT JOIN DcnbPhieuNhapKhoHdr pnk On pnk.qdDcCucId = qdc.id " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = dtlh.qdCcId " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "LEFT JOIN QlnvDmVattu dmvt On dmvt.ma = khdcd.cloaiVthh " +
            "WHERE 1 =1 "+
            "AND qdc.trangThai = '29' AND qdc.loaiDc = :#{#param.loaiDc} "+
            "AND (dmvt.loaiHang in :#{#param.dsLoaiHang} ) "+
            "AND ((:#{#param.loaiQdinh} IS NULL OR qdc.loaiQdinh = :#{#param.loaiQdinh})) "+
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}) OR (:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))"+
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.soPhieu} IS NULL OR LOWER(pktcl.soPhieu) LIKE CONCAT('%',LOWER(:#{#param.soPhieu}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(pktcl.soQdinhDc) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR pktcl.ngayKiem >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR pktcl.ngayKiem <= :#{#param.denNgay}) ) " +
            "ORDER BY pktcl.soQdinhDc desc, pktcl.nam desc")
    Page<DcnbPhieuKtChatLuongHdrDTO> search(@Param("param")SearchPhieuKtChatLuong req, Pageable pageable);

    Optional<DcnbPhieuKtChatLuongHdr> findFirstBySoPhieu(String soPhieu);

    List<DcnbPhieuKtChatLuongHdr> findByIdIn(List<Long> ids);

    List<DcnbPhieuKtChatLuongHdr> findAllByIdIn(List<Long> idList);
}
