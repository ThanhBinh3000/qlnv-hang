package com.tcdt.qlnvhang.response.khoahoccongnghebaoquan;
import com.tcdt.qlnvhang.request.khoahoccongnghebaoquan.QuyChuanQuocGiaDtlReq;
import lombok.Data;

import java.util.List;

@Data
public class KhCnBaoQuanPreviewRes {

    String tenLoaiVthh;

    String tenDvi;

    String ngayHieuLuc;

    String ngayHetHieuLuc;

    List<QuyChuanQuocGiaDtlReq> tieuChuanList;



}
