package com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface NhPhieuNhapKhoCtRepository extends BaseRepository<NhPhieuNhapKhoCt, Long> {
    List<NhPhieuNhapKhoCt> findAllByPhieuNkId(Long phieuNhapKhoId);

    @Transactional
    @Modifying
    void deleteByPhieuNkIdIn(Collection<Long> phieuNhapKhoIds);
}
