package com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.tonghop;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttHdr;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.tonghop.SearchXhThopDxKhBtt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface XhThopDxKhBttRepository extends JpaRepository<XhThopDxKhBttHdr, Long> {

    @Query("SELECT distinct c FROM XhThopDxKhBttHdr c left join c.children h WHERE 1=1 " +
            "AND (:#{#param.dvql} IS NULL OR c.maDvi LIKE CONCAT(:#{#param.dvql},'%')) " +
            "AND (:#{#param.namKh} IS NULL OR c.namKh = :#{#param.namKh}) " +
            "AND (:#{#param.loaiVthh} IS NULL OR c.loaiVthh LIKE CONCAT(:#{#param.loaiVthh},'%')) " +
            "AND (:#{#param.cloaiVthh} IS NULL OR c.cloaiVthh LIKE CONCAT(:#{#param.cloaiVthh},'%')) " +
            "AND (:#{#param.noiDungThop} IS NULL OR LOWER(c.noiDungThop) LIKE LOWER(CONCAT(CONCAT('%',:#{#param.noiDungThop}),'%'))) " +
            "AND ((:#{#param.ngayThopTu}  IS NULL OR c.ngayThop >= :#{#param.ngayThopTu})" +
            "AND  (:#{#param.ngayThopDen}  IS NULL OR c.ngayThop <= :#{#param.ngayThopDen})) " +
            "AND (:#{#param.trangThai} IS NULL OR c.trangThai = :#{#param.trangThai}) " +
            "ORDER BY c.ngaySua desc , c.ngayTao desc, c.id desc")
    Page<XhThopDxKhBttHdr> searchPage(@Param("param") SearchXhThopDxKhBtt param, Pageable pageable);

    List<XhThopDxKhBttHdr> findByIdIn(List<Long> ids);

    List<XhThopDxKhBttHdr> findAllByIdIn(List<Long> ids);

    @Transactional()
    @Modifying
    @Query(value = "UPDATE XH_THOP_DX_KH_BTT_HDR SET TRANG_THAI =:trangThai WHERE ID = :idThHdr", nativeQuery = true)
    void updateTrangThai(Long idThHdr, String trangThai);
}
