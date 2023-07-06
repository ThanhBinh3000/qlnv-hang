package com.tcdt.qlnvhang.response.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangDtl;
import lombok.Data;

import java.util.List;

@Data
public class XhXkTongHopKhXuatHangDTO {
    List<XhXkTongHopKhXuatCuc> listDxCuc;
    List<XhXkKhXuatHangDtl> xhXkKhXuatHangDtl;
    List<String> listSoToTrinh;
    List<Long> listIdKeHoach;
}
