package com.tcdt.qlnvhang.request.object;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HhDchinhDxKhLcntPreview {
    private String tenLoaiVthh;
    private String tenCloaiVthh;
    private Integer namKhoach;
    private String soQdPdKhlcnt;
    private String ngayPdKhlcnt;
    private String soQdDcKhlcnt;
    private String ngayPdDcKhlcnt;
    private String tongSl;
    private String tongSlDc;
    private String tongThanhTien;
    private String tenNguonVon;
    private String tenHthucLcnt;
    private String tenPthucLcnt;
    private String quy;
    private String tenLoaiHd;
    private List<HhDchinhDxKhLcntDtlPreview> children;
    private List<HhDchinhDxKhLcntGthauPreview> dsGthauVt;
}
