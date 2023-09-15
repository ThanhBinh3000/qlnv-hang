package com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang;

import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbNhapDayKhoDtlDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DcnbBbNhapDayKhoHdrPreview {
    private String tenDvi;
    private String maQhns;
    private String soBb;
    private String ngayLap;
    private String tenLanhDao;
    private String tenKeToan;
    private String ktvBaoQuan;
    private String tenThuKho;
    private String chungLoaiHangHoa;
    private String tenHangDtqg;
    private String tenNganKho;
    private String tenLoKho;
    private String tenNhaKho;
    private String tenDiemKho;
    private String tenDiaDiemKho;
    private String ngayBdNhap;
    private String ngayKtNhap;
    List<DcnbBbNhapDayKhoDtlDto> dcnbBbNhapDayKhoDtls;
}
