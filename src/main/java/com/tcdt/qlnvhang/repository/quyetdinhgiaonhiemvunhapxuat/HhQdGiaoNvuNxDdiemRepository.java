package com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat;

import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNxDdiem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhQdGiaoNvuNxDdiemRepository extends BaseRepository<NhQdGiaoNvuNxDdiem, Long> {

    void deleteAllByIdCt(Long idCt);

    List<NhQdGiaoNvuNxDdiem> findAllByIdCt(Long idCt);

}
