package com.tcdt.qlnvhang.entities.xuathang.phieukiemnghiemchatluong;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "XH_PHIEU_KNGHIEM_CLUONG_CT")
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XhPhieuKnghiemCluongCt implements Serializable {
    private static final long serialVersionUID = -1315211820556764708L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_PHIEU_KNGHIEM_CLUONG_CT_SEQ")
    @SequenceGenerator(sequenceName = "XH_PHIEU_KNGHIEM_CLUONG_CT_SEQ", allocationSize = 1, name = "XH_PHIEU_KNGHIEM_CLUONG_CT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "PHIEU_KNGHIEM_ID")
    private Long phieuKnghiemId;

    @Column(name = "STT")
    private Integer stt;

    @Column(name = "TEN_CTIEU")
    private String tenCtieu;

    @Column(name = "KQUA_KTRA")
    private String kquaKtra;

    @Column(name = "PPHAP_XDINH")
    private String pphapXdinh;

    @Column(name = "CHI_SO_CHAT_LUONG")
    private String chiSoChatLuong;

}
