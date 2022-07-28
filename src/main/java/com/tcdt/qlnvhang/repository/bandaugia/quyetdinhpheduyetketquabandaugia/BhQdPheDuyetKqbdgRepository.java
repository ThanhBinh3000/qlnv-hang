package com.tcdt.qlnvhang.repository.bandaugia.quyetdinhpheduyetketquabandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetketquabandaugia.BhQdPheDuyetKqbdg;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

public interface BhQdPheDuyetKqbdgRepository extends BaseRepository<BhQdPheDuyetKqbdg, Long>, BhQdPheDuyetKqbdgRepositoryCustom {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<BhQdPheDuyetKqbdg> findFirstBySoQuyetDinh(String soQuyetDinh);
}
