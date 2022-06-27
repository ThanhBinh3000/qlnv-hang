package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinGoiThau;

import lombok.Data;

@Entity
@Table(name = HhDthauGthau.TABLE_NAME)
@Data
public class HhDthauGthau implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_DTHAU_GTHAU";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhDthauGthau.TABLE_NAME + "_SEQ")
	@SequenceGenerator(sequenceName = HhDthauGthau.TABLE_NAME
			+ "_SEQ", allocationSize = 1, name = HhDthauGthau.TABLE_NAME + "_SEQ")
	private Long id;

	Long idGoiThau;
	String soQdPdKhlcnt;
	String ngayQdPdKhlcnt;
	String tenGthau;
	String loaiVthh;
	@Transient
	String tenVthh;
	String cloaiVthh;
	@Transient
	String tenCloaiVthh;
	String dviTinh;
	String maHhoa;
	BigDecimal soLuong;
	BigDecimal giaGthau;
	String nguonVon;
	String hthucLcnt;
	String pthucLcnt;
	Date tuTgianLcnt;
	Date denTgianLcnt;
	String hthucHdong;
	Date tgianThHienHd;
	String ghiChu;
	Date tgianMoHsdxtc;
	String soQd;
	Date ngayKy;
	String nhaThauTthao;
	Integer vat;
	Long donGia;
	Integer tgianThienHd;

	@Transient
	private List<HhDthauNthauDuthau> nthauDuThauList = new ArrayList<>();

	@Transient
	private List<HhDthauHsoKthuat> hsoKthuatList = new ArrayList<>();

	@Transient
	private List<HhDthauHsoTchinh> hsoTchinhLinh = new ArrayList<>();

	@Transient
	private HhDthauTthaoHdong tthaoHdong;

	@Transient
	private HhDthauKquaLcnt kquaLcnt;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "dataId")
	@JsonManagedReference
	@Where(clause = "data_type='" + HhDthauGthau.TABLE_NAME + "'")
	private List<FileDKemJoinGoiThau> fileDinhKems = new ArrayList<>();

	public void setFileDinhKems(List<FileDKemJoinGoiThau> children5) {
		this.fileDinhKems.clear();
		for (FileDKemJoinGoiThau child5 : children5) {
			child5.setParent(this);
		}
		this.fileDinhKems.addAll(children5);
	}

}
