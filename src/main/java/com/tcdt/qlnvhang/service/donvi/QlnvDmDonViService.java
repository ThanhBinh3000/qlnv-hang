package com.tcdt.qlnvhang.service.donvi;

import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;

public interface QlnvDmDonViService {
    QlnvDmDonvi getDonViByMa(String ma);

    String getCapDviByMa(String ma) throws Exception;
}
