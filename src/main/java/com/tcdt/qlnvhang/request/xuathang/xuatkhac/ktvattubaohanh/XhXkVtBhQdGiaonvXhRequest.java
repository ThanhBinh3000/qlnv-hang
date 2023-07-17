package com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhQdGiaonvXhDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXkVtBhQdGiaonvXhRequest extends BaseRequest {

  Long id;
  Integer nam;
  String maDvi;
  String soQuyetDinh;
  String loai;
  String trichYeu;
  LocalDate ngayKy;
  Integer soLanLm;
  LocalDate thoiHanXuatHang;
  String soCanCu;
  Long idCanCu;
  String trangThai;
  LocalDate ngayGduyet;
  Long nguoiGduyetId;
  LocalDate ngayPduyet;
  Long nguoiPduyetId;
  String lyDoTuChoi;
  String loaiCanCu;
  List<String> listTrangThaiXh = new ArrayList<>();
  Integer capDvi;
  List<XhXkVtBhQdGiaonvXhDtl> qdGiaonvXhDtl = new ArrayList<>();
  List<FileDinhKem> fileDinhKems;
  //search params
  LocalDate ngayKyQdTu;
  LocalDate ngayKyQdDen;
  String dvql;
}
