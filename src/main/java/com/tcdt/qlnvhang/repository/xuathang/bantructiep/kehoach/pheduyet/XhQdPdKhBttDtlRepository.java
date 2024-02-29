package com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDtl;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin.SearchXhTcTtinBttReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdPdKhBttDtlRepository extends JpaRepository<XhQdPdKhBttDtl, Long> {

    @Query("SELECT DISTINCT dtl FROM XhQdPdKhBttDtl dtl" +
            " LEFT JOIN XhQdPdKhBttHdr hdr ON hdr.id = dtl.idHdr" +
            " LEFT JOIN XhQdPdKhBttDvi dvi on dtl.id = dvi.idDtl" +
            " LEFT JOIN XhHopDongBttHdr hd on dtl.id = hd.idChaoGia" +
            " LEFT JOIN XhTcTtinBtt tt on dtl.id = tt.idQdPdDtl WHERE" +
            " (:#{#param.dvql} IS NULL OR dtl.maDvi LIKE CONCAT(:#{#param.dvql}, '%'))" +
            " AND (:#{#param.namKh} IS NULL OR dtl.namKh = :#{#param.namKh})" +
            " AND (:#{#param.soDxuat} IS NULL OR LOWER(dtl.soDxuat) LIKE LOWER(CONCAT(:#{#param.soDxuat},'%'))) " +
            " AND (:#{#param.soQdPd} IS NULL OR LOWER(dtl.soQdPd) LIKE LOWER(CONCAT(:#{#param.soQdPd},'%'))) " +
            " AND (:#{#param.soQdDc} IS NULL OR LOWER(dtl.soQdDc) LIKE LOWER(CONCAT(:#{#param.soQdDc},'%'))) " +
            " AND (:#{#param.soQdKq} IS NULL OR LOWER(dtl.soQdKq) LIKE LOWER(CONCAT(:#{#param.soQdKq},'%'))) " +
            " AND (:#{#param.soHopDong} IS NULL OR LOWER(hd.soHopDong) LIKE LOWER(CONCAT(:#{#param.soHopDong},'%'))) " +
            " AND (:#{#param.tenHopDong} IS NULL OR LOWER(hd.tenHopDong) LIKE CONCAT(:#{#param.tenHopDong}, '%'))" +
            " AND (:#{#param.ngayCgiaTu} IS NULL OR dtl.ngayNhanCgia >= :#{#param.ngayCgiaTu})" +
            " AND (:#{#param.ngayCgiaDen} IS NULL OR dtl.ngayNhanCgia <= :#{#param.ngayCgiaDen})" +
            " AND (:#{#param.ngayTaoTu} IS NULL OR hdr.ngayTao >= :#{#param.ngayTaoTu})" +
            " AND (:#{#param.ngayTaoDen} IS NULL OR hdr.ngayTao <= :#{#param.ngayTaoDen})" +
            " AND (:#{#param.ngayDuyetTu} IS NULL OR hdr.ngayPduyet >= :#{#param.ngayDuyetTu})" +
            " AND (:#{#param.ngayDuyetDen} IS NULL OR hdr.ngayPduyet <= :#{#param.ngayDuyetDen})" +
            " AND (:#{#param.tochucCanhan} IS NULL OR LOWER(tt.tochucCanhan) LIKE LOWER(CONCAT('%', :#{#param.tochucCanhan}, '%')))" +
            " AND (:#{#param.trangThai} IS NULL OR dtl.trangThai = :#{#param.trangThai})" +
            " AND (:#{#param.trangThaiHdr} IS NULL OR hdr.trangThai = :#{#param.trangThaiHdr})" +
            " AND (:#{#param.lastest} IS NULL OR LOWER(hdr.lastest) LIKE LOWER(CONCAT(:#{#param.lastest},'%'))) " +
            " AND (:#{#param.typeHopDong} IS NULL OR LOWER(hdr.typeHopDong) LIKE LOWER(CONCAT(:#{#param.typeHopDong},'%'))) " +
            " AND (:#{#param.loaiVthh} IS NULL OR LOWER(dtl.loaiVthh) LIKE CONCAT(:#{#param.loaiVthh}, '%'))" +
            " AND (:#{#param.pthucBanTrucTiep == null || #param.pthucBanTrucTiep.isEmpty()} = TRUE OR dtl.pthucBanTrucTiep IN :#{#param.pthucBanTrucTiep})" +
            " AND (:#{#param.maChiCuc} IS NULL OR dvi.maDvi LIKE CONCAT(:#{#param.maChiCuc}, '%')) " +
            " AND (:#{#param.maCuc} IS NULL OR dtl.maDvi LIKE CONCAT(:#{#param.maCuc}, '%')) " +
            " ORDER BY dtl.namKh DESC, dtl.ngayNhanCgia DESC, dtl.id DESC")
    Page<XhQdPdKhBttDtl> searchPage(@Param("param") SearchXhTcTtinBttReq param, Pageable pageable);

    void deleteAllByIdHdr(Long idHdr);

    List<XhQdPdKhBttDtl> findAllByIdHdr(Long idHdr);

    List<XhQdPdKhBttDtl> findByIdHdrIn(List<Long> listId);

    List<XhQdPdKhBttDtl> findByIdIn(List<Long> idDtlList);
}