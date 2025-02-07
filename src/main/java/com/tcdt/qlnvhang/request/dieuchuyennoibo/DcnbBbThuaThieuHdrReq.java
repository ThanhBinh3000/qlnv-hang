package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbThuaThieuDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbThuaThieuKiemKeDtl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBbThuaThieuHdrReq extends BaseRequest {
    private Long id;
    @NotNull
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    @NotNull
    private String maDviNhan;
    private String tenCanBo;
    private String tenBaoCao;
    private String canBoId;
    private String soBb;
    @NotNull
    private LocalDate ngayLap;
    @NotNull
    private Long qdDcCucId;
    @NotNull
    private String soQdDcCuc;
    @NotNull
    private LocalDate ngayKyQdCuc;
    private String soBcKetQuaDc;
    private Long bcKetQuaDcId;
    private LocalDate ngayLapBcKetQuaDc;
    private String nguyenNhan;
    private String kienNghi;
    private String ghiChu;
    private String trangThai;
    private List<FileDinhKemReq> fileBienBanHaoDois = new ArrayList<>();
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    private List<DcnbBbThuaThieuDtl> chiTiet = new ArrayList<>();
    @Valid
    private List<DcnbBbThuaThieuKiemKeDtl> banKiemKe = new ArrayList<>();

    private LocalDate tuNgayLap;
    private LocalDate denNgayLap;
    private LocalDate tuNgayBc;
    private LocalDate denNgayBc;
}
