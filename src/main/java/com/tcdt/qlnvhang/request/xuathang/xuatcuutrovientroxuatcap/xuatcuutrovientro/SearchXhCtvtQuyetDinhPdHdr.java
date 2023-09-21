package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class SearchXhCtvtQuyetDinhPdHdr extends BaseRequest {
    private Integer nam;
    private String maDviDx;
    private String maDviGiao;
    private List<String> listMaDviGiao;
    private String dvql;
    private String soDx;
    private String soBbQd;
    private String loaiVthh;
    private String tenVthh;
    private LocalDate ngayKyTu;
    private LocalDate ngayKyDen;
    private LocalDate ngayDxTu;
    private LocalDate ngayDxDen;
    private LocalDate ngayKetThucDxTu;
    private LocalDate ngayKetThucDxDen;
    private String trangThai;
    private String type;
    private Boolean xuatCap;

    //dung cho chuc nang danh sach cua man hình chon
    private List<Long> idQdPdList = new ArrayList<>();
    private Boolean idQdPdNull = false;
    private List<Long> idQdGnvList = new ArrayList<>();
    private Boolean idQdGnvNull = false;
}
