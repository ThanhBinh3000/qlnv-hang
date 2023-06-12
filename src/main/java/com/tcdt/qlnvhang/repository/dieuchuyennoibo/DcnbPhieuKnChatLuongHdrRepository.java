package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchPhieuKnChatLuong;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuKnChatLuongHdrDTO;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuKnChatLuongHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DcnbPhieuKnChatLuongHdrRepository extends JpaRepository<DcnbPhieuKnChatLuongHdr, Long> {

    @Query(value = "SELECT new com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuKnChatLuongHdrDTO(" +
            "pkncl.id,bblm.id,qdc.id,qdc.soQdinh,qdc.nam,khdcd.thoiGianDkDc,khdcd.maDiemKho,khdcd.tenDiemKho,khdcd.maLoKho," +
            "khdcd.tenLoKho, khdcd.thayDoiThuKho,pkncl.soPhieu,pkncl.ngayKiem,bblm.soBbLayMau,bblm.ngayLayMau,bblm.soBbTinhKho,bblm.ngayXuatDocKho," +
            "bblm.soBbHaoDoi,pkncl.trangThai,pkncl.trangThai,khdcd.loaiVthh,khdcd.tenLoaiVthh,khdcd.cloaiVthh,khdcd.tenCloaiVthh,khdcd.maNhaKho,khdcd.tenNhaKho,khdcd.thuKho,khdcd.maNganKho,khdcd.tenNganKho, qdc.ngayHieuLuc,khdcd.donViTinh,khdcd.tenDonViTinh) FROM DcnbQuyetDinhDcCHdr qdc " +
            "LEFT JOIN DcnbBienBanLayMauHdr bblm On bblm.qDinhDccId = qdc.id " +
            "LEFT JOIN DcnbPhieuKnChatLuongHdr pkncl On pkncl.qdDcId = qdc.id " +
            "LEFT JOIN DcnbQuyetDinhDcCDtl qdcd On qdcd.hdrId = qdc.id " +
            "LEFT JOIN DcnbKeHoachDcHdr khdch On khdch.id = qdcd.keHoachDcHdrId " +
            "LEFT JOIN DcnbKeHoachDcDtl khdcd On khdcd.hdrId = khdch.id " +
            "WHERE 1 =1 "+
            "AND qdc.trangThai = '29' AND qdc.loaiDc = :#{#param.loaiDc} AND (:#{#param.type} IS NULL OR pkncl.type = :#{#param.type}) "+
            "AND ((:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}) OR (:#{#param.maDvi} IS NULL OR qdc.maDvi = :#{#param.maDvi}))"+
            "AND (:#{#param.nam} IS NULL OR qdc.nam = :#{#param.nam}) " +
            "AND (:#{#param.soPhieu} IS NULL OR LOWER(pkncl.soPhieu) LIKE CONCAT('%',LOWER(:#{#param.soPhieu}),'%')) " +
            "AND (:#{#param.soQdinhDcc} IS NULL OR LOWER(pkncl.soQdinhDc) LIKE CONCAT('%',LOWER(:#{#param.soQdinhDcc}),'%')) " +
            "AND ((:#{#param.tuNgay}  IS NULL OR pkncl.ngayKiem >= :#{#param.tuNgay})" +
            "AND (:#{#param.denNgay}  IS NULL OR pkncl.ngayKiem <= :#{#param.denNgay}) ) " +
            "ORDER BY pkncl.soQdinhDc desc, pkncl.nam desc")
    Page<DcnbPhieuKnChatLuongHdrDTO> searchPage(@Param("param") SearchPhieuKnChatLuong req, Pageable pageable);

    Optional<DcnbPhieuKnChatLuongHdr> findFirstBySoPhieu(String soPhieu);

    List<DcnbPhieuKnChatLuongHdr> findByIdIn(List<Long> ids);

    List<DcnbPhieuKnChatLuongHdr> findAllByIdIn(List<Long> idList);
}
