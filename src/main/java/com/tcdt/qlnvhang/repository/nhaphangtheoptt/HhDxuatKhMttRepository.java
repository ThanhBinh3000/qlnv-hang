package com.tcdt.qlnvhang.repository.nhaphangtheoptt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttHdr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HhDxuatKhMttRepository extends JpaRepository<HhDxuatKhMttHdr, Long> {
    

    @Query(value = "select * from HH_DX_KHMTT_HDR MTT where (:namKh IS NULL OR MTT.NAM_KH = TO_NUMBER(:namKh)) " +
            "AND (:soDxuat IS NULL OR LOWER(MTT.SO_DXUAT) LIKE LOWER(CONCAT(CONCAT('%',:soDxuat),'%' ) ) )" +
            "AND (:ngayTaoTu IS NULL OR MTT.NGAY_TAO >=  TO_DATE(:ngayTaoTu,'yyyy-MM-dd')) " +
            "AND (:ngayTaoDen IS NULL OR MTT.NGAY_TAO <= TO_DATE(:ngayTaoDen,'yyyy-MM-dd'))" +
            "AND (:ngayDuyetTu IS NULL OR MTT.NGAY_PDUYET >=  TO_DATE(:ngayDuyetTu,'yyyy-MM-dd')) " +
            "AND (:ngayDuyetDen IS NULL OR MTT.NGAY_PDUYET <= TO_DATE(:ngayDuyetDen,'yyyy-MM-dd'))" +
            "AND (:loaiVthh IS NULL OR MTT.LOAI_VTHH = :loaiVthh) " +
            "AND (:trangThai IS NULL OR MTT.TRANG_THAI = :trangThai)" +
            "AND (:trangThaiTh IS NULL OR MTT.TRANG_THAI_TH = :trangThaiTh) " +
            "AND (:maDvi IS NULL OR LOWER(MTT.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    Page<HhDxuatKhMttHdr> searchPage(Integer namKh, String soDxuat, String ngayTaoTu, String ngayTaoDen,String ngayDuyetTu, String ngayDuyetDen, String loaiVthh, String trangThai, String trangThaiTh, String maDvi, Pageable pageable);


    Optional<HhDxuatKhMttHdr> findBySoDxuat(String soDxuat);

    List <HhDxuatKhMttHdr>findAllByIdIn(List<Long> listId);

}
