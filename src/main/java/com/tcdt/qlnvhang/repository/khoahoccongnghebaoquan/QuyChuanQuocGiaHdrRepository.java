package com.tcdt.qlnvhang.repository.khoahoccongnghebaoquan;

import com.tcdt.qlnvhang.entities.khcn.quychuankythuat.QuyChuanQuocGiaHdr;
import com.tcdt.qlnvhang.request.khoahoccongnghebaoquan.SearchQuyChuanQgReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuyChuanQuocGiaHdrRepository extends JpaRepository<QuyChuanQuocGiaHdr, Long> {

    @Query(value = "select c from QuyChuanQuocGiaHdr c WHERE 1=1 " +
            " AND (:#{#param.soVanBan} IS NULL OR LOWER(c.soVanBan) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soVanBan}),'%' ) ) ) " +
            " AND (:#{#param.soHieuQuyChuan} IS NULL OR LOWER(c.soHieuQuyChuan) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.soHieuQuyChuan}),'%' ) ) )" +
            " AND (:#{#param.loaiVthh}  IS NULL OR LOWER(c.loaiVthh) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.loaiVthh}),'%' ) ) ) " +
            " AND (:#{#param.cloaiVthh}  IS NULL OR LOWER(c.cloaiVthh) =:#{#param.cloaiVthh}) " +
            " AND (:#{#param.trichYeu}  IS NULL OR LOWER(c.trichYeu) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.trichYeu}),'%' ) ) ) " +
            " AND ((:#{#param.ngayKyTu}  IS NULL OR c.ngayKy >= :#{#param.ngayKyTu})" +
            " AND (:#{#param.ngayKyDen}  IS NULL OR c.ngayKy <= :#{#param.ngayKyDen}) ) " +
            " AND ((:#{#param.ngayHieuLucTu}  IS NULL OR c.ngayHieuLuc >= :#{#param.ngayHieuLucTu})" +
            " AND (:#{#param.ngayHieuLucDen}  IS NULL OR c.ngayHieuLuc <= :#{#param.ngayHieuLucDen}) ) " +
            " AND (:#{#param.trangThai} IS NULL OR LOWER(c.trangThai)=:#{#param.trangThai} )" +
            " AND (:#{#param.isMat} IS NULL OR c.isMat=:#{#param.isMat} )" +
            " AND (:#{#param.maBn} IS NULL OR c.maBn=:#{#param.maBn} )" +
            " AND (:#{#param.apDungTai}  IS NULL OR LOWER(c.apDungTai) LIKE LOWER(CONCAT(:#{#param.apDungTai},'%' ) ) ) "+
            " AND (:#{#param.maDvi} IS NULL OR LOWER(c.maDvi) LIKE LOWER(CONCAT(:#{#param.maDvi},'%')))  "
    )
    Page<QuyChuanQuocGiaHdr> search(@Param("param") SearchQuyChuanQgReq param, Pageable pageable);

    Optional<QuyChuanQuocGiaHdr> findAllBySoVanBan(String maDetai);

    List<QuyChuanQuocGiaHdr> findAllByIdIn(List<Long> ids);

    Optional<QuyChuanQuocGiaHdr> findAllByLoaiVthh(String loaiVthh);

}
