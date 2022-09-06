package com.tcdt.qlnvhang.response.xuathang.phieuxuatkho;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.exolab.castor.types.DateTime;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class XhPhieuXuatKhoRes {
    private Long id;
    private Long sqdxId;
    private Long pknclId;
    private String maDvi;
    private String maQHNS;

    private Integer spXuatKho;
    private String nguoiNhanHang;
    private String boPhan;


    private String maDiemkho;
    private String maNhakho;
    private String maNgankho;
    private String maNganlo;
    private String maLoaiHangHoa;
    private String maChungLoaiHangHoa;


    private DateTime xuatHang;
    private String lyDoXuatKho;
    private String trangThai;
    private Integer so;
    private Integer nam;


    private LocalDate ngayTao;
    private Long nguoiTaoId;
    private LocalDate ngaySua;
    private Long nguoiSuaId;
    private Long nguoiGuiDuyetId;
    private LocalDate ngayGuiDuyet;
    private Long nguoiPduyetId;
    private LocalDate ngayPduyet;
    private List<XhPhieuXuatKhoCtRes> ds = new ArrayList<>();
}
