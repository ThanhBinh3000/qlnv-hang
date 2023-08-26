package com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ThongTinDauGiaNtgReq extends BaseRequest {
    private Long id;
    private Long idHdr;
    private String hoVaTen;
    private String soCccd;
    private String chucVu;
    private String diaChi;
    private String loai;  //KM-khach moi    DGV-dau gia vien    NTG-nguoi tham gia
    private BigDecimal idVirtual;
}
