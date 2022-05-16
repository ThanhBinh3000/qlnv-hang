package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "HH_SO_KHO_HDR")
@Data
public class HhSoKhoHdr extends BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_SO_KHO_HDR_SEQ")
	@SequenceGenerator(sequenceName = "HH_SO_KHO_HDR_SEQ", allocationSize = 1, name = "HH_SO_KHO_HDR_SEQ")
	private Long id;
	Date ngayMoSo;
	String maHhoa;
	String tenHhoa;
	String maDvi;
	String maNgan;
	String maLo;
	String dviTinh;
	String thuKho;
	String keToan;
	String ttruongDvi;
	Integer nam;
/*
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference
	private List<HhSoKhoDtl> children = new ArrayList<>();

	public void setChildren(List<HhSoKhoDtl> children) {
		this.children.clear();
		for (HhSoKhoDtl child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(HhSoKhoDtl child) {
		child.setParent(this);
		this.children.add(child);
	}
	*/
}
