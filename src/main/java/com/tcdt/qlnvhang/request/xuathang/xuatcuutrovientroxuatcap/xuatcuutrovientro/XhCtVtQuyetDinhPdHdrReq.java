package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhCtVtQuyetDinhPdHdrReq {
    private Long id;
    private String maDvi;
    private Integer nam;
    private String soQd;
    private LocalDate ngayKy;
    private LocalDate ngayHluc;
    private Long idTongHop;
    private String maTongHop;
    private LocalDate ngayThop;
    private Long idDx;
    private String soDx;
    private LocalDate ngayDx;
    private Long tongSoLuongDx;
    private Long tongSoLuong;
    private Long soLuongXuaCap;
    private String loaiVthh;
    private String cloaiVthh;
    private String trichYeu;
    private String trangThai;
    private String lyDoTuChoi;
    private String type;

    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;

    private List<FileDinhKemReq> fileDinhKem;

    private List<FileDinhKemReq> canCu = new ArrayList<>();

    private List<XhCtVtQuyetDinhPdDtlReq> quyetDinhPdDtl = new ArrayList<>();
}

