package com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang;

import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBBKetThucNKDtlDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DcnbBBKetThucNKHdrPreview {
    private String maDvi;
    private String maQhns;
    private String soBb;
    private String ngayLap;
    private String tenLanhDlanhdaoaoChiCuc;
    private String tenKeToanTruong;
    private Long ktvBQuan;
    private String tenThuKho;
    private String chungLoaiHangHoa;
    private String tenHangDtqg;
    private String tenNganKho;
    private String tenLoKho;
    private String tenNhaKho;
    private String tenDiemKho;
    private String diaDaDiemKho;
    private String ngayBatDauNhap;
    private String ngayKetThucNhap;
    private List<DcnbBBKetThucNKDtlDto> dcnbBBKetThucNKDtlList;
}
