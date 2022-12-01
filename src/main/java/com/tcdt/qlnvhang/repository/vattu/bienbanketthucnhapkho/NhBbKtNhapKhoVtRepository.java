package com.tcdt.qlnvhang.repository.vattu.bienbanketthucnhapkho;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbanketthucnhapkho.NhBbKtNhapKhoVt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface NhBbKtNhapKhoVtRepository extends BaseRepository<NhBbKtNhapKhoVt, Long>, NhBbKtNhapKhoVtRepositoryCustom {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<NhBbKtNhapKhoVt> findFirstBySoBienBan(String soBienBan);
}
