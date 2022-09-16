package com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat;

import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNxDdiem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhQdGiaoNvuNxDdiemRepository extends BaseRepository<HhQdGiaoNvuNxDdiem, Long> {

    void deleteAllByIdCt(Long idCt);

    List<HhQdGiaoNvuNxDdiem> findAllByIdCt(Long idCt);

}
