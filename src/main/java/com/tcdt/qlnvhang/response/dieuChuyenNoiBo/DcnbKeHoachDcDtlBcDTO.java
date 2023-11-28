package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DcnbKeHoachDcDtlBcDTO {
    private String loaiVthh;
    private String cloaiVthh;
    private String tenLoaiVthh;
    private String tenCloaiVthh;
    private String maDiemKho;
    private String tenDiemKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private String maLoKho;
    private String tenLoKho;

    public DcnbKeHoachDcDtlBcDTO(String loaiVthh, String cloaiVthh,
                                 String tenLoaiVthh,String tenCloaiVthh,
                                 String maDiemKho,String tenDiemKho,
                                 String maNhaKho,String tenNhaKho,
                                 String maNganKho,String tenNganKho,
                                 String maLoKho,String tenLoKho) {
        this.loaiVthh = loaiVthh;
        this.cloaiVthh = cloaiVthh;
        this.tenLoaiVthh = tenLoaiVthh;
        this.tenCloaiVthh = tenCloaiVthh;
        this.maDiemKho = maDiemKho;
        this.tenDiemKho = tenDiemKho;
        this.maNhaKho = maNhaKho;
        this.tenNhaKho = tenNhaKho;
        this.maNganKho = maNganKho;
        this.tenNganKho = tenNganKho;
        this.maLoKho = maLoKho;
        this.tenLoKho = tenLoKho;
    }
}
