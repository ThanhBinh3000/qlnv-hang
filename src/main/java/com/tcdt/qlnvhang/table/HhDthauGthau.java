package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
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

	@Temporal(TemporalType.DATE)
	Date ngayQdPdKhlcnt;

	String tenGthau;
	String loaiVthh;
	@Transient
	String tenVthh;
	String cloaiVthh;
	@Transient
	String tenCloaiVthh;
	String dviTinh;
//	String maHhoa;
	Long donGia;
	BigDecimal soLuong;
	BigDecimal tongTien;
	String nguonVon;
	String hthucLcnt;
	String pthucLcnt;
	String loaiHdong;
	Integer tgianThienHd;

	@Temporal(TemporalType.DATE)
	Date tgianNhang;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianMthau;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
	Date tgianDthau;

	@Temporal(TemporalType.DATE)
	Date ngayKyBban;
	Long idNhaThau;
	BigDecimal donGiaTrcVat;
	Integer vat;

	Long idDtHdr;

	//	Date tuTgianLcnt;
//	Date denTgianLcnt;
//	String hthucHdong;
//	Date tgianThHienHd;
	String ghiChu;
//	Date tgianMoHsdxtc;
//	String soQd;
//	Date ngayKy;


	@Transient
	private List<HhDthauNthauDuthau> nthauDuThauList = new ArrayList<>();

	@Transient
	private List<HhDthauHsoKthuat> hsoKthuatList = new ArrayList<>();

	@Transient
	private List<HhDthauHsoTchinh> hsoTchinhLinh = new ArrayList<>();

	@Transient
	private List<HhDthauDdiemNhap> diaDiemNhapList = new ArrayList<>();
//
//	@Transient
//	private HhDthauTthaoHdong tthaoHdong;

	@Transient
	private HhDthauKquaLcnt kquaLcnt;

}
