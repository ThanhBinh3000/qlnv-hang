package com.tcdt.qlnvhang.response.xuathang.phieuxuatkho;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.exolab.castor.types.DateTime;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class XhPhieuXuatKhoRes {
    private Long id;
    private Long sqdxId;
    private String tenSqdx;
    private Long pknclId;
    private String maDvi;
    private String maQHNS;
    private String soHd;

    private String spXuatKho;
    private String nguoiNhanHang;
    private String boPhan;


    private String maDiemkho;
    private String tenDiemkho;
    private String maNhakho;
    private String tenNhakho;
    private String maNgankho;
    private String tenNgankho;
    private String maNganlo;
    private String tenNganlo;
    private String maLoaiHangHoa;
    private String tenLoaiHangHoa;
    private String maChungLoaiHangHoa;
    private String tenChungLoaiHangHoa;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime xuatKho;
    private String lyDoXuatKho;
    private String trangThai;
    private String tenTrangThai;
    private String ghiChu;
    private String tongTien;

    private List<XhPhieuXuatKhoCtRes> ds = new ArrayList<>();
}
