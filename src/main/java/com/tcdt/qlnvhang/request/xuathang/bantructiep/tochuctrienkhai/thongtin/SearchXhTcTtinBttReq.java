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
    private String soQdKq;
    private String soQdDc;
    private String soQdPd;
    private String soDxuat;
    private LocalDate ngayCgiaTu;
    private LocalDate ngayCgiaDen;
    private String tochucCanhan;
    private Integer lanDieuChinh;
    private Integer lastest;
    private String loaiVthh;
    private List<String> pthucBanTrucTiep = new ArrayList<>();
    private String dvql;
    private String maChiCuc;
    private String trangThaiHdr;
}