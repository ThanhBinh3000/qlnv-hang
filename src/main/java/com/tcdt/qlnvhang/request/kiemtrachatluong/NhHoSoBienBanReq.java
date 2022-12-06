package com.tcdt.qlnvhang.request.kiemtrachatluong;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class NhHoSoBienBanReq {
    private Long id;
    private String maDvi;
    private String soBienBan;
    private String loaiBienBan;
    private LocalDate ngayGduyet;
    private String nguoiGduyet;
    private LocalDate ngayPduyet;
    private String nguoiPduyet;
    private String trangThai;
    private String lyDotuChoi;
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    private List<NhHoSoBienBanCtReq> hoSoBienBanCtList= new ArrayList<>();
}
