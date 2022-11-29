package com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia;

import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhThopDxKhBdg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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


    @Query(value = "select * from XH_THOP_DX_KH_BAN_DAU_GIA  TH" +
            " where (:namKh IS NULL OR TH.NAM_KH = TO_NUMBER(:namKh)) " +
            "AND (:loaiVthh IS NULL OR TH.LOAI_VTHH = :loaiVthh) " +
            "AND (:cloaiVthh IS NULL OR TH.CLOAI_VTHH = :cloaiVthh) " +
            "AND (:noiDungThop IS NULL OR LOWER( TH.NOI_DUNG_THOP) LIKE LOWER(CONCAT(CONCAT('%',:noiDungThop),'%')))" +
            "AND (:ngayThopTu IS NULL OR TH.NGAY_THOP >=  TO_DATE(:ngayThopTu,'yyyy-MM-dd')) " +
            "AND (:ngayThopDen IS NULL OR TH.NGAY_THOP <= TO_DATE(:ngayThopDen,'yyyy-MM-dd'))" +
            "AND (:trangThai IS NULL OR TH.TRANG_THAI = :trangThai) " +
            "AND (:maDvi IS NULL OR LOWER(TH.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    Page<XhThopDxKhBdg> searchPage(Integer namKh, String loaiVthh, String cloaiVthh, String noiDungThop, String ngayThopTu, String ngayThopDen, String trangThai, String maDvi, Pageable pageable);

    List<XhThopDxKhBdg> findAllByIdIn(List<Long> ids);

    Optional<XhThopDxKhBdg> findAllBySoQdPd(String soQdPd);

    @Transactional
    @Modifying
    void deleteAllByIdIn(List<Long> ids);
}
