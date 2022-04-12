package com.tcdt.qlnvhang.service.donvi;

import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QlnvDmDonViServiceImpl implements QlnvDmDonViService {
    @Autowired
    private QlnvDmDonviRepository qlnvDmDonviRepository;

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
}
