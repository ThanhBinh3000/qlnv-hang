package com.tcdt.qlnvhang.service.donvi;

import com.tcdt.qlnvhang.entities.QlnvDmDonviEntity;
import com.tcdt.qlnvhang.repository.QlnvDmDonviEntityRepository;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QlnvDmDonViServiceImpl implements QlnvDmDonViService {
    @Autowired
    private QlnvDmDonviRepository qlnvDmDonviRepository;

    @Autowired
    private QlnvDmDonviEntityRepository qlnvDmDonviEntityRepository;

    @Override
    public QlnvDmDonvi getDonViByMa(String ma) {
        return qlnvDmDonviRepository.findByMaDvi(ma);
    }

    @Override
    public String getCapDviByMa(String ma) throws Exception {
        QlnvDmDonvi qlnvDmDonvi = this.getDonViByMa(ma);
        if (qlnvDmDonvi == null)
            throw new Exception("Đơn vị không tồn tại.");
        return qlnvDmDonvi.getCapDvi();
    }

    @Cacheable("dsdonvi")
    @Override
    public List<QlnvDmDonviEntity> findAllDviCache(){
        List<QlnvDmDonviEntity> dvi = qlnvDmDonviEntityRepository.findAll();
        return  dvi;
    }
}
