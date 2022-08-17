package com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetkehoachbandaugia;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BhQdPheDuyetKhbdgCtResponse {
    private Long id;
    private Long maDonVi;
    private Long bhDgKeHoachId;
    private LocalDate ngayKy;
    private String trichYeu;
    private BigDecimal soLuongDvTaiSan;
    private BigDecimal giaKhoiDiem;
    private BigDecimal khoanTienDatTruoc;
    private Long bhTongHopDeXuatId;
    private String tenDonVi;
}
