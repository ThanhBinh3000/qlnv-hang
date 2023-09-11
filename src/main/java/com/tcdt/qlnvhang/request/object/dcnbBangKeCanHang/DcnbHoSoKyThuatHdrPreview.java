package com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbHoSoBienBanDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbHoSoTaiLieuDtl;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DcnbHoSoKyThuatHdrPreview {
    private String soHoSoKyThuat;
    private String chungLoaiHangHoa; //Chủng loại hàng DTQG
    private String namKhoach; //Năm kế hoạch
    private String tenNganKho;
    private String tenLoKho;
    private String tenDiemKho;
    private String tenDvi; // Đơn vị tạo hồ sơ kỹ thuật
    private String ngayTao; // Ngày tạo hồ sơ kỹ thuật
    private String tenCbthskt; // Cán bộ tạo hồ sơ kỹ thuật
    private String ngayDuyetHskt;
    private List<DcnbHoSoTaiLieuDtl> danhSachHoSoTaiLieu;
    private List<DcnbHoSoBienBanDtl> danhSachHoSoBienBan;

}
