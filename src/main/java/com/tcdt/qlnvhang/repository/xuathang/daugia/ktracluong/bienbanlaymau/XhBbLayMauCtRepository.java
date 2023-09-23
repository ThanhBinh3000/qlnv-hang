package com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.bienbanlaymau;

import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauCt;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhBbLayMauCtRepository extends BaseRepository<XhBbLayMauCt, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhBbLayMauCt> findAllByIdHdr(Long idHdr);

    List<XhBbLayMauCt> findByIdHdrIn(List<Long> listId);
}
