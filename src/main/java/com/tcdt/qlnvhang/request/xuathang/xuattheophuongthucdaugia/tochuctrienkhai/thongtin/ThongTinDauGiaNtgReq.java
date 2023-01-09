package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThongTinDauGiaNtgReq extends BaseRequest {

  private Long id;
  private String hoVaTen;
  private String soCccd;
  private String chucVu;
  private String diaChi;
  private String giayTo;
  private String loai;  //KM-khach moi    DGV-dau gia vien    NTG-nguoi tham gia

}
