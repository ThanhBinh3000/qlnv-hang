package com.tcdt.qlnvhang.request.suachua;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScKiemTraChatLuongDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class ScKiemTraChatLuongReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maQhns;
    private String soPhieuKtcl;
    private LocalDate ngayLap;
    private Long idTruongPhongKtvq;
    private Long idQdXh;
    private String soQdXh;
    private Long idPhieuXuatKho;
    private String soPhieuXuatKho;
    private String dviKiemDinh;
    private LocalDate ngayKiemDinh;
    private String hinhThucBaoQuan;
    private String ketQua;
    private Integer dat;
    private String nhanXet;

    private List<FileDinhKemReq> fileDinhKemReq = new ArrayList<>();

    private List<ScKiemTraChatLuongDtl> children = new ArrayList<>();

    private Long idScDanhSachHdr;

    // Region sr
    private String maDviSr;
    private LocalDate ngayTu;
    private LocalDate ngayDen;
}
