package com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.phieunhapkho;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoCt1;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface NhPhieuNhapKhoCt1Repository extends BaseRepository<NhPhieuNhapKhoCt1, Long> {

    List<NhPhieuNhapKhoCt1> findByPhieuNkIdIn(Collection<Long> phieuNkIds);

    @Transactional
    @Modifying
    int deleteByPhieuNkIdIn(Collection<Long> phieuNkIds);
}
