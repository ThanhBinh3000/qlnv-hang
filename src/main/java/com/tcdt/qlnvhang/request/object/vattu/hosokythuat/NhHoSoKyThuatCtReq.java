package com.tcdt.qlnvhang.request.object.vattu.hosokythuat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class NhHoSoKyThuatCtReq {
    private Long id;
    private String tenHoSo;
    private String loaiTaiLieu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tdiemNhap;
    private Integer soLuong;
    private String ghiChu;
    private List<FileDinhKemReq> fileDinhKem;
}
