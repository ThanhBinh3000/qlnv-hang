package com.tcdt.qlnvhang.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.hosokythuat.NhHoSoKyThuatCtNk;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "FILE_DINH_KEM")
@Data
public class FileDKemJoinNhHoSoKyThuatCtNk implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FILE_DINH_KEM_SEQ")
	@SequenceGenerator(sequenceName = "FILE_DINH_KEM_SEQ", allocationSize = 1, name = "FILE_DINH_KEM_SEQ")
	Long id;
	String fileName;
	String noiDung;
	String fileSize;
	String fileUrl;
	String fileType;
	String dataType;
	Date createDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dataId")
	@JsonBackReference
	private NhHoSoKyThuatCtNk parent;
}
