package com.tcdt.qlnvhang.service.filedinhkem;


import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

public interface FileDinhKemService {

    @Transactional(rollbackOn = Exception.class)
    List<FileDinhKem> saveListFileDinhKem(List<FileDinhKemReq> fileDinhKemReqs,
                                          Long dataId,
                                          String dataType);

    List<FileDinhKem> search(Long dataId, Collection<String> dataTypes);
    List<FileDinhKem> search(Long dataId, String dataTypes);

    void delete(Long dataId, Collection<String> dataTypes);
    void delete(Long dataId, String dataTypes);

    void saveFileDinhKems(Collection<FileDinhKem> fileDinhKems);

    void deleteMultiple(Collection<Long> dataIds, Collection<String> dataTypes);
}
