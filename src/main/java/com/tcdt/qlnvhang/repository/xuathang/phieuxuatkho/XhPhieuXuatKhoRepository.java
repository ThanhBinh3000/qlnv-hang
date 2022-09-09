package com.tcdt.qlnvhang.repository.xuathang.phieuxuatkho;

import com.tcdt.qlnvhang.entities.xuathang.phieuxuatkho.XhPhieuXuatKho;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhPhieuXuatKhoRepository extends BaseRepository<XhPhieuXuatKho, Long>, XhPhieuXuatKhoRepositoryCustom {
    void deleteByIdIn(List<Long> ids);

}
