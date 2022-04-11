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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinPhuLuc;
import com.tcdt.qlnvhang.util.Contains;

import lombok.Data;

@Entity
@Table(name = HhPhuLucHd.TABLE_NAME)
@Data
public class HhPhuLucHd implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_PHU_LUC_HD";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhPhuLucHd.TABLE_NAME + "_SEQ")
	@SequenceGenerator(sequenceName = HhPhuLucHd.TABLE_NAME + "_SEQ", allocationSize = 1, name = HhPhuLucHd.TABLE_NAME
			+ "_SEQ")
	private Long id;

	String soPluc;
	Date ngayKy;
	Date ngayHluc;
	String veViec;
	Date tuNgayTrcDc;
	Date tuNgaySauDc;
	long soNgayTrcDc;
	long soNgaySauDc;
	Date denNgayTrcDc;
	Date denNgaySauDc;
	String soHd;
	String noiDungDc;
	String trangThai;
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;
	String ldoTuchoi;
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	Date ngayPduyet;
	String nguoiPduyet;
	String loaiVthh;
	String maDvi;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_hdr")
	@JsonManagedReference
	@Where(clause = "type='" + Contains.PHU_LUC + "'")
	private List<HhPhuLucHdDtl> children = new ArrayList<>();

	public void setChildren(List<HhPhuLucHdDtl> children) {
		this.children.clear();
		for (HhPhuLucHdDtl child : children) {
			child.setParent(this);
		}
		this.children.addAll(children);
	}

	public void addChild(HhPhuLucHdDtl child) {
		child.setParent(this);
		this.children.add(child);
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "dataId")
	@JsonManagedReference
	@Where(clause = "data_type='" + HhPhuLucHd.TABLE_NAME + "'")
	private List<FileDKemJoinPhuLuc> children1 = new ArrayList<>();

	public void setChildren1(List<FileDKemJoinPhuLuc> children1) {
		this.children1.clear();
		for (FileDKemJoinPhuLuc child1 : children1) {
			child1.setParent(this);
		}
		this.children1.addAll(children1);
	}

	public void addChild1(FileDKemJoinPhuLuc child1) {
		child1.setParent(this);
		this.children1.add(child1);
	}

}
