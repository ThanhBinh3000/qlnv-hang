package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbThuaThieuDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbThuaThieuKiemKeDtl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Access;
import javax.persistence.AccessType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBbThuaThieuHdrReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private String maDviNhan;
    private String tenCanBo;
    private String canBoId;
    private String soBb;
    private LocalDate ngayLap;
    private Long qdDcCucId;
    private String soQdDcCuc;
    private LocalDate ngayKyQdCuc;
    private String soBcKetQuaDc;
    private String bcKetQuaDcId;
    private LocalDate ngayLapBcKetQuaDc;
    private String nguyenNhan;
    private String kienNghi;
    private String ghiChu;
    private String trangThai;
    private List<FileDinhKemReq> fileBienBanHaoDois = new ArrayList<>();
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    private List<DcnbBbThuaThieuDtl> chiTiet = new ArrayList<>();
    private List<DcnbBbThuaThieuKiemKeDtl> banKiemKe = new ArrayList<>();

    private LocalDate tuNgayLap;
    private LocalDate denNgayLap;
    private LocalDate tuNgayBc;
    private LocalDate denNgayBc;
}
