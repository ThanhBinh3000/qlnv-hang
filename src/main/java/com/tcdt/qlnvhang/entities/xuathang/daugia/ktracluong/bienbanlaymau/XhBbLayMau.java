package com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = XhBbLayMau.TABLE_NAME)
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class XhBbLayMau extends TrangThaiBaseEntity implements Serializable {
	public static final String TABLE_NAME = "XH_BB_LAY_MAU";
	private static final long serialVersionUID = -7021660593183667859L;

	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_BB_LAY_MAU_SEQ")
//	@SequenceGenerator(sequenceName = "XH_BB_LAY_MAU_SEQ", allocationSize = 1, name = "XH_BB_LAY_MAU_SEQ")
	@Column(name = "ID")
	private Long id;

	private Integer nam;

	private String maDvi;

	private Long idQd;

	private String soQd;

	private String soHd;


	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date ngayHd;

	private Long idKtv;

	private String soBienBan;


	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date ngayLayMau;

	private String dviKnghiem;

	private String ddiemLayMau;

	private String loaiVthh;

	private String cloaiVthh;

	private String moTaHangHoa;

	private Long idDdiemXh;

	private String maDiemKho;

	private String maNhaKho;

	private String maNganKho;

	private String maLoKho;

	private BigDecimal soLuong;

	private String ppLayMau;

	private String chiTieuKiemTra;

	private Integer ketQuaNiemPhong;

	// Transient
	private String tenLoaiVthh;

	private String tenCloaiVthh;

	private String tenDiemKho;

	private String tenNhaKho;

	private String tenNganKho;

	private String tenLoKho;

	@Transient
	private List<FileDinhKem> fileDinhKems = new ArrayList<>();

	@Transient
	private List<XhBbLayMauCt> children = new ArrayList<>();


}
