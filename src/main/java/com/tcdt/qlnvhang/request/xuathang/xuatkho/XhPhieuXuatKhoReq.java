package com.tcdt.qlnvhang.request.xuathang.xuatkho;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class XhPhieuXuatKhoReq  extends BaseRequest {
    private Long id;
    private Long sqdxId;
    private Long pknclId;
    private String maDvi;
    private String maQHNS;
    private String soHd;

    private String spXuatKho;
    private String nguoiNhanHang;
    private String boPhan;


    private String maDiemkho;
    private String maNhakho;
    private String maNgankho;
    private String maNganlo;
    private String maLoaiHangHoa;
    private String maChungLoaiHangHoa;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private  LocalDateTime xuatKho;
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
    private String ghiChu;
    private String tongTien;

    private List<XhPhieuXuatKhoCtReq> ds = new ArrayList<>();
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

}
