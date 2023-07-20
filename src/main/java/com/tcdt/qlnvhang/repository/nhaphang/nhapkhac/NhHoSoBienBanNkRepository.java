package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoBienBan;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.hosokythuat.NhHoSoBienBanNk;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhHoSoBienBanNkRepository extends BaseRepository<NhHoSoBienBanNk,Long> {

    List<NhHoSoBienBanNk> findAllByIdIn(List<Long> ids);

    List<NhHoSoBienBanNk> findAllBySoHoSoKyThuat(String soHoSoKyThuat);

    void deleteAllBySoBbLayMau(String soBbLayMau);


}
