package com.tcdt.qlnvhang.service.filedinhkem;

import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class FileDinhKemServiceImpl implements FileDinhKemService {

    @Autowired
    private FileDinhKemRepository fileDinhKemRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public List<FileDinhKem> saveListFileDinhKem(List<FileDinhKemReq> fileDinhKemReqs,
                                                 Long dataId,
                                                 String dataType) {

        fileDinhKemRepository.deleteByDataIdAndDataTypeIn(dataId, Collections.singleton(dataType));

        if (CollectionUtils.isEmpty(fileDinhKemReqs))
            return Collections.emptyList();

        List<FileDinhKem> fileDinhKems = new ArrayList<>();
        for (FileDinhKemReq dinhKemReq : fileDinhKemReqs) {
            FileDinhKem dinhKem = new ModelMapper().map(dinhKemReq, FileDinhKem.class);
            dinhKem.setCreateDate(new Date());
            dinhKem.setDataType(dataType);
            dinhKem.setDataId(dataId);
            fileDinhKems.add(dinhKem);
        }
        fileDinhKemRepository.saveAll(fileDinhKems);

        return fileDinhKems;
    }

    @Override
    public List<FileDinhKem> search(Long dataId, Collection<String> dataTypes) {
        return fileDinhKemRepository.findByDataIdAndDataTypeIn(dataId, dataTypes);
    }

    @Override
    public List<FileDinhKem> search(Long dataId, String dataTypes) {
        return fileDinhKemRepository.findByDataIdAndDataType(dataId, dataTypes);
    }

    @Override
    public void delete(Long dataId, Collection<String> dataTypes) {
        fileDinhKemRepository.deleteByDataIdAndDataTypeIn(dataId, dataTypes);
    }

    @Override
    public void delete(Long dataId, String dataTypes) {
        fileDinhKemRepository.deleteByDataIdAndDataType(dataId, dataTypes);
    }

    @Override
    public void saveFileDinhKems(Collection<FileDinhKem> fileDinhKems) {
        fileDinhKemRepository.saveAll(fileDinhKems);
    }

    @Override
    public void deleteMultiple(Collection<Long> dataIds, Collection<String> dataTypes) {
        fileDinhKemRepository.deleteByDataIdInAndDataTypeIn(dataIds, dataTypes);
    }
}
