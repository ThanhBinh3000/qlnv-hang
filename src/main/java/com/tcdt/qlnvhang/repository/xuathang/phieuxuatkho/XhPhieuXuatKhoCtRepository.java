package com.tcdt.qlnvhang.repository.xuathang.phieuxuatkho;

import com.tcdt.qlnvhang.entities.xuathang.phieuxuatkho.XhPhieuXuatKhoCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface XhPhieuXuatKhoCtRepository extends BaseRepository<XhPhieuXuatKhoCt, Long> {
    @Transactional
    void deleteAllByPxuatKhoId(Long id);
    @Transactional
    void deleteByPxuatKhoIdIn(List<Long> id);

    List<XhPhieuXuatKhoCt> findByPxuatKhoIdIn(Collection<Long> ids);

}
