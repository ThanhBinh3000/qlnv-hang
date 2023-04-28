package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.THKeHoachDieuChuyenTongCucDtl;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ThKeHoachDieuChuyenTongCucHdrReq {
    private Long id;

    private LocalDate ngaytao;

    private Long nguoiTaoId;

    private LocalDate ngaySua;

    private Long nguoiSuaId;

    private String maTongHop;

    private LocalDate ngayTongHop;

    private String noiDung;

    private Integer namKeHoach;

    private String loaiDieuChuyen;

    private LocalDate thTuNgay;

    private LocalDate thDenNgay;

    private String loaiHangHoa;

    private String tenLoaiHangHoa;

    private String chungLoaiHangHoa;

    private String trangThai;

    private String maDVi;

    private String tenDVi;

    private LocalDateTime thoiGianTongHop;

    List<ThKeHoachDieuChuyenTongCucDtlReq> thKeHoachDieuChuyenTongCucDtls;
}
