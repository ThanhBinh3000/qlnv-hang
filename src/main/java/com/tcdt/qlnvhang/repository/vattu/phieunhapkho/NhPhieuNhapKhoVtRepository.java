package com.tcdt.qlnvhang.repository.vattu.phieunhapkho;

import com.tcdt.qlnvhang.entities.vattu.phieunhapkho.NhPhieuNhapKhoVt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface NhPhieuNhapKhoVtRepository extends BaseRepository<NhPhieuNhapKhoVt, Long>, NhPhieuNhapKhoVtRepositoryCustom {
    @Transactional
    @Modifying
    void deleteByIdIn(Collection<Long> ids);

    Optional<NhPhieuNhapKhoVt> findFirstBySoPhieu(String soPhieu);

}
