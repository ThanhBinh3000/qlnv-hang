package com.tcdt.qlnvhang.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tcdt.qlnvhang.entities.UserActivity;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKho;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoCt;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.phieuxuatkho.XhPhieuXkhoBtt;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgPhieuXuatKho;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoCtRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhPhieuNhapKhoCtRepository;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.UserActivityService;
import com.tcdt.qlnvhang.service.feign.LuuKhoClient;
import com.tcdt.qlnvhang.table.PhieuNhapXuatHistory;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuXuatKhoHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoCt;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuXuatKho;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.UserUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Aspect
@Component
public class LoggingAspect {

  private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
  private final static String SYSTEM = "hang";

  @Autowired
  private Gson gson;
  @Autowired
  private UserActivityService userActivityService;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private LuuKhoClient luuKhoClient;
  @Autowired
  private NhPhieuNhapKhoCtRepository nhPhieuNhapKhoCtRepository;
  @Autowired
  private HhPhieuNhapKhoCtRepository hhPhieuNhapKhoCtRepository;


  @Pointcut("within(com.tcdt.qlnvhang..*) && bean(*Controller))")
  public void v3Controller() {
  }

  @Pointcut("execution(* com.tcdt.qlnvhang.controller.nhaphang.dauthau.nhapkho.NhPhieuNhapKhoController.updateStatus(..)) ||" +
      "execution(* com.tcdt.qlnvhang.controller.nhaphangtheoptmuatt.HhPhieuNhapKhoControler.updateStatusUbtvqh(..)) ||" +
      "execution(* com.tcdt.qlnvhang.controller.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuXuatKhoController.updateStatus(..)) ||" +
      "execution(* com.tcdt.qlnvhang.controller.xuathang.daugia.xuatkho.XhDgPhieuXuatKhoController.updateStatus(..)) ||" +
      "execution(* com.tcdt.qlnvhang.controller.xuathang.bantructiep.xuatkho.phieuxuatkho.XhPhieuXkhoBttControler.updateStatus(..)) ||" +
      "execution(* com.tcdt.qlnvhang.controller.dieuchuyennoibo.DcnbPhieuXuatKhoController.updateStatus(..)) || " +
      "execution(* com.tcdt.qlnvhang.controller.dieuchuyennoibo.DcnbPhieuNhapKhoController.updateStatus(..))")
  public void phieuNhapXuatPointCut() {
  }

