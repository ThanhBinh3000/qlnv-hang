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
  ObjectMapper objectMapper;
  @Autowired
  private LuuKhoClient luuKhoClient;
  @Autowired
  private NhPhieuNhapKhoCtRepository nhPhieuNhapKhoCtRepository;

  @Pointcut("within(com.tcdt.qlnvhang..*) && bean(*Controller))")
  public void v3Controller() {
  }

  @Pointcut("execution(* com.tcdt.qlnvhang.controller.nhaphang.dauthau.nhapkho.NhPhieuNhapKhoController.updateStatus(..))")
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
      if (result != null && result.getBody().getMsg().equals(EnumResponse.RESP_SUCC.getDescription())) {
        NhPhieuNhapKho nhPhieuNhapKho = objectMapper.convertValue(result.getBody().getData(), NhPhieuNhapKho.class);
        if (nhPhieuNhapKho.getTrangThai().equals(TrangThaiAllEnum.DA_DUYET_LDCC.getId())) {
          NhPhieuNhapKhoCt nhPhieuNhapKhoCt = nhPhieuNhapKhoCtRepository.findAllByIdPhieuNkHdr(nhPhieuNhapKho.getId()).get(0);
          PhieuNhapXuatHistory phieuNhapXuatHistory = new PhieuNhapXuatHistory();
          phieuNhapXuatHistory.setSoLuong(DataUtils.safeToLong(nhPhieuNhapKhoCt.getSoLuongThucNhap()));
          phieuNhapXuatHistory.setLoaiVthh(nhPhieuNhapKho.getLoaiVthh());
          phieuNhapXuatHistory.setMaKho(nhPhieuNhapKho.getMaLoKho());
          phieuNhapXuatHistory.setNgayDuyet(DataUtils.convertToLocalDate(nhPhieuNhapKho.getNgayPduyet()));
          phieuNhapXuatHistory.setLoaiNhapXuat(1);//fix tam 1 la nhap -1 la xuat
          luuKhoClient.synchronizeData(phieuNhapXuatHistory);
          logger.info("Cập nhật kho theo Phiếu nhập kho {}", nhPhieuNhapKho);
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
}
