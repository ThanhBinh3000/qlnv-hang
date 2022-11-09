package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia;

import lombok.Data;

import javax.persistence.Transient;

@Data
public class XhDxKhBanDauGiaDtlReq {
    private Long id;
    private Long idHdr;
    private String maDvi;
    private String tenDvi;
    private String maDiemKho;
    private String maNganKho;
    private String maLoKho;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
    private String maDviTsan;
    private String soLuong;
    private String DviTinh;
    private String giaKhongVat;
    private String giaKhoiDiem;
    private String tienDatTruoc;
}
