package com.tcdt.qlnvhang.service.donvi;

import com.tcdt.qlnvhang.entities.QlnvDmDonviEntity;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;

import java.util.List;

public interface QlnvDmDonViService {
    QlnvDmDonvi getDonViByMa(String ma);

    String getCapDviByMa(String ma) throws Exception;

    List<QlnvDmDonviEntity> findAllDviCache();
}

