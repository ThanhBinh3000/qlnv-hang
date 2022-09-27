package com.tcdt.qlnvhang.repository.nhaphangtheoptt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxKhMttThopHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface HhDxuatKhMttThopRepository extends JpaRepository<HhDxKhMttThopHdr ,Long> {

    @Query(value = "select * from HH_DX_KHMTT_THOP_HDR TH where (:namKh IS NULL OR TH.NAM_KHOACH = TO_NUMBER(:namKh)) " +
            "AND (:loaiVthh IS NULL OR TH.LOAI_VTHH = :loaiVthh) " +
            "AND (:cloaiVthh IS NULL OR TH.CLOAI_VTHH = :cloaiVthh) " +
            "AND (:noiDung IS NULL OR LOWER( TH.NOI_DUNG) LIKE LOWER(CONCAT(CONCAT('%',:noiDung),'%')))" +
            "AND (:ngayThopTu IS NULL OR TH.NGAY_THOP >=  TO_DATE(:ngayThopTu,'yyyy-MM-dd')) " +
            "AND (:ngayThopDen IS NULL OR TH.NGAY_THOP <= TO_DATE(:ngayThopDen,'yyyy-MM-dd'))" +
            "AND (:trangThai IS NULL OR TH.TRANG_THAI = :trangThai) " +
            "AND (:maDvi IS NULL OR LOWER(TH.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    Page<HhDxKhMttThopHdr> searchPage(Integer namKh, String loaiVthh, String cloaiVthh, String noiDung, String ngayThopTu, String ngayThopDen, String trangThai, String maDvi, Pageable pageable);

    List<HhDxKhMttThopHdr> findAllByIdIn(List<Long> ids);

    @Transactional
    @Modifying
    void deleteAllByIdIn(List<Long> ids);

}
