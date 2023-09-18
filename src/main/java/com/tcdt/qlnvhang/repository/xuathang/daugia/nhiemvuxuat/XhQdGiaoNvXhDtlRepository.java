package com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDtl;
import com.tcdt.qlnvhang.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XhQdGiaoNvXhDtlRepository extends BaseRepository<XhQdGiaoNvXhDtl, Long> {

    void deleteAllByIdHdr(Long idHdr);

    List<XhQdGiaoNvXhDtl> findAllByIdHdr(Long idHdr);

    List<XhQdGiaoNvXhDtl> findByIdHdrIn(List<Long> listId);

}
