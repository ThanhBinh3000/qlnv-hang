package com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchXhDgPhieuXuatKhoReq extends BaseRequest {
    private String maDvi;
    private String dvql;
    private Long nam;
    private String soQdGiaoNvXh;
    private String soPhieuXuatKho;
    private LocalDate ngayXuatKhoTu;
    private LocalDate ngayXuatKhoDen;
    private String type;
    private String trangThai;
    private String loaiVthh;
}
