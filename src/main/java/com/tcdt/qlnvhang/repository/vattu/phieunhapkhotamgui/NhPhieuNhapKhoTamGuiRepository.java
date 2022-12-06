package com.tcdt.qlnvhang.repository.vattu.phieunhapkhotamgui;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkhotamgui.NhPhieuNhapKhoTamGui;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface NhPhieuNhapKhoTamGuiRepository extends BaseRepository<NhPhieuNhapKhoTamGui, Long>, NhPhieuNhapKhoTamGuiRepositoryCustom {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

//    Optional<NhPhieuNhapKhoTamGui> findFirstBySoPhieu(String soPhieu);

    @Query(value = "select pp.* from NH_PHIEU_NHAP_KHO_TAM_GUI pp where pp.ID_QD_GIAO_NV_NH = :idDdiemGiaoNvNh and rownum =1", nativeQuery = true)
    NhPhieuNhapKhoTamGui findByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh);

}
