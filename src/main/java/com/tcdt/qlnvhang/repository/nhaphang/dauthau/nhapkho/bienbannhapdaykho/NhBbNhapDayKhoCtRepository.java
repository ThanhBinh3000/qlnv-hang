package com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bienbannhapdaykho;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKhoCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface NhBbNhapDayKhoCtRepository extends BaseRepository<NhBbNhapDayKhoCt, Long> {
//    List<NhBbNhapDayKhoCt> findAllByQlBienBanNdkLtIdOrderBySttAsc(Long qlBienBanNdkLtId);
//
    @Transactional
    @Modifying
    void deleteByIdBbNhapDayKho(Long idBbNhapDayKho);

}
