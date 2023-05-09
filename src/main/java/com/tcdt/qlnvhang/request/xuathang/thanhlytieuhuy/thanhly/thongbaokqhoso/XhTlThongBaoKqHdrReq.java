package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.thongbaokqhoso;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhTlThongBaoKqHdrReq extends BaseRequest {

    private Long id;

    private String maDvi;

    private String soThongBao;

    private Long idHoSo;

    private String soHoSo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTrinhDuyetHsTl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayThamDinhHsTl;

    private String trangThaiThanhLy;

    private String noiDung;

    private String lyDoKhongPduyet;

    private String trichYeu;

    @Transient
    private List<FileDinhKemReq> fileDinhKem = new ArrayList<>();
}
