package com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang;

import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbChuanBiKhoDtlPheDuyetDto;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbChuanBiKhoDtlThucHienDto;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DcnbBbChuanBiKhoHdrPreview {
    private String maDvi;
    private String tenDvi;
    private String maQhns;
    private String soBban;
    private String ngayLap;
    private String tenChiCuc; //Tên chi cục
    private String tenLanhDao;
    private String tenKeToan;
    private String ktvBaoQuan;//KTV bảo quản
    private String tenThuKho;
    private String chungLoaiHangHoa; //Chủng loại hàng DTQG
    private String tenNganKho;
    private String tenLoKho;
    private String loaiHinhKho;
    private BigDecimal tichLuong;
    private BigDecimal thucNhap;// Thực nhập
    private String pthucBquan;
    private String hthucKlot;
    private BigDecimal dinhMucGiao;
    private BigDecimal dinhMucThucTe;
    private BigDecimal tongSoKinhPhiThucTeDaThucHien;//Tổng số kinh phí thực tế đã thực hiện
    private String tongSoKinhPhiThucTeDaThucHienText;//Tổng số kinh phí thực tế đã thực hiện viết bằng chữ
    private String nhanXet;
    private int ngayNhap;
    private int thangNhap;
    private int namNhap;
    private List<DcnbBbChuanBiKhoDtlPheDuyetDto> dcnbBbChuanBiKhoDtlPheDuyetDto;
    private List<DcnbBbChuanBiKhoDtlThucHienDto> dcnbBbChuanBiKhoDtlThucHienDto;
}
