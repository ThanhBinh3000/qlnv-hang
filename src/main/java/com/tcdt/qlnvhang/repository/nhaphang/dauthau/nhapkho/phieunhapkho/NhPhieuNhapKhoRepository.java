package com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.phieunhapkho;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuktracl.NhPhieuKtChatLuong;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKho;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface NhPhieuNhapKhoRepository extends BaseRepository<NhPhieuNhapKho, Long> {

    @Query(
            value = "SELECT * FROM NH_PHIEU_NHAP_KHO PNKLT " +
                    "WHERE (:soPhieu IS NULL OR PNKLT.SO_PHIEU_NHAP_KHO = :soPhieu )" +
                    "  AND (:nam IS NULL OR PNKLT.NAM = TO_NUMBER(:nam) ) " +
                    "  AND (:maDvi IS NULL OR PNKLT.MA_DVI = :maDvi ) " +
                    "  AND (:loaiVthh IS NULL OR PNKLT.LOAI_VTHH = :loaiVthh ) " +
                    "  AND (:cloaiVthh IS NULL OR PNKLT.CLOAI_VTHH = :cloaiVthh ) " +
                    "  AND (:maDiemKho IS NULL OR PNKLT.MA_DIEM_KHO = :maDiemKho ) " +
                    "  AND (:maNhaKho IS NULL OR PNKLT.MA_NHA_KHO = :maNhaKho ) " +
                    "  AND (:maNganKho IS NULL OR PNKLT.MA_NGAN_KHO = :maNganKho ) " +
                    "  AND (:maLoKho IS NULL OR PNKLT.MA_LO_KHO = :maLoKho ) " , nativeQuery = true)
    Page<NhPhieuNhapKho> selectPage(String soPhieu, Integer nam, String maDvi, String loaiVthh, String cloaiVthh, String maDiemKho, String maNhaKho, String maNganKho, String maLoKho, Pageable pageable);
    @Query(
            value = "SELECT * FROM NH_PHIEU_NHAP_KHO PNKLT " +
                    "WHERE (:trangThai IS NULL OR PNKLT.TRANG_THAI = :trangThai )" +
                    "  AND (:nam IS NULL OR PNKLT.NAM = TO_NUMBER(:nam) ) " +
                    "AND (:tuNgay IS NULL OR PNKLT.NGAY_TAO >=  TO_DATE(:tuNgay,'yyyy-MM-dd'))"+
                    "AND (:denNgay IS NULL OR PNKLT.NGAY_TAO <=  TO_DATE(:denNgay,'yyyy-MM-dd'))"+
                    "  AND (:maDvi IS NULL OR PNKLT.MA_DVI = :maDvi ) " +
                    "  AND (:loaiVthh IS NULL OR PNKLT.LOAI_VTHH = :loaiVthh ) " +
                    "  AND (:cloaiVthh IS NULL OR PNKLT.CLOAI_VTHH = :cloaiVthh ) " +
                    "  AND (:maDiemKho IS NULL OR PNKLT.MA_DIEM_KHO = :maDiemKho ) " +
                    "  AND (:maNhaKho IS NULL OR PNKLT.MA_NHA_KHO = :maNhaKho ) " +
                    "  AND (:maNganKho IS NULL OR PNKLT.MA_NGAN_KHO = :maNganKho ) " +
                    "  AND (:maLoKho IS NULL OR PNKLT.MA_LO_KHO = :maLoKho ) "
            , nativeQuery = true)
    Page<NhPhieuNhapKho> selectPageTheKho(String trangThai, Integer nam,String tuNgay, String denNgay, String maDvi, String loaiVthh, String cloaiVthh, String maDiemKho, String maNhaKho, String maNganKho, String maLoKho, Pageable pageable);

    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

//    Optional<NhPhieuNhapKho> findFirstBySoPhieu(String soPhieu);

    List<NhPhieuNhapKho> findAllByIdQdGiaoNvNh(Long idQdGiaoNvNh);

    List<NhPhieuNhapKho> findByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh);

    List<NhPhieuNhapKho> findBySoPhieuKtraCl(String soPhieuKtraCl);


}
