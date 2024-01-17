package com.tcdt.qlnvhang.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttHdr;
import com.tcdt.qlnvhang.table.HhQdPheduyetKqMttSLDD;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBcanKeHangHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBienBanDayKhoHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdGiaoNvNhangDtl;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc.HopDongMttHdr;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
public class SoLuongDaKyHopDongDTO {
    private String soQd;
    private String maDvi;
    private String soHd;
    private String soQdGiaoNvNh;
    private Long idQdGiaoNvNh;
    private BigDecimal soLuong;

    public SoLuongDaKyHopDongDTO() {
    }

    public SoLuongDaKyHopDongDTO(String soHd, String soQd, String maDvi, BigDecimal soLuong, String soQdGiaoNvNh, Long idQdGiaoNvNh) {
        this.soQd = soQd;
        this.soHd = soHd;
        this.maDvi = maDvi;
        this.soLuong = soLuong;
        this.soQdGiaoNvNh = soQdGiaoNvNh;
        this.idQdGiaoNvNh = idQdGiaoNvNh;
    }
}
