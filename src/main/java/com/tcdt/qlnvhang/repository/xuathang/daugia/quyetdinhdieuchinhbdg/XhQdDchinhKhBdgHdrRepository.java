package com.tcdt.qlnvhang.repository.xuathang.daugia.quyetdinhdieuchinhbdg;

import com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgHdr;
import com.tcdt.qlnvhang.request.xuathang.daugia.quyetdinhdieuchinh.XhQdDchinhKhBdgReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface XhQdDchinhKhBdgHdrRepository extends JpaRepository<XhQdDchinhKhBdgHdr, Long> {

    @Query("SELECT DISTINCT DX FROM XhQdDchinhKhBdgHdr DX " +
            "WHERE (:#{#param.dvql} IS NULL OR DX.maDvi LIKE CONCAT(:#{#param.dvql}, '%')) " +
            "AND (:#{#param.nam} IS NULL OR DX.nam = :#{#param.nam}) " +
            "AND (:#{#param.soQdDc} IS NULL OR DX.soQdDc = :#{#param.soQdDc}) " +
            "AND (:#{#param.trichYeu} IS NULL OR LOWER(DX.trichYeu) LIKE LOWER(CONCAT('%', :#{#param.trichYeu}, '%'))) " +
            "AND (:#{#param.ngayKyDcTu} IS NULL OR DX.ngayKyDc >= :#{#param.ngayKyDcTu}) " +
            "AND (:#{#param.ngayKyDcDen} IS NULL OR DX.ngayKyDc <= :#{#param.ngayKyDcDen}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR DX.loaiVthh = :#{#param.loaiVthh}) " +
            "AND (:#{#param.trangThai} IS NULL OR DX.trangThai = :#{#param.trangThai}) " +
            "ORDER BY DX.ngaySua DESC, DX.ngayTao DESC, DX.id DESC")
    Page<XhQdDchinhKhBdgHdr> searchPage(@Param("param") XhQdDchinhKhBdgReq param, Pageable pageable);

    boolean existsBySoQdDc(String soQdDc);

    boolean existsBySoQdDcAndIdNot(String soQdDc, Long id);

    List<XhQdDchinhKhBdgHdr> findByIdIn(List<Long> idList);

    List<XhQdDchinhKhBdgHdr> findAllByIdIn(List<Long> listId);

    long countByidQdPd(Long idQdPd);
}