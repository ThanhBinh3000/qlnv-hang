package com.tcdt.qlnvhang.request.object;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HhDchinhDxKhLcntDtlPreview {
    private String tenDvi;
    private String tgianNhang;
    private String tgianNhangDc;
    private List<HhDchinhDxKhLcntGthauPreview> children;
}
