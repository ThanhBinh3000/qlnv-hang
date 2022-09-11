package com.tcdt.qlnvhang.repository.xuathang.bangkecanhang;

import com.tcdt.qlnvhang.entities.xuathang.bangkecanhang.XhBangKeCanHangCt;
import com.tcdt.qlnvhang.entities.xuathang.phieuxuatkho.XhPhieuXuatKhoCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface XhBangKeCanHangCtRepository extends BaseRepository<XhBangKeCanHangCt, Long> {
    void deleteAllByBkCanHangID(Long id);

    void deleteByBkCanHangIDIn(List<Long> id);

    List<XhBangKeCanHangCt> findByBkCanHangIDIn(Collection<Long> ids);

}
