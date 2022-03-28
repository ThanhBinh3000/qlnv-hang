package com.tcdt.qlnvhang.request.search.quyetdinhpheduyetketqualuachonnhathauvatu;

import com.tcdt.qlnvhang.request.search.BaseSearchRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class QdPheDuyetKqlcntVtSearchReq extends BaseSearchRequest {
    private String soQd;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private Long vatTuId;
    private Integer namKeHoach;
}
