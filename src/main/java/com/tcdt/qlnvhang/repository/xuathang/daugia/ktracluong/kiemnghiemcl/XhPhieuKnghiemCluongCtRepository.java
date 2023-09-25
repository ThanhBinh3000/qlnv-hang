package com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.kiemnghiemcl;

import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl.XhPhieuKnghiemCluongCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhPhieuKnghiemCluongCtRepository extends BaseRepository<XhPhieuKnghiemCluongCt, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhPhieuKnghiemCluongCt> findAllByIdHdr(Long idHdr);

    List<XhPhieuKnghiemCluongCt> findByIdHdrIn(List<Long> listId);
}
