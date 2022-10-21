package com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bangkecanhang;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHangCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface NhBangKeChCtRepository extends BaseRepository<NhBangKeCanHangCt, Long> {

//    List<NhBangKeCanHangCt> findAllByQlBangKeCanHangLtId(Long bangKeId);

    @Transactional
    @Modifying
    void deleteByIdBangKeCanHangHdr(Long idBangKeCanHangHdr);

    List<NhBangKeCanHangCt> findAllByIdBangKeCanHangHdr(Long idBangKeCanHangHdr);
}
