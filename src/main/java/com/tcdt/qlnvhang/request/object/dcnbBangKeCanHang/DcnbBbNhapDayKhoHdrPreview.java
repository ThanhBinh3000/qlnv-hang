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
    private String ktvBaoQuan;//KTV bảo quản
    private String tenThuKho;
    private String chungLoaiHangHoa; //Chủng loại hàng DTQG
    private String tenHangDtqg; //Tên hàng DTQG
    private String tenNganKho;
    private String tenLoKho;
    private String tenNhaKho;
    private String tenDiemKho;
    private String tenDiaDiemKho;//Địa điểm kho
    private String ngayBdNhap;
    private String ngayKtNhap;
    List<DcnbBbNhapDayKhoDtlDto> dcnbBbNhapDayKhoDtls;
}
