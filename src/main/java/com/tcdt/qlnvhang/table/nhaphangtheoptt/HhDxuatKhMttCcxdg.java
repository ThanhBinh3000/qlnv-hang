package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinDxKhMttCcxdg;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "HH_DX_KHMTT_CCXDG")
@Data
public class HhDxuatKhMttCcxdg implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_DX_KHMTT_CCXDG";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DX_KHMTT_CCXDG_SEQ")
    @SequenceGenerator(sequenceName = "HH_DX_KHMTT_CCXDG_SEQ", allocationSize = 1, name = "HH_DX_KHMTT_CCXDG_SEQ")
    private Long id;
    private Long idHdr;

    private String tenTlieu;
    private String loaiCanCu;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @JsonManagedReference
    @Where(clause = "data_type='" + HhDxuatKhMttCcxdg.TABLE_NAME + "'")
    private List<FileDKemJoinDxKhMttCcxdg> children = new ArrayList<>();

    public void setChildren(List<FileDKemJoinDxKhMttCcxdg> children) {
        this.children.clear();
        for (FileDKemJoinDxKhMttCcxdg child : children) {
            child.setParent(this);
        }
        this.children.addAll(children);
    }

    public void addChild(FileDKemJoinDxKhMttCcxdg child) {
        child.setParent(this);
        this.children.add(child);
    }
}
