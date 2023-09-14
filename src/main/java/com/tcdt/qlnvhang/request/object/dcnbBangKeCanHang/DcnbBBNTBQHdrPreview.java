package com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang;

import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBBNTBQDtlPheDuyetDto;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBBNTBQDtlThucHienDto;
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
    private String tenChiCuc;
    private String ldChiCuc;
    private String keToan;
    private String ktvBaoQuan;
    private String thuKho;
    private String chungLoaiHangHoa;
    private String tenNganKho;
    private String tenLoKho;
    private String loaiHinhKho;
    private Double tichLuongKhaDung;
    private BigDecimal slThucNhapDc;
    private String phuongThucBaoQuan;
    private String hinhThucKeLot;
    private String hinhThucBaoQuan;
    private Double dinhMucDuocGiao;
    private Double dinhMucTT;
    private BigDecimal tongKinhPhiDaTh;
    private String tongKinhPhiDaThBc;
    private String nhanXet;
    private List<DcnbBBNTBQDtlPheDuyetDto> dcnbBBNTBQDtlPheDuyetDto;
    private List<DcnbBBNTBQDtlThucHienDto> dcnbBBNTBQDtlThucHienDto;
}
