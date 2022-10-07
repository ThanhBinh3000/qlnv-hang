package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttHdr;
import com.tcdt.qlnvhang.util.Contains;
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
public interface HhDxuatKhMttRepository extends JpaRepository<HhDxuatKhMttHdr, Long> {
    

    @Query(value = "select * from HH_DX_KHMTT_HDR MTT where (:namKh IS NULL OR MTT.NAM_KH = TO_NUMBER(:namKh)) " +
            "AND (:soDxuat IS NULL OR LOWER(MTT.SO_DXUAT) LIKE LOWER(CONCAT(CONCAT('%',:soDxuat),'%' ) ) )" +
            "AND (:ngayTaoTu IS NULL OR MTT.NGAY_TAO >=  TO_DATE(:ngayTaoTu,'yyyy-MM-dd')) " +
            "AND (:ngayTaoDen IS NULL OR MTT.NGAY_TAO <= TO_DATE(:ngayTaoDen,'yyyy-MM-dd'))" +
            "AND (:ngayDuyetTu IS NULL OR MTT.NGAY_PDUYET >=  TO_DATE(:ngayDuyetTu,'yyyy-MM-dd')) " +
            "AND (:ngayDuyetDen IS NULL OR MTT.NGAY_PDUYET <= TO_DATE(:ngayDuyetDen,'yyyy-MM-dd'))" +
            "AND (:trichYeu IS NULL OR LOWER(MTT.TRICH_YEU) LIKE LOWER(CONCAT(CONCAT('%',:trichYeu),'%' ) ) )" +
            "AND (:loaiVthh IS NULL OR MTT.LOAI_VTHH = :loaiVthh) " +
            "AND (:trangThai IS NULL OR MTT.TRANG_THAI = :trangThai)" +
            "AND (:trangThaiTh IS NULL OR MTT.TRANG_THAI_TH = :trangThaiTh) " +
            "AND (:maDvi IS NULL OR LOWER(MTT.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    Page<HhDxuatKhMttHdr> searchPage(Integer namKh, String soDxuat, String ngayTaoTu, String ngayTaoDen,String ngayDuyetTu, String ngayDuyetDen, String trichYeu, String loaiVthh, String trangThai, String trangThaiTh, String maDvi, Pageable pageable);


    Optional<HhDxuatKhMttHdr> findBySoDxuat(String soDxuat);

    List <HhDxuatKhMttHdr>findAllByIdIn(List<Long> listId);

    @Query(value = "select * from HH_DX_KHMTT_HDR MTT where (:namKh IS NULL OR MTT.NAM_KH = TO_NUMBER(:namKh)) " +
            " AND (:loaiVthh IS NULL OR MTT.LOAI_VTHH = :loaiVthh) " +
            " AND (:cloaiVthh IS NULL OR MTT.CLOAI_VTHH = :cloaiVthh) " +
            " AND MTT.TRANG_THAI = '"+ Contains.DADUYET_LDC+"'" +
            " AND MTT.TRANG_THAI_TH = '"+ Contains.CHUATONGHOP+"'" +
            " AND (:maDvi IS NULL OR LOWER(MTT.MA_DVI) LIKE LOWER(CONCAT(:maDvi,'%')))  "
            ,nativeQuery = true)
    List<HhDxuatKhMttHdr> listTongHop(Integer namKh, String loaiVthh, String cloaiVthh, String maDvi);

    @Transactional()
    @Modifying
    @Query(value = "UPDATE HH_DX_KHMTT_HDR SET TRANG_THAI_TH=:trangThaiTh WHERE SO_DXUAT IN :soDxuatList", nativeQuery = true)
    void updateTongHop(List<String> soDxuatList, String trangThaiTh);

    List<HhDxuatKhMttHdr> findByIdIn(List<Long> id);

    @Transactional()
    @Modifying
    @Query(value = "UPDATE HH_DX_KHMTT_HDR SET SO_QD_PDUYET =:soQdPd WHERE ID = :id", nativeQuery = true)
    void updateSoQdPduyet(Long id, String soQdPd);
}
