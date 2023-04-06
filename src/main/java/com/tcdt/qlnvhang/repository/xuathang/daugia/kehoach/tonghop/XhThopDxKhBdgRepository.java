package com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.tonghop;

import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdg;
import com.tcdt.qlnvhang.request.xuathang.daugia.SearchXhThopDxKhBdg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface XhThopDxKhBdgRepository extends JpaRepository<XhThopDxKhBdg,Long> {

    @Transactional()
    @Modifying
    @Query(value = "UPDATE XH_THOP_DX_KH_BAN_DAU_GIA SET TRANG_THAI =:trangThai WHERE ID = :idThHdr", nativeQuery = true)
    void updateTrangThai(Long idThHdr, String trangThai);

    @Query("SELECT  DX from XhThopDxKhBdg DX WHERE 1 = 1 " +
            "AND (:#{#param.namKh} IS NULL OR DX.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR DX.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.cloaiVthh} IS NULL OR DX.cloaiVthh LIKE CONCAT(:#{#param.cloaiVthh},'%')) " +
            "AND (:#{#param.noiDungThop} IS NULL OR LOWER(DX.noiDungThop) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.noiDungThop}),'%'))) " +
            "AND (:#{#param.ngayThopTu} IS NULL OR DX.ngayThop >= :#{#param.ngayThopTu}) " +
            "AND (:#{#param.ngayThopDen} IS NULL OR DX.ngayThop <= :#{#param.ngayThopDen}) " +
            "AND (:#{#param.trangThai} IS NULL OR DX.trangThai = :#{#param.trangThai})")
    Page<XhThopDxKhBdg> searchPage(@Param("param") SearchXhThopDxKhBdg param, Pageable pageable);

    List<XhThopDxKhBdg> findAllByIdIn(List<Long> ids);

    Optional<XhThopDxKhBdg> findAllBySoQdPd(String soQdPd);

    @Transactional
    @Modifying
    void deleteAllByIdIn(List<Long> ids);
}
