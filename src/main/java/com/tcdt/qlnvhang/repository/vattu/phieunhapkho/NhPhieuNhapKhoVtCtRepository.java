package com.tcdt.qlnvhang.repository.vattu.phieunhapkho;

import com.tcdt.qlnvhang.entities.vattu.phieunhapkho.NhPhieuNhapKhoVtCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface NhPhieuNhapKhoVtCtRepository extends BaseRepository<NhPhieuNhapKhoVtCt, Long> {
    List<NhPhieuNhapKhoVtCt> findByPhieuNkIdIn(Collection<Long> phieuNkIds);

    @Transactional
    @Modifying
    void deleteByPhieuNkIdIn(Collection<Long> phieuNkIds);
}
