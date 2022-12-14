package com.tcdt.qlnvhang.repository.kiemtrachatluong;

import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoBienBanCt;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhHoSoBienBanCtRepository extends BaseRepository<NhHoSoBienBanCt,Long> {
    List<NhHoSoBienBanCt> findAllByIdHoSoBienBan(Long ids);
    List<NhHoSoBienBanCt> findAllByIdHoSoBienBanIn(List<Long> ids);

    void deleteAllByIdHoSoBienBan(Long idHoSoBienBan);
}
