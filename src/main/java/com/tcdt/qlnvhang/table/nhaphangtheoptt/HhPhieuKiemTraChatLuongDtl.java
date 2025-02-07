package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "HH_PHIEU_KT_CHAT_LUONG_DTL")
@Data
public class HhPhieuKiemTraChatLuongDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_PHIEU_KT_CHAT_LUONG_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_PHIEU_KT_CHAT_LUONG_DTL_SEQ")
    @SequenceGenerator(sequenceName = "HH_PHIEU_KT_CHAT_LUONG_DTL_SEQ", allocationSize = 1, name = "HH_PHIEU_KT_CHAT_LUONG_DTL_SEQ")
    private Long id;
    private Long idHdr;
    private String chiTieuCl;
    private String chiSoCl;
    private String ketQuaPt;
    private String phuongPhap;
}
