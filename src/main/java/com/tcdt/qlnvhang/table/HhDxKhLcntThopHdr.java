package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Table(name = HhDxKhLcntThopHdr.TABLE_NAME)
@Data
public class HhDxKhLcntThopHdr implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_DX_KHLCNT_THOP_HDR";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DX_KHLCNT_THOP_HDR_SEQ")
	@SequenceGenerator(sequenceName = "HH_DX_KHLCNT_THOP_HDR_SEQ", allocationSize = 1, name = "HH_DX_KHLCNT_THOP_HDR_SEQ")
	private Long id;

	String loaiVthh;
	String hthucLcnt;
	String pthucLcnt;
	String loaiHdong;
	String nguonVon;

	@Temporal(TemporalType.DATE)
	Date tuTgianTbao;
	@Temporal(TemporalType.DATE)
	Date denTgianTbao;
	@Temporal(TemporalType.DATE)
	Date tuTgianDthau;
	@Temporal(TemporalType.DATE)
	Date denTgianDthau;
	@Temporal(TemporalType.DATE)
	Date tuTgianNhang;
	@Temporal(TemporalType.DATE)
	Date denTgianNhang;
	@Temporal(TemporalType.DATE)
	Date tuTgianMthau;
	@Temporal(TemporalType.DATE)
	Date denTgianMthau;

	Date ngayTao;
	String nguoiTao;
	String veViec;
	String namKhoach;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference
	private List<HhDxKhLcntThopDtl> children = new ArrayList<>();

	public void setChildren(List<HhDxKhLcntThopDtl> children) {
		this.children.clear();
		for (HhDxKhLcntThopDtl child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(HhDxKhLcntThopDtl child) {
		child.setParent(this);
		this.children.add(child);
	}
}
