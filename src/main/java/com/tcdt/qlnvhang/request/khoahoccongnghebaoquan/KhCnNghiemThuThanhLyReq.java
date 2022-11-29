package com.tcdt.qlnvhang.request.khoahoccongnghebaoquan;

import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class KhCnNghiemThuThanhLyReq {
    private Long id;
    private Long idHdr;
    @Temporal(TemporalType.DATE)
    private Date ngayNghiemThu;
    private String diaDiem;
    private String hoTen;
    private String donVi;
    private String yKienDanhGia;
    private Integer tongDiem;
    private String xepLoai;
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    private List<KhCnNghiemThuThanhLyDtlReq> children = new ArrayList<>();
}
