package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoBienBanCt;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.hosokythuat.NhHoSoBienBanCtNk;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhHoSoBienBanCtNkRepository extends BaseRepository<NhHoSoBienBanCtNk,Long> {
    List<NhHoSoBienBanCtNk> findAllByIdHoSoBienBan(Long ids);
    List<NhHoSoBienBanCtNk> findAllByIdHoSoBienBanIn(List<Long> ids);

    void deleteAllByIdHoSoBienBan(Long idHoSoBienBan);
}
