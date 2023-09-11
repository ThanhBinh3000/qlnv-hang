package com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQDtl;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DcnbBBNTBQHdrPreview {
    private String maDvi;
    private String tenDvi;
    private String maQhns;
    private String soBban;
    private String ngayLap;
    private String tenChiCuc; //Tên chi cục
    private String ldChiCuc;
    private String keToan;
    private String ktvBaoQuan;//KTV bảo quản
    private String thuKho;
    private String chungLoaiHangHoa; //Chủng loại hàng DTQG
    private String tenNganKho;
    private String tenLoKho;
    private String loaiHinhKho;
    private Double tichLuongKhaDung;
    private BigDecimal slThucNhapDc;
    private String phuongThucBaoQuan;
    private String hinhThucKeLot; //Hình thức kê lót
    private String hinhThucBaoQuan;
    private Double dinhMucDuocGiao;
    private Double dinhMucTT;
    private BigDecimal tongKinhPhiDaTh;
    private String tongKinhPhiDaThBc;
    private String nhanXet;
    private List<DcnbBBNTBQDtl> dcnbBBNTBQDtl;
}
