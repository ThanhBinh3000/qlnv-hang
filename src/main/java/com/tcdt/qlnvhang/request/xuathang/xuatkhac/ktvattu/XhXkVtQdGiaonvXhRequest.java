package com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXkVtQdGiaonvXhRequest extends BaseRequest {

    Long id;
    Integer namKeHoach;
    String maDvi;
    String loai;
    String soQuyetDinh;
    String trichYeu;
    LocalDate ngayKy;
    LocalDate thoiHanXuatHang;
    String soCanCu;
    Long idCanCu;
    String trangThai;
    String tenTrangThaiXh;
    Integer capDvi;
    List<XhXkVtQdGiaonvXhDtl> xhXkVtQdGiaonvXhDtl = new ArrayList<>();
    List<FileDinhKem> fileDinhKems;
    //search params
    LocalDate ngayKyQdTu;
    LocalDate ngayKyQdDen;
    String dvql;
}
