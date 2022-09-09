package com.tcdt.qlnvhang.repository.xuathang.phieuxuatkho;

import com.tcdt.qlnvhang.entities.xuathang.phieuxuatkho.XhPhieuXuatKho;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface XhPhieuXuatKhoRepository extends BaseRepository<XhPhieuXuatKho, Long>, XhPhieuXuatKhoRepositoryCustom {
    @Transactional
    @Modifying
    void deleteByIdIn(List<Long> ids);

    Optional<XhPhieuXuatKho> findFirstBySoHd(String so);
}
