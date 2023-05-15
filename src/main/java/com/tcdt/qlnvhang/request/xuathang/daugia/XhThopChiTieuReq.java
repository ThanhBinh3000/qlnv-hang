package com.tcdt.qlnvhang.request.xuathang.daugia;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class XhThopChiTieuReq {

    private Integer namKh;

    private String loaiVthh;

    private String cloaiVthh;

    private LocalDate ngayDuyetTu;

    private LocalDate ngayDuyetDen;
}
