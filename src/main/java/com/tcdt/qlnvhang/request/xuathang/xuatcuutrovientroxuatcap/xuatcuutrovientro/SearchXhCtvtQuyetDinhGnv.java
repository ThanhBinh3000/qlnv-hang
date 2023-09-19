package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class SearchXhCtvtQuyetDinhGnv extends BaseRequest {
   Long id;
   Integer nam;
   String maDvi;
   String maDviGiao;
   String dvql;
   String soBbQd;
   String tenVthh;
   String loaiVthh;
   String trichYeu;
   LocalDate ngayKyTu;
   LocalDate ngayKyDen;
   String trangThai;
   String trangThaiXh;
   List<String> listTrangThaiXh = new ArrayList<>();
   String type;

  //dung cho chuc nang list cua man hình Qdpd
   List<Long> idQdPdList = new ArrayList<>();
   Boolean idQdPdNull = false;
}
