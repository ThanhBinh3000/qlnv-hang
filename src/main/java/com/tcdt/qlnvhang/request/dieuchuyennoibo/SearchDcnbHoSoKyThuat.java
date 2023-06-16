package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SearchDcnbHoSoKyThuat extends BaseRequest {
    private String maDvi;
    private Boolean isVatTu = false;
    private List<String> dsLoaiHang = new ArrayList<>();
}
