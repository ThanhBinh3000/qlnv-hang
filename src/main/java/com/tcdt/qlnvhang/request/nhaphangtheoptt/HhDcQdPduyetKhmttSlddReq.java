package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdPdKhMttSlddDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class HhDcQdPduyetKhmttSlddReq {
    private Long id;
    private Long idDxKhmtt;
    private Long idDcKhmtt;
    private String maDvi;
    private String tenDvi;
    private String maDiemKho;
    private String diaDiemKho;
    private BigDecimal soLuongCtieu;
    private BigDecimal soLuongKhDd;
    private BigDecimal soLuongDxmtt;
    private BigDecimal donGiaVat;
    private BigDecimal thanhTien;
    private String diaChi;
    private String tenGoiThau;
    private BigDecimal soLuongChiTieu;
    private BigDecimal donGia;
    private BigDecimal tongSoLuong;
    private BigDecimal tongThanhTien;
    private BigDecimal tongThanhTienVat;
    private BigDecimal soLuong;
    List<HhDcQdPdKhmttSlddDtlReq> children= new ArrayList<>();
}
