package com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetketquabandaugia;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BhQdPheDuyetKqbdgSearchReq extends BaseRequest {
    private String soQuyetDinh;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayKyTu;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayKyDen;
    private String loaiVthh;
    private String maVatTu;
    private String maVatTuCha;
    private Integer nam;
    private String trichYeu;
}
