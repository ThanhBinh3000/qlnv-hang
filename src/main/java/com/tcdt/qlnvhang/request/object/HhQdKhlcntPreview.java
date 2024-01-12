package com.tcdt.qlnvhang.request.object;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDtl;
import lombok.Data;

import java.util.List;

@Data
public class HhQdKhlcntPreview {
    private String nguonVon;
    private String hthucLcnt;
    private String pthucLcnt;
    private String loaiHdong;
    private String tgianBdauTchuc;
    private String tgianThienHd;
    private String tenLoaiVthh;
    private String tenCloaiVthh;
    private String namKhoach;
    private String tongSl;
    private String tenDvi;
    private String tongThanhTien;
    private String tongThanhTienNhaThau;
    private String tongThanhTienDuThau;
    private String tongChenhLech;
    private String soQdPdKhlcnt;
    private String ngayPdKhlcnt;
    private List<HhQdKhlcntDsgthau> dsGthau;
    private List<DsGthauPreview> dsGthauKq;
    private List<HhQdKhlcntDtl> qdKhlcntDtls;
}
