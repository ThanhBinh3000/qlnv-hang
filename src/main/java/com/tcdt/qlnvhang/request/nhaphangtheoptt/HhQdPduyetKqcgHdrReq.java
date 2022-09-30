package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class HhQdPduyetKqcgHdrReq  {
    private Long id;
    private Integer namKh;
    private String soQdPdCg;

    private Long idPdKq;
    private String soQdPdKq;
    private String trichYeu;
    private String ghiChu;
    private String maDvi;
    private String tenDvi;
    private String diaChiCgia;
    @Temporal(TemporalType.DATE)
    private Date tgianMkho;
    @Temporal(TemporalType.DATE)
    private Date tgianKthuc;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String trangThai;

    private List<FileDinhKemReq> fileDinhkems =new ArrayList<>();
    @Transient
    private List<HhChiTietTTinChaoGiaReq> hhChiTietTTinChaoGiaReqList = new ArrayList<>();
}
