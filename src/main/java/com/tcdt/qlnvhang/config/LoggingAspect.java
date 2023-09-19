package com.tcdt.qlnvhang.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tcdt.qlnvhang.entities.UserActivity;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKho;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoCt;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoCtRepository;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.UserActivityService;
import com.tcdt.qlnvhang.service.feign.LuuKhoClient;
import com.tcdt.qlnvhang.table.PhieuNhapXuatHistory;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuXuatKhoHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuXuatKho;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.UserUtils;
import org.apache.poi.ss.formula.functions.T;
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


  @Pointcut("within(com.tcdt.qlnvhang..*) && bean(*Controller))")
  public void v3Controller() {
  }

  @Pointcut("execution(* com.tcdt.qlnvhang.controller.nhaphang.dauthau.nhapkho.NhPhieuNhapKhoController.updateStatus(..)) ||" +
      "execution(* com.tcdt.qlnvhang.controller.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuXuatKhoController.updateStatus(..)) ||" +
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
            phieuNhapXuatHistory.setSoLuong(nhPhieuNhapKhoCt.getSoLuongThucNhap());
            phieuNhapXuatHistory.setSoPhieu(rowData.getSoPhieuNhapKho());
            phieuNhapXuatHistory.setSoLuongChungTu(nhPhieuNhapKhoCt.getSoLuongChungTu());
            phieuNhapXuatHistory.setLoaiVthh(rowData.getLoaiVthh());
            phieuNhapXuatHistory.setCloaiVthh(rowData.getCloaiVthh());
            phieuNhapXuatHistory.setMaKho(rowData.getMaLoKho());
            phieuNhapXuatHistory.setNgayDuyet(DataUtils.convertToLocalDate(rowData.getNgayPduyet()));
            phieuNhapXuatHistory.setLoaiNhapXuat(1);//fix tam 1 la nhap -1 la xuat
            phieuNhapXuatHistory.setKieu("NHAP_XUAT");//nhap xuat hoac khoi tao so du dau ky
            luuKhoClient.synchronizeData(phieuNhapXuatHistory);
            logger.info("Cập nhật kho theo Phiếu nhập kho NhPhieuNhapKhoController {}", rowData);
          }
        }
      } else if (joinPoint.getTarget().toString().contains("XhCtvtPhieuXuatKhoController")) {
        if (result != null && result.getBody().getMsg().equals(EnumResponse.RESP_SUCC.getDescription())) {
          XhCtvtPhieuXuatKho rowData = objectMapper.convertValue(result.getBody().getData(), XhCtvtPhieuXuatKho.class);
          if (rowData.getTrangThai().equals(TrangThaiAllEnum.DA_DUYET_LDCC.getId())) {
            PhieuNhapXuatHistory phieuNhapXuatHistory = new PhieuNhapXuatHistory();
            phieuNhapXuatHistory.setSoLuong(rowData.getThucXuat());
            phieuNhapXuatHistory.setSoPhieu(rowData.getSoPhieuXuatKho());
            phieuNhapXuatHistory.setSoLuongChungTu(BigDecimal.valueOf(Double.valueOf(rowData.getTheoChungTu())));
            phieuNhapXuatHistory.setLoaiVthh(rowData.getLoaiVthh());
            phieuNhapXuatHistory.setCloaiVthh(rowData.getCloaiVthh());
            phieuNhapXuatHistory.setMaKho(rowData.getMaLoKho());
            phieuNhapXuatHistory.setNgayDuyet(rowData.getNgayPduyet());
            phieuNhapXuatHistory.setLoaiNhapXuat(-1);//fix tam 1 la nhap -1 la xuat
            phieuNhapXuatHistory.setKieu("NHAP_XUAT");//nhap xuat hoac khoi tao so du dau ky
            luuKhoClient.synchronizeData(phieuNhapXuatHistory);
            logger.info("Cập nhật kho theo Phiếu nhập kho XhCtvtPhieuXuatKhoController {}", rowData);
          }
        }
      }else if (joinPoint.getTarget().toString().contains("DcnbPhieuXuatKhoController")) {
        if (result != null && result.getBody().getMsg().equals(EnumResponse.RESP_SUCC.getDescription())) {
          DcnbPhieuXuatKhoHdr rowData = objectMapper.convertValue(result.getBody().getData(), DcnbPhieuXuatKhoHdr.class);
          if (rowData.getTrangThai().equals(TrangThaiAllEnum.DA_DUYET_LDCC.getId())) {
            PhieuNhapXuatHistory phieuNhapXuatHistory = new PhieuNhapXuatHistory();
            phieuNhapXuatHistory.setSoLuong(rowData.getTongSoLuong()); //ThucXuat
            phieuNhapXuatHistory.setSoPhieu(rowData.getSoPhieuXuatKho());
            phieuNhapXuatHistory.setLoaiVthh(rowData.getLoaiVthh());
            phieuNhapXuatHistory.setCloaiVthh(rowData.getCloaiVthh());
            phieuNhapXuatHistory.setMaKho(rowData.getMaLoKho() == null ? rowData.getMaNganKho() : rowData.getMaLoKho());
            phieuNhapXuatHistory.setNgayDuyet(rowData.getNgayPduyet());
            phieuNhapXuatHistory.setLoaiNhapXuat(-1);//fix tam 1 la nhap -1 la xuat
            phieuNhapXuatHistory.setKieu("NHAP_XUAT");//nhap xuat hoac khoi tao so du dau ky
            luuKhoClient.synchronizeData(phieuNhapXuatHistory);
            logger.info("Cập nhật kho theo Phiếu nhập kho DcnbPhieuXuatKhoController {}", rowData);
          }
        }
        else if (joinPoint.getTarget().toString().contains("DcnbPhieuNhapKhoController")) {
          if (result != null && result.getBody().getMsg().equals(EnumResponse.RESP_SUCC.getDescription())) {
              DcnbPhieuNhapKhoHdr rowData = objectMapper.convertValue(result.getBody().getData(), DcnbPhieuNhapKhoHdr.class);
            if (rowData.getTrangThai().equals(TrangThaiAllEnum.DA_DUYET_LDCC.getId())) {
              PhieuNhapXuatHistory phieuNhapXuatHistory = new PhieuNhapXuatHistory();
              phieuNhapXuatHistory.setSoLuong(rowData.getTongSoLuong()); //ThucXuat
              phieuNhapXuatHistory.setSoPhieu(rowData.getSoPhieuNhapKho());
              phieuNhapXuatHistory.setLoaiVthh(rowData.getLoaiVthh());
              phieuNhapXuatHistory.setCloaiVthh(rowData.getCloaiVthh());
              phieuNhapXuatHistory.setMaKho(rowData.getMaLoKho() == null ? rowData.getMaNganKho() : rowData.getMaLoKho());
              phieuNhapXuatHistory.setNgayDuyet(rowData.getNgayPDuyet());
              phieuNhapXuatHistory.setLoaiNhapXuat(1);//fix tam 1 la nhap -1 la xuat
              phieuNhapXuatHistory.setKieu("NHAP_XUAT");//nhap xuat hoac khoi tao so du dau ky
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
