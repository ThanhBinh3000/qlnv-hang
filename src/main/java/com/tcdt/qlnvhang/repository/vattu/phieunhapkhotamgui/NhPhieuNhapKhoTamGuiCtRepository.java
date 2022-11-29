package com.tcdt.qlnvhang.repository.vattu.phieunhapkhotamgui;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface NhPhieuNhapKhoTamGuiCtRepository extends BaseRepository<NhPhieuNhapKhoTamGuiCt, Long> {
    List<NhPhieuNhapKhoTamGuiCt> findByPhieuNkTgIdIn(Collection<Long> phieuNkTgIds);

    List<NhPhieuNhapKhoTamGuiCt> findByPhieuNkTgId(Long phieuNkTgId);

    @Transactional
    @Modifying
    void deleteByPhieuNkTgIdIn(Collection<Long> phieuNkTgIds);


    @Transactional
    @Modifying
    void deleteAllByPhieuNkTgId(Long phieuNkTgId);
}
