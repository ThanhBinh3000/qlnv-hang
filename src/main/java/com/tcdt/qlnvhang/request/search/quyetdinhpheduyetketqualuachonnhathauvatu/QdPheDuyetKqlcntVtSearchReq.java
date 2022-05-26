package com.tcdt.qlnvhang.request.search.quyetdinhpheduyetketqualuachonnhathauvatu;

import com.tcdt.qlnvhang.request.search.BaseSearchRequest;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class QdPheDuyetKqlcntVtSearchReq extends BaseSearchRequest {
    private String soQd;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate tuNgay;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate denNgay;
    private Long vatTuId;
    private Integer namKeHoach;
}
