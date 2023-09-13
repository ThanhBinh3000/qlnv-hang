package com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.nvnhap;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinQdNhapHangKhac;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuknghiemcl.PhieuKnghiemCluongHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbangiaonhan.NhBbGiaoNhanVt;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.kiemtracl.bblaymaubangiaomau.BienBanLayMauKhac;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.kiemtracl.bblaymaubangiaomau.BienBanLayMauKhacCt;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.qdpdnk.HhQdPdNhapKhacDtl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = HhQdGiaoNvuNhapHangKhacHdr.TABLE_NAME)
@Data
public class HhQdGiaoNvuNhapHangKhacHdr implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_QD_GIAO_NVU_NHAPXUAT_KHAC";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_GIAO_NVU_NHAP_KHAC_SEQ")
	@SequenceGenerator(sequenceName = "HH_QD_GIAO_NVU_NHAP_KHAC_SEQ", allocationSize = 1, name = "HH_QD_GIAO_NVU_NHAP_KHAC_SEQ")
	private Long id;
	private Long idQdPdNk;

	String soQd;
	String soQdPd;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQd;

	String maDvi;

	String trangThai;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayTao;
	String nguoiTao;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngaySua;
	String nguoiSua;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;

	String ldoTuchoi;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayPduyet;
	String nguoiPduyet;

	String loaiVthh;

	String cloaiVthh;
	String dvt;

	String trichYeu;

	Integer nam;

	Long tongSlNhap;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianNkMnhat;

	@Transient
	String tenDvi;

	@Transient
	String tenLoaiQd;

	@Transient
	String tenTrangThai;

	@Transient
	String tenTrangThaiNh;

	@Transient
	String tenLoaiVthh;
	@Transient
	String tenLoaiHangHoa;

	@Transient
	String tenCloaiVthh;
	String loaiHinhNx;
	@Transient
	String tenLoaiHinhNx;
	@Transient
	String tenKieuNx;
	String kieuNx;

	@Transient
	private List<HhQdPdNhapKhacDtl> dtlList = new ArrayList<>();

	@Transient
	private BienBanLayMauKhac bienBanLayMau;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "dataId")
	@JsonManagedReference
	@Where(clause = "data_type='" + HhQdGiaoNvuNhapHangKhacHdr.TABLE_NAME + "'")
	private List<FileDKemJoinQdNhapHangKhac> children2 = new ArrayList<>();

	public void setChildren2(List<FileDKemJoinQdNhapHangKhac> children2) {
		this.children2.clear();
		for (FileDKemJoinQdNhapHangKhac child2 : children2) {
			child2.setParent(this);
		}
		this.children2.addAll(children2);
	}

	public void addChild2(FileDKemJoinQdNhapHangKhac child2) {
		child2.setParent(this);
		this.children2.add(child2);
	}

	@Transient
	List<PhieuKnghiemCluongHang> listPhieuKiemNghiemCl;

	@Transient
	List<NhBbGiaoNhanVt> listBienBanGiaoNhan;
	@Transient
	private List<FileDinhKem> fileDinhKems;
	@Transient
	private List<FileDinhKem> fileCanCu;
	// Print preview
	@Transient
	private String ngay;
	@Transient
	private String tenBaoCao;
	@Transient
	private String thang;
	@Transient
	private String canCuPhapLy;

	public String getNgay() {
		return Objects.isNull(this.getNgayQd()) ? null : String.valueOf(this.getNgayQd().getDate());
	}
	public String getThang() {
		return Objects.isNull(this.getNgayQd()) ? null : String.valueOf(this.getNgayQd().getMonth()+1);
	}

	public String getCanCuPhapLy() {
		if(fileCanCu.isEmpty()){
			return null;
		}else{
			List<String> collect = fileCanCu.stream().map(FileDinhKem::getFileName).collect(Collectors.toList());
			List<String> newList = new ArrayList<String>(new HashSet<String>(collect));
			canCuPhapLy = String.join("-",newList);
		}
		return canCuPhapLy;
	}

}