  @Before("v3Controller()")
  public void logBefore(JoinPoint joinPoint) {
    try {
      HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
      String userAgent = request.getHeader("User-Agent");
      if (ObjectUtils.isEmpty(userAgent)) {
        userAgent = request.getHeader("user-agent");
      }
      Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(userAgent);
      if (m.find()) {
        userAgent = m.group(1);
      }
      if (!String.class.equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass())) {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserActivity entity = new UserActivity();
        entity.setIp(UserUtils.getClientIpAddress(request));
        entity.setRequestMethod(request.getMethod());
        entity.setRequestUrl(request.getRequestURI());
        entity.setUserId(user.getUser().getId());
        entity.setSystem(SYSTEM);
        entity.setUserAgent(userAgent);
        entity.setRequestBody(getBody(joinPoint.getArgs()));
        entity.setUserName(user.getUser().getUsername());
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap != null && !parameterMap.isEmpty()) {
          entity.setRequestParameter(gson.toJson(parameterMap));
        }
        userActivityService.log(entity);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  @AfterReturning(value = "phieuNhapXuatPointCut()", returning = "result")
  public void phieuNhapXuatAfterLog(JoinPoint joinPoint, ResponseEntity<BaseResponse> result) {
    try {
      if (joinPoint.getTarget().toString().contains("NhPhieuNhapKhoController")) {
        if (result != null && result.getBody().getMsg().equals(EnumResponse.RESP_SUCC.getDescription())) {
          NhPhieuNhapKho rowData = objectMapper.convertValue(result.getBody().getData(), NhPhieuNhapKho.class);
          if (rowData.getTrangThai().equals(TrangThaiAllEnum.DA_DUYET_LDCC.getId())) {
            NhPhieuNhapKhoCt nhPhieuNhapKhoCt = nhPhieuNhapKhoCtRepository.findAllByIdPhieuNkHdr(rowData.getId()).get(0);
            PhieuNhapXuatHistory phieuNhapXuatHistory = new PhieuNhapXuatHistory();
            phieuNhapXuatHistory.setMaKho(rowData.getMaLoKho() == null ? rowData.getMaNganKho() : rowData.getMaLoKho());
            phieuNhapXuatHistory.setSoLuong(DataUtils.safeToLong(nhPhieuNhapKhoCt.getSoLuongThucNhap()));
            phieuNhapXuatHistory.setDonGia(DataUtils.safeToLong(nhPhieuNhapKhoCt.getDonGia()));
            phieuNhapXuatHistory.setThanhTien(DataUtils.safeToLong(nhPhieuNhapKhoCt.getThanhTien()));
            phieuNhapXuatHistory.setSoLuongChungTu(nhPhieuNhapKhoCt.getSoLuongChungTu());
            phieuNhapXuatHistory.setIdPhieu(rowData.getId());
            phieuNhapXuatHistory.setSoPhieu(rowData.getSoPhieuNhapKho());
            phieuNhapXuatHistory.setLoaiNhapXuat(1);
            phieuNhapXuatHistory.setLoaiVthh(rowData.getLoaiVthh());
            phieuNhapXuatHistory.setCloaiVthh(rowData.getCloaiVthh());
            phieuNhapXuatHistory.setNgayDuyet(DataUtils.convertToLocalDate(rowData.getNgayPduyet()));
            phieuNhapXuatHistory.setKieu("NHAP_DAU_THAU");
            phieuNhapXuatHistory.setBang(rowData.TABLE_NAME);
            phieuNhapXuatHistory.setNamNhap(rowData.getNam());
            phieuNhapXuatHistory.setNgayTao(LocalDate.now());

            luuKhoClient.synchronizeData(phieuNhapXuatHistory);
            logger.info("Cập nhật kho theo Phiếu nhập kho NhPhieuNhapKhoController {}", rowData);
          }
        }
      } else if (joinPoint.getTarget().toString().contains("HhPhieuNhapKhoControler")) {
        if (result != null && result.getBody().getMsg().equals(EnumResponse.RESP_SUCC.getDescription())) {
          HhPhieuNhapKhoHdr rowData = objectMapper.convertValue(result.getBody().getData(), HhPhieuNhapKhoHdr.class);
          if (rowData.getTrangThai().equals(TrangThaiAllEnum.DA_DUYET_LDCC.getId())) {
            HhPhieuNhapKhoCt dtl = hhPhieuNhapKhoCtRepository.findAllByIdHdr(rowData.getId()).get(0);
            PhieuNhapXuatHistory phieuNhapXuatHistory = new PhieuNhapXuatHistory();
            phieuNhapXuatHistory.setMaKho(rowData.getMaLoKho() == null ? rowData.getMaNganKho() : rowData.getMaLoKho());
            phieuNhapXuatHistory.setSoLuong(DataUtils.safeToLong(dtl.getSoLuongThucNhap()));
            phieuNhapXuatHistory.setDonGia(DataUtils.safeToLong(dtl.getDonGia()));
            phieuNhapXuatHistory.setThanhTien(DataUtils.safeToLong(dtl.getDonGia()) * DataUtils.safeToLong(dtl.getSoLuongThucNhap()));
            phieuNhapXuatHistory.setSoLuongChungTu(dtl.getSoLuongChungTu());
            phieuNhapXuatHistory.setIdPhieu(rowData.getId());
            phieuNhapXuatHistory.setSoPhieu(rowData.getSoPhieuNhapKho());
            phieuNhapXuatHistory.setLoaiNhapXuat(1);
            phieuNhapXuatHistory.setLoaiVthh(rowData.getLoaiVthh());
            phieuNhapXuatHistory.setCloaiVthh(rowData.getCloaiVthh());
            phieuNhapXuatHistory.setNgayDuyet(DataUtils.convertToLocalDate(rowData.getNgayPduyet()));
            phieuNhapXuatHistory.setKieu("NHAP_TRUC_TIEP");
            phieuNhapXuatHistory.setBang(rowData.TABLE_NAME);
            phieuNhapXuatHistory.setNamNhap(rowData.getNamKh());
            phieuNhapXuatHistory.setNgayTao(LocalDate.now());

            luuKhoClient.synchronizeData(phieuNhapXuatHistory);
            logger.info("Cập nhật kho theo Phiếu nhập kho HhPhieuNhapKhoControler {}", rowData);
          }
        }
      } else if (joinPoint.getTarget().toString().contains("XhCtvtPhieuXuatKhoController")) {
        if (result != null && result.getBody().getMsg().equals(EnumResponse.RESP_SUCC.getDescription())) {
          XhCtvtPhieuXuatKho rowData = objectMapper.convertValue(result.getBody().getData(), XhCtvtPhieuXuatKho.class);
          if (rowData.getTrangThai().equals(TrangThaiAllEnum.DA_DUYET_LDCC.getId())) {
            PhieuNhapXuatHistory phieuNhapXuatHistory = new PhieuNhapXuatHistory();
            phieuNhapXuatHistory.setMaKho(rowData.getMaLoKho() == null ? rowData.getMaNganKho() : rowData.getMaLoKho());
            phieuNhapXuatHistory.setSoLuong(DataUtils.safeToLong(rowData.getThucXuat()));
            phieuNhapXuatHistory.setDonGia(DataUtils.safeToLong(rowData.getDonGia()));
            phieuNhapXuatHistory.setThanhTien(DataUtils.safeToLong(rowData.getThanhTien()));
            phieuNhapXuatHistory.setSoLuongChungTu(BigDecimal.ZERO);
            phieuNhapXuatHistory.setIdPhieu(rowData.getId());
            phieuNhapXuatHistory.setSoPhieu(rowData.getSoPhieuXuatKho());
            phieuNhapXuatHistory.setLoaiNhapXuat(-1);
            phieuNhapXuatHistory.setLoaiVthh(rowData.getLoaiVthh());
            phieuNhapXuatHistory.setCloaiVthh(rowData.getCloaiVthh());
            phieuNhapXuatHistory.setNgayDuyet(rowData.getNgayPduyet());
            phieuNhapXuatHistory.setKieu("CUU_TRO_VIEN_TRO");
            phieuNhapXuatHistory.setBang(rowData.TABLE_NAME);
            phieuNhapXuatHistory.setNamNhap(rowData.getNam());
            phieuNhapXuatHistory.setNgayTao(LocalDate.now());
            luuKhoClient.synchronizeData(phieuNhapXuatHistory);
            logger.info("Cập nhật kho theo Phiếu nhập kho XhCtvtPhieuXuatKhoController {}", rowData);
          }
        }
      } else if (joinPoint.getTarget().toString().contains("XhDgPhieuXuatKhoController")) {
        if (result != null && result.getBody().getMsg().equals(EnumResponse.RESP_SUCC.getDescription())) {
          XhDgPhieuXuatKho rowData = objectMapper.convertValue(result.getBody().getData(), XhDgPhieuXuatKho.class);
          if (rowData.getTrangThai().equals(TrangThaiAllEnum.DA_DUYET_LDCC.getId())) {
            PhieuNhapXuatHistory phieuNhapXuatHistory = new PhieuNhapXuatHistory();
            phieuNhapXuatHistory.setMaKho(rowData.getMaLoKho() == null ? rowData.getMaNganKho() : rowData.getMaLoKho());
            phieuNhapXuatHistory.setSoLuong(DataUtils.safeToLong(rowData.getThucXuat()));
            phieuNhapXuatHistory.setDonGia(DataUtils.safeToLong(rowData.getDonGia()));
            phieuNhapXuatHistory.setThanhTien(DataUtils.safeToLong(rowData.getDonGia()) * DataUtils.safeToLong(rowData.getThucXuat()));
            phieuNhapXuatHistory.setSoLuongChungTu(BigDecimal.ZERO);
            phieuNhapXuatHistory.setIdPhieu(rowData.getId());
            phieuNhapXuatHistory.setSoPhieu(rowData.getSoPhieuXuatKho());
            phieuNhapXuatHistory.setLoaiNhapXuat(-1);
            phieuNhapXuatHistory.setLoaiVthh(rowData.getLoaiVthh());
            phieuNhapXuatHistory.setCloaiVthh(rowData.getCloaiVthh());
            phieuNhapXuatHistory.setNgayDuyet(rowData.getNgayPduyet());
            phieuNhapXuatHistory.setKieu("BAN_DAU_GIA");
            phieuNhapXuatHistory.setBang(rowData.TABLE_NAME);
            phieuNhapXuatHistory.setNamNhap(rowData.getNam());
            phieuNhapXuatHistory.setNgayTao(LocalDate.now());

            luuKhoClient.synchronizeData(phieuNhapXuatHistory);
            logger.info("Cập nhật kho theo Phiếu nhập kho XhDgPhieuXuatKhoController {}", rowData);
          }
        }
      } else if (joinPoint.getTarget().toString().contains("XhPhieuXkhoBttControler")) {
        if (result != null && result.getBody().getMsg().equals(EnumResponse.RESP_SUCC.getDescription())) {
          XhPhieuXkhoBtt rowData = objectMapper.convertValue(result.getBody().getData(), XhPhieuXkhoBtt.class);
          if (rowData.getTrangThai().equals(TrangThaiAllEnum.DA_DUYET_LDCC.getId())) {
            PhieuNhapXuatHistory phieuNhapXuatHistory = new PhieuNhapXuatHistory();
            phieuNhapXuatHistory.setMaKho(rowData.getMaLoKho() == null ? rowData.getMaNganKho() : rowData.getMaLoKho());
            phieuNhapXuatHistory.setSoLuong(DataUtils.safeToLong(rowData.getSoLuongThucXuat()));
            phieuNhapXuatHistory.setDonGia(DataUtils.safeToLong(rowData.getDonGia()));
            phieuNhapXuatHistory.setThanhTien(DataUtils.safeToLong(rowData.getDonGia()) * DataUtils.safeToLong(rowData.getSoLuongThucXuat()));
            phieuNhapXuatHistory.setSoLuongChungTu(rowData.getSoLuongChungTu());
            phieuNhapXuatHistory.setIdPhieu(rowData.getId());
            phieuNhapXuatHistory.setSoPhieu(rowData.getSoPhieu());
            phieuNhapXuatHistory.setLoaiNhapXuat(-1);
            phieuNhapXuatHistory.setLoaiVthh(rowData.getLoaiVthh());
            phieuNhapXuatHistory.setCloaiVthh(rowData.getCloaiVthh());
            phieuNhapXuatHistory.setNgayDuyet(rowData.getNgayPduyet());
            phieuNhapXuatHistory.setKieu("BAN_TRUC_TIEP");
            phieuNhapXuatHistory.setBang(rowData.TABLE_NAME);
            phieuNhapXuatHistory.setNamNhap(rowData.getNamKh());
            phieuNhapXuatHistory.setNgayTao(LocalDate.now());

            luuKhoClient.synchronizeData(phieuNhapXuatHistory);
            logger.info("Cập nhật kho theo Phiếu nhập kho XhPhieuXkhoBttControler {}", rowData);
          }
        }
      } else if (joinPoint.getTarget().toString().contains("DcnbPhieuXuatKhoController")) {
        if (result != null && result.getBody().getMsg().equals(EnumResponse.RESP_SUCC.getDescription())) {
          DcnbPhieuXuatKhoHdr rowData = objectMapper.convertValue(result.getBody().getData(), DcnbPhieuXuatKhoHdr.class);
          if (rowData.getTrangThai().equals(TrangThaiAllEnum.DA_DUYET_LDCC.getId())) {
            PhieuNhapXuatHistory phieuNhapXuatHistory = new PhieuNhapXuatHistory();
            phieuNhapXuatHistory.setMaKho(rowData.getMaLoKho() == null ? rowData.getMaNganKho() : rowData.getMaLoKho());
            phieuNhapXuatHistory.setSoLuong(DataUtils.safeToLong(rowData.getTongSoLuong()));
            phieuNhapXuatHistory.setDonGia(DataUtils.safeToLong(rowData.getDonGia()));
            phieuNhapXuatHistory.setThanhTien(DataUtils.safeToLong(rowData.getThanhTien()));
            phieuNhapXuatHistory.setSoLuongChungTu(BigDecimal.ZERO);
            phieuNhapXuatHistory.setIdPhieu(rowData.getId());
            phieuNhapXuatHistory.setSoPhieu(rowData.getSoPhieuXuatKho());
            phieuNhapXuatHistory.setLoaiNhapXuat(-1);
            phieuNhapXuatHistory.setLoaiVthh(rowData.getLoaiVthh());
            phieuNhapXuatHistory.setCloaiVthh(rowData.getCloaiVthh());
            phieuNhapXuatHistory.setNgayDuyet(rowData.getNgayPduyet());
            phieuNhapXuatHistory.setKieu("XUAT_DIEU_CHUYEN");
            phieuNhapXuatHistory.setBang(rowData.TABLE_NAME);
            phieuNhapXuatHistory.setNamNhap(rowData.getNam());
            phieuNhapXuatHistory.setNgayTao(LocalDate.now());
            luuKhoClient.synchronizeData(phieuNhapXuatHistory);
            logger.info("Cập nhật kho theo Phiếu nhập kho DcnbPhieuXuatKhoController {}", rowData);
          }
        } else if (joinPoint.getTarget().toString().contains("DcnbPhieuNhapKhoController")) {
          if (result != null && result.getBody().getMsg().equals(EnumResponse.RESP_SUCC.getDescription())) {
            DcnbPhieuNhapKhoHdr rowData = objectMapper.convertValue(result.getBody().getData(), DcnbPhieuNhapKhoHdr.class);
            if (rowData.getTrangThai().equals(TrangThaiAllEnum.DA_DUYET_LDCC.getId())) {
              PhieuNhapXuatHistory phieuNhapXuatHistory = new PhieuNhapXuatHistory();
              phieuNhapXuatHistory.setMaKho(rowData.getMaLoKho() == null ? rowData.getMaNganKho() : rowData.getMaLoKho());
              phieuNhapXuatHistory.setSoLuong(DataUtils.safeToLong(rowData.getTongSoLuong()));
              phieuNhapXuatHistory.setDonGia(0L);
              phieuNhapXuatHistory.setThanhTien(0L);
              phieuNhapXuatHistory.setSoLuongChungTu(BigDecimal.ZERO);
              phieuNhapXuatHistory.setIdPhieu(rowData.getId());
              phieuNhapXuatHistory.setSoPhieu(rowData.getSoPhieuNhapKho());
              phieuNhapXuatHistory.setLoaiNhapXuat(1);
              phieuNhapXuatHistory.setLoaiVthh(rowData.getLoaiVthh());
              phieuNhapXuatHistory.setCloaiVthh(rowData.getCloaiVthh());
              phieuNhapXuatHistory.setNgayDuyet(rowData.getNgayPDuyet());
              phieuNhapXuatHistory.setKieu("NHAP_DIEU_CHUYEN");
              phieuNhapXuatHistory.setBang(rowData.TABLE_NAME);
              phieuNhapXuatHistory.setNamNhap(rowData.getNam());
              phieuNhapXuatHistory.setNgayTao(LocalDate.now());

              luuKhoClient.synchronizeData(phieuNhapXuatHistory);
              logger.info("Cập nhật kho theo Phiếu nhập kho DcnbPhieuXuatKhoController {}", rowData);
            }
          }
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String getBody(Object[] args) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(args);
    } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
      return null;
    }
  }

  public <C> C test(Object data, Class<C> clazz) {
    return objectMapper.convertValue(data, clazz);
  }
}
