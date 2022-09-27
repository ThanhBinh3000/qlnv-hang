package com.tcdt.qlnvhang.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class HhQdPheduyetKhMttHdrSearchReq extends BaseRequest {

   private Integer namKh;

   private  String soQd;

   private String trichYeu;

   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
   private Date ngayKyQdTu;

   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
   private  Date ngayKyQdDen;

   private  String loaiVthh;

   private  String trangThai;

   private String trangThaiTh;

   private  String maDvi;

}
