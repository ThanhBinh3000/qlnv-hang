package com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.thongtin;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SearchXhTcTtinBttReq extends BaseRequest {

    private Integer namKh;

    private String maDvi;

    private LocalDate ngayCgiaTu;

    private LocalDate  ngayCgiaDen;

    private LocalDate ngayTaoTu;

    private LocalDate ngayTaoDen;

    private LocalDate ngayDuyetTu;

    private LocalDate ngayDuyetDen;

    private String maDviChiCuc;

    private String tochucCanhan;

    private Integer lastest ;

    private String loaiVthh;

    private String soDxuat;

    private List<String> pthucBanTrucTiep = new ArrayList<>();

    private Integer typeSoQdKq;
}
