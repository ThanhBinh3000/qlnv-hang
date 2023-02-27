package com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.kiemnghiemcl;

import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl.XhPhieuKnghiemCluongCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface XhPhieuKnghiemCluongCtRepository extends BaseRepository<XhPhieuKnghiemCluongCt, Long> {

//    List<XhPhieuKnghiemCluongCt> findByPhieuKnghiemIdIn(Collection<Long> phieuKnghiemIds);
//
//    @Transactional
//    @Modifying
//    void deleteByPhieuKnghiemIdIn(Collection<Long> phieuKnghiemIds);

    List<XhPhieuKnghiemCluongCt> findAllByIdHdr(Long idHdr);

    void deleteAllByIdHdr (Long idHdr);


}
