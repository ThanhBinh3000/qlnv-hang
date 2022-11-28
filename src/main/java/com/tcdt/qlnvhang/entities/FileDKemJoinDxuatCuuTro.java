package com.tcdt.qlnvhang.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.table.XhDxCuuTroHdr;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "FILE_DINH_KEM")
@Data
public class FileDKemJoinDxuatCuuTro implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FILE_DINH_KEM_SEQ")
	@SequenceGenerator(sequenceName = "FILE_DINH_KEM_SEQ", allocationSize = 1, name = "FILE_DINH_KEM_SEQ")
	Long id;
	String fileName;
	String fileSize;
	String fileUrl;
	String fileType;
	String dataType;
	String noiDung;
	@JsonFormat(pattern="dd/MM/yyyy HH:mm:ss")
	Date createDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dataId")
	@JsonBackReference
	private XhDxCuuTroHdr parent;
}