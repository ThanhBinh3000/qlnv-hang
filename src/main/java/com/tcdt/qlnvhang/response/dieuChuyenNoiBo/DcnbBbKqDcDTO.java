package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbKqDcDtl;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Access;
import javax.persistence.AccessType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbBbKqDcDTO {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private String maDviNhan;
    private String soBc;
    private LocalDate ngayBc;
    private String tenBc;
    private Long qdDcCucId;
    private String soQdDcCuc;
    private Long qdDcTcId;
    private String soQdDcTc;
    private LocalDate ngayKyQdTc;
    private String noiDung;
    private String trangThai;
    private String lyDoTuChoi;
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    private List<DcnbBbKqDcDtl> danhSachDaiDien = new ArrayList<>();
    private String tenTrangThai;

    public DcnbBbKqDcDTO(Long id, Integer nam, String maDvi, String tenDvi, String maDviNhan, String soBc, LocalDate ngayBc, String tenBc, Long qdDcCucId, String soQdDcCuc, Long qdDcTcId, String soQdDcTc, LocalDate ngayKyQdTc, String noiDung, String trangThai, String lyDoTuChoi, List<FileDinhKemReq> fileDinhKems, List<DcnbBbKqDcDtl> danhSachDaiDien, String tenTrangThai) {
        this.id = id;
        this.nam = nam;
        this.maDvi = maDvi;
        this.tenDvi = tenDvi;
        this.maDviNhan = maDviNhan;
        this.soBc = soBc;
        this.ngayBc = ngayBc;
        this.tenBc = tenBc;
        this.qdDcCucId = qdDcCucId;
        this.soQdDcCuc = soQdDcCuc;
        this.qdDcTcId = qdDcTcId;
        this.soQdDcTc = soQdDcTc;
        this.ngayKyQdTc = ngayKyQdTc;
        this.noiDung = noiDung;
        this.trangThai = trangThai;
        this.lyDoTuChoi = lyDoTuChoi;
        this.fileDinhKems = fileDinhKems;
        this.danhSachDaiDien = danhSachDaiDien;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
