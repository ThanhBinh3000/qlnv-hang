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
    private LocalDate ngayCgiaTu;
    private LocalDate  ngayCgiaDen;
    private String tochucCanhan;
    private String maDviChiCuc;
    private Integer lastest ;
    private String loaiVthh;
    private String soDxuat;
    private List<String> pthucBanTrucTiep = new ArrayList<>();
    private String dvql;
}
