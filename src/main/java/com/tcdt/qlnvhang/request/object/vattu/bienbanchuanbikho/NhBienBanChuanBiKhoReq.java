package com.tcdt.qlnvhang.request.object.vattu.bienbanchuanbikho;

import com.tcdt.qlnvhang.request.object.SoBienBanPhieuReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NhBienBanChuanBiKhoReq extends SoBienBanPhieuReq {

    private Long id;

    private Long idQdGiaoNvNh;

    private String soBienBan;

    private Date ngayNghiemThu;

    private String keToanDonVi;

    private String loaiHinhKho;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    private String pthucBquan;

    private String thucNhap;

    private String hthucBquan;

    private String ketLuan;

    private String maDvi;

    private BigDecimal tongSo;

    private Integer nam;

    private String loaiVthh;

    private String cloaiVthh;

    private BigDecimal soLuongDdiemGiaoNvNh;

    private Long idDdiemGiaoNvNh;

    private List<NhBienBanChuanBiKhoCtReq> children = new ArrayList<>();
}

