package com.tcdt.qlnvhang.repository.vattu.bienbanketthucnhapkho;

import com.tcdt.qlnvhang.entities.nhaphang.vattu.bienbanketthucnhapkho.NhBbKtNhapKhoVtCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface NhBbKtNhapKhoVtCtRepository extends BaseRepository<NhBbKtNhapKhoVtCt, Long> {
    List<NhBbKtNhapKhoVtCt> findByBbKtNhapKhoIdIn(Collection<Long> bbKtNkIds);

    @Transactional
    @Modifying
    void deleteByBbKtNhapKhoIdIn(Collection<Long> bbKtNkIds);
}
