package com.tcdt.qlnvhang.request.kiemtrachatluong;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class NhHoSoBienBanReq extends BaseRequest {

    private Long id;

    private String maDvi;

    private String soBienBan;

    private String loaiBb;

    private Integer nam;

    private String soHoSoKyThuat;

    private String soBbLayMau;
    private Long idBbLayMau;

    private String soQdGiaoNvNh;

    private Long idQdGiaoNvNh;

    private String soHd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHd;

    private String soBienBanGuiHang;

    private String diaDiemKiemTra;

    private String loaiVthh;

    private String tenLoaiVthh;

    private String cloaiVthh;

    private String tenCloaiVthh;

    private String noiDung;

    private String soSerial;

    private String kyMaHieu;

    private String ppLayMau;

    private BigDecimal soLuongNhap;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianNhap;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianBsung;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianKtra;

    private String ketLuan;

    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    private List<FileDinhKemReq> listCanCu = new ArrayList<>();

    private List<NhHoSoBienBanCtReq> children = new ArrayList<>();
}
