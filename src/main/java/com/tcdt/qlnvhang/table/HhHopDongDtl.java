package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.util.Contains;

import lombok.Data;

@Entity
@Table(name = "HH_HOP_DONG_DTL")
@Data
public class HhHopDongDtl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_HOP_DONG_DTL_SEQ")
	@SequenceGenerator(sequenceName = "HH_HOP_DONG_DTL_SEQ", allocationSize = 1, name = "HH_HOP_DONG_DTL_SEQ")
	private Long id;

	String shgt;
	String tenGthau;
	BigDecimal soLuong;
	BigDecimal donGia;
	Long vat;
	BigDecimal giaTruocVat;
	BigDecimal giaSauVat;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
	@JsonBackReference
	private HhHopDongHdr parent;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference
	@Where(clause = "type='" + Contains.HOP_DONG + "'")
	private List<HhDdiemNhapKho> children = new ArrayList<>();

	public void setChildren(List<HhDdiemNhapKho> children) {
		this.children.clear();
		for (HhDdiemNhapKho child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(HhDdiemNhapKho child) {
		child.setParent(this);
		this.children.add(child);
	}

}
