package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoKyThuatCt;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.hosokythuat.NhHoSoKyThuatCtNk;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface NhHoSoKyThuatCtNkRepository extends BaseRepository<NhHoSoKyThuatCtNk, Long>  {

    List<NhHoSoKyThuatCtNk> findByHoSoKyThuatIdIn(Collection<Long> hoSoKyThuatIds);

    List<NhHoSoKyThuatCtNk> findByHoSoKyThuatId(Long hoSoKyThuatId);

    @Transactional
    @Modifying
    void deleteByHoSoKyThuatIdIn(Collection<Long> hoSoKyThuatIds);

    @Transactional
    @Modifying
    void deleteAllByHoSoKyThuatId(Long hoSoKyThuatId);
}
