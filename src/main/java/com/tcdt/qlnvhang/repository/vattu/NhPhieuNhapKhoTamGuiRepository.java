package com.tcdt.qlnvhang.repository.vattu;

import com.tcdt.qlnvhang.entities.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGui;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface NhPhieuNhapKhoTamGuiRepository extends BaseRepository<NhPhieuNhapKhoTamGui, Long>, NhPhieuNhapKhoTamGuiRepositoryCustom {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<NhPhieuNhapKhoTamGui> findFirstBySoPhieu(String soPhieu);

}
