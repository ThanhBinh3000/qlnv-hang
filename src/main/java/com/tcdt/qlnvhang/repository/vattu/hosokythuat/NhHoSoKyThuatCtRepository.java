package com.tcdt.qlnvhang.repository.vattu.hosokythuat;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoKyThuatCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface NhHoSoKyThuatCtRepository extends BaseRepository<NhHoSoKyThuatCt, Long>  {

    List<NhHoSoKyThuatCt> findByHoSoKyThuatIdIn(Collection<Long> hoSoKyThuatIds);

    List<NhHoSoKyThuatCt> findByHoSoKyThuatId(Long hoSoKyThuatId);

    @Transactional
    @Modifying
    void deleteByHoSoKyThuatIdIn(Collection<Long> hoSoKyThuatIds);

    @Transactional
    @Modifying
    void deleteAllByHoSoKyThuatId(Long hoSoKyThuatId);
}
