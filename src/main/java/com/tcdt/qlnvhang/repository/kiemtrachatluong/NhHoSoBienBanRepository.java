package com.tcdt.qlnvhang.repository.kiemtrachatluong;

import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoBienBan;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhHoSoBienBanRepository extends BaseRepository<NhHoSoBienBan,Long> {

    List<NhHoSoBienBan> findAllByIdIn(List<Long> ids);


}
