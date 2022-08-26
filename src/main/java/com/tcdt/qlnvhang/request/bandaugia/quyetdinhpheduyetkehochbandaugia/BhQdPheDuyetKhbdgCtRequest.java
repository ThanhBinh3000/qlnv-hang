package com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetkehochbandaugia;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.NotNull;
import com.tcdt.qlnvhang.request.bandaugia.tonghopdexuatkhbdg.BhQdPheDuyetKhBdgThongTinTaiSanRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BhQdPheDuyetKhbdgCtRequest {
    private Long id;
    @NotNull
    private Long bhDgKeHoachId;
    @NotNull
    private Long quyetDinhPheDuyetId;
    List<BhQdPheDuyetKhBdgThongTinTaiSanRequest> thongTinTaiSans = new ArrayList<>();
}
