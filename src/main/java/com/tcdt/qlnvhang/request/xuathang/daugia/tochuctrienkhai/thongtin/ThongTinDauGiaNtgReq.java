package com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThongTinDauGiaNtgReq extends BaseRequest {

  private Long id;
  private String hoaVaTen;
  private String soCccd;
  private String chucVu;
  private String diaChi;
  private String giayTo;
  private String loai;  //KM-khach moi    DGV-dau gia vien    NTG-nguoi tham gia

}
