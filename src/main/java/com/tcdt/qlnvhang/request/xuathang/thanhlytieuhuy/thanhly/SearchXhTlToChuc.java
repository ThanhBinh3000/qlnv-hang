package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

@Data
public class SearchXhTlToChuc extends BaseRequest {
    private String dvql;

    private Integer nam;

    private Long idQdTl;
}
