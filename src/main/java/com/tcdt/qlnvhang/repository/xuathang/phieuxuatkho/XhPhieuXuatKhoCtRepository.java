package com.tcdt.qlnvhang.repository.xuathang.phieuxuatkho;

import com.tcdt.qlnvhang.entities.xuathang.XhQdGiaoNvuXuatCt;
import com.tcdt.qlnvhang.entities.xuathang.phieuxuatkho.XhPhieuXuatKhoCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface XhPhieuXuatKhoCtRepository extends BaseRepository<XhPhieuXuatKhoCt, Long> {
    void deleteAllByPxuatKhoId(Long id);

    void deleteByPxuatKhoIdIn(List<Long> id);

    List<XhPhieuXuatKhoCt> findByPxuatKhoIdIn(Collection<Long> ids);

}
