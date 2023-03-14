package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.Transient;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class HhDxuatKhMttSlddDtlReq {

   private Long id;

   private String maDiemKho;

   private String diaDiemNhap;

   private Long idDtl;

   private BigDecimal soLuong;

   private BigDecimal donGia;

   private BigDecimal donGiaVat;
}
