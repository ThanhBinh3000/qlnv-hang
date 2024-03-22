package com.tcdt.qlnvhang.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tcdt.qlnvhang.entities.UserActivity;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKho;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKho;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoCt;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.phieuxuatkho.XhPhieuXkhoBtt;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgPhieuXuatKho;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.khotang.KtNganKhoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoCtRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhPhieuNhapKhoCtRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhGnvHdrRepository;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.service.UserActivityService;
import com.tcdt.qlnvhang.service.UserActivitySettingService;
import com.tcdt.qlnvhang.service.feign.LuuKhoClient;
import com.tcdt.qlnvhang.table.PhieuNhapXuatHistory;
import com.tcdt.qlnvhang.table.UserActivitySetting;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuXuatKhoHdr;
import com.tcdt.qlnvhang.table.khotang.KtNganKho;
import com.tcdt.qlnvhang.table.khotang.KtNganLo;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBienBanDayKhoHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoCt;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuXuatKho;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhGnvHdr;
import com.tcdt.qlnvhang.util.BeanUtils;
import com.tcdt.qlnvhang.util.DataUtils;
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
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
  @Autowired
  private XhCtvtQuyetDinhGnvHdrRepository xhCtvtQuyetDinhGnvHdrRepository;
  @Autowired
  private UserActivitySettingService userActivitySettingService;
  @Autowired
  private KtNganKhoRepository ktNganKhoRepository;
  @Autowired
  private KtNganLoRepository ktNganLoRepository;

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

  @Pointcut("execution(* com.tcdt.qlnvhang.controller.nhaphang.dauthau.nhapkho.NhBienBanNhapDayKhoController.updateStatus(..)) ||" +
      "execution(* com.tcdt.qlnvhang.controller.nhaphangtheoptmuatt.HhBienBanDayKhoControler.updateStatusUbtvqh(..))")
  public void BbNhapDayKhoPointCut() {
  }

  @Pointcut("execution(* com.tcdt.qlnvhang.controller.xuathang.daugia.xuatkho.XhDgBbTinhKhoController.updateStatus(..)) ||" +
      "execution(* com.tcdt.qlnvhang.controller.xuathang.bantructiep.xuatkho.bienbantinhkho.XhBbTinhkBttControler.updateStatus(..))")
  public void BbXuatDocKhoPointCut() {
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
        Boolean isAllow = getAllow(request);
        if (isAllow) {
          CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
          UserActivity entity = new UserActivity();
          entity.setIp(BeanUtils.getClientIpAddress(request));
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
            phieuNhapXuatHistory.setSoLuong(DataUtils.safeToDouble(nhPhieuNhapKhoCt.getSoLuongThucNhap()));
            phieuNhapXuatHistory.setDonGia(DataUtils.safeToDouble(nhPhieuNhapKhoCt.getDonGia()));
//            phieuNhapXuatHistory.setThanhTien(DataUtils.safeToLong(nhPhieuNhapKhoCt.getThanhTien()));
            phieuNhapXuatHistory.setThanhTien(phieuNhapXuatHistory.getDonGia() * phieuNhapXuatHistory.getSoLuong());
            phieuNhapXuatHistory.setSoLuongChungTu(nhPhieuNhapKhoCt.getSoLuongChungTu());
            phieuNhapXuatHistory.setIdPhieu(rowData.getId());
            phieuNhapXuatHistory.setSoPhieu(rowData.getSoPhieuNhapKho());
            phieuNhapXuatHistory.setLoaiNhapXuat(1);
            phieuNhapXuatHistory.setLoaiVthh(rowData.getLoaiVthh());
            phieuNhapXuatHistory.setCloaiVthh(rowData.getCloaiVthh());
            phieuNhapXuatHistory.setNgayDuyet(DataUtils.convertToLocalDate(rowData.getNgayPduyet()));
            phieuNhapXuatHistory.setKieu("NHAP_DAU_THAU");
            phieuNhapXuatHistory.setLoaiHinhNhapXuat("81");
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
            phieuNhapXuatHistory.setSoLuong(DataUtils.safeToDouble(dtl.getSoLuongThucNhap()));
            phieuNhapXuatHistory.setDonGia(DataUtils.safeToDouble(dtl.getDonGia()));
//            phieuNhapXuatHistory.setThanhTien(DataUtils.safeToLong(dtl.getDonGia()) * DataUtils.safeToLong(dtl.getSoLuongThucNhap()));
            phieuNhapXuatHistory.setThanhTien(DataUtils.safeToDouble(dtl.getDonGia()) * DataUtils.safeToLong(dtl.getSoLuongThucNhap()));
            phieuNhapXuatHistory.setSoLuongChungTu(dtl.getSoLuongChungTu());
            phieuNhapXuatHistory.setIdPhieu(rowData.getId());
            phieuNhapXuatHistory.setSoPhieu(rowData.getSoPhieuNhapKho());
            phieuNhapXuatHistory.setLoaiNhapXuat(1);
            phieuNhapXuatHistory.setLoaiVthh(rowData.getLoaiVthh());
            phieuNhapXuatHistory.setCloaiVthh(rowData.getCloaiVthh());
            phieuNhapXuatHistory.setNgayDuyet(DataUtils.convertToLocalDate(rowData.getNgayPduyet()));
            phieuNhapXuatHistory.setKieu("NHAP_TRUC_TIEP");
            phieuNhapXuatHistory.setLoaiHinhNhapXuat("93");
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
            phieuNhapXuatHistory.setSoLuong(DataUtils.safeToDouble(rowData.getThucXuat()));
            phieuNhapXuatHistory.setDonGia(DataUtils.safeToDouble(rowData.getDonGia()));
            phieuNhapXuatHistory.setThanhTien(DataUtils.safeToDouble(rowData.getThanhTien()));
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
            //xu ly loai hinh nhap xuat
            Optional<XhCtvtQuyetDinhGnvHdr> gnvHdr = xhCtvtQuyetDinhGnvHdrRepository.findById(rowData.getIdQdGiaoNvXh());
            if (gnvHdr.isPresent()) {
              if (gnvHdr.get().getLoaiNhapXuat().equals("Xuất viện trợ")) {
                phieuNhapXuatHistory.setLoaiHinhNhapXuat("92");
              } else {
                phieuNhapXuatHistory.setLoaiHinhNhapXuat("84");
              }
            }
            if (rowData.getType().equals("XC")) {
              phieuNhapXuatHistory.setLoaiHinhNhapXuat("101");
            }

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
            phieuNhapXuatHistory.setSoLuong(DataUtils.safeToDouble(rowData.getThucXuat()));
            phieuNhapXuatHistory.setDonGia(DataUtils.safeToDouble(rowData.getDonGia()));
            phieuNhapXuatHistory.setThanhTien(DataUtils.safeToDouble(rowData.getDonGia()) * DataUtils.safeToLong(rowData.getThucXuat()));
            phieuNhapXuatHistory.setSoLuongChungTu(BigDecimal.ZERO);
            phieuNhapXuatHistory.setIdPhieu(rowData.getId());
            phieuNhapXuatHistory.setSoPhieu(rowData.getSoPhieuXuatKho());
            phieuNhapXuatHistory.setLoaiNhapXuat(-1);
            phieuNhapXuatHistory.setLoaiVthh(rowData.getLoaiVthh());
            phieuNhapXuatHistory.setCloaiVthh(rowData.getCloaiVthh());
            phieuNhapXuatHistory.setNgayDuyet(rowData.getNgayPduyet());
            phieuNhapXuatHistory.setKieu("BAN_DAU_GIA");
            phieuNhapXuatHistory.setLoaiHinhNhapXuat("89");
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
            phieuNhapXuatHistory.setSoLuong(DataUtils.safeToDouble(rowData.getThucXuat()));
            phieuNhapXuatHistory.setDonGia(DataUtils.safeToDouble(rowData.getDonGia()));
            phieuNhapXuatHistory.setThanhTien(DataUtils.safeToDouble(rowData.getDonGia()) * DataUtils.safeToLong(rowData.getThucXuat()));
            phieuNhapXuatHistory.setSoLuongChungTu(rowData.getTheoChungTu());
            phieuNhapXuatHistory.setIdPhieu(rowData.getId());
            phieuNhapXuatHistory.setSoPhieu(rowData.getSoPhieuXuatKho());
            phieuNhapXuatHistory.setLoaiNhapXuat(-1);
            phieuNhapXuatHistory.setLoaiVthh(rowData.getLoaiVthh());
            phieuNhapXuatHistory.setCloaiVthh(rowData.getCloaiVthh());
            phieuNhapXuatHistory.setNgayDuyet(rowData.getNgayPduyet());
            phieuNhapXuatHistory.setKieu("BAN_TRUC_TIEP");
            phieuNhapXuatHistory.setLoaiHinhNhapXuat("95");
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
            phieuNhapXuatHistory.setSoLuong(DataUtils.safeToDouble(rowData.getTongSoLuong()));
            phieuNhapXuatHistory.setDonGia(DataUtils.safeToDouble(rowData.getDonGia()));
            phieuNhapXuatHistory.setThanhTien(DataUtils.safeToDouble(rowData.getThanhTien()));
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
              phieuNhapXuatHistory.setSoLuong(DataUtils.safeToDouble(rowData.getTongSoLuong()));
              phieuNhapXuatHistory.setDonGia(0d);
              phieuNhapXuatHistory.setThanhTien(0d);
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

  @AfterReturning(value = "BbNhapDayKhoPointCut()", returning = "result")
  public void BbNhapDayKhoAfterLog(JoinPoint joinPoint, ResponseEntity<BaseResponse> result) {
    try {
      if (joinPoint.getTarget().toString().contains("NhBienBanNhapDayKhoController")) {
        if (result != null && result.getBody().getMsg().equals(EnumResponse.RESP_SUCC.getDescription())) {
          NhBbNhapDayKho rowData = objectMapper.convertValue(result.getBody().getData(), NhBbNhapDayKho.class);
          if (rowData.getTrangThai().equals(TrangThaiAllEnum.DA_DUYET_LDCC.getId())) {
            KtNganLo nganLo = ktNganLoRepository.findFirstByMaNganlo(rowData.getMaLoKho());
            if (!DataUtils.isNullObject(nganLo)) {
              nganLo.setNgayNhapDay(rowData.getNgayKetThucNhap());
              nganLo.setTichLuongKdLt(BigDecimal.ZERO);
              nganLo.setTichLuongKdVt(BigDecimal.ZERO);
              ktNganLoRepository.save(nganLo);
            } else {
              KtNganKho nganKho = ktNganKhoRepository.findByMaNgankho(rowData.getMaNganKho());
              if (!DataUtils.isNullObject(nganKho)) {
                nganKho.setNgayNhapDay(rowData.getNgayKetThucNhap());
                nganKho.setTichLuongKdLt(BigDecimal.ZERO);
                nganKho.setTichLuongKdVt(BigDecimal.ZERO);
                ktNganKhoRepository.save(nganKho);
              }
            }
            /*
            PhieuNhapXuatHistory phieuNhapXuatHistory = new PhieuNhapXuatHistory();
            phieuNhapXuatHistory.setMaKho(rowData.getMaLoKho() == null ? rowData.getMaNganKho() : rowData.getMaLoKho());
            phieuNhapXuatHistory.setSoLuong(DataUtils.safeToDouble(rowData.getSoLuong()));
            phieuNhapXuatHistory.setIdPhieu(rowData.getId());
            phieuNhapXuatHistory.setSoPhieu(rowData.getSoBienBanNhapDayKho());
            phieuNhapXuatHistory.setLoaiNhapXuat(0);
            phieuNhapXuatHistory.setNgayDuyet(DataUtils.convertToLocalDate(rowData.getNgayPduyet()));
            phieuNhapXuatHistory.setKieu("BB_DAY_KHO_NHAP_DAU_THAU");
            phieuNhapXuatHistory.setBang(rowData.TABLE_NAME);
            phieuNhapXuatHistory.setNamNhap(rowData.getNam());
            phieuNhapXuatHistory.setNgayNhapDay(DataUtils.convertToLocalDate(rowData.getNgayKetThucNhap()));
            phieuNhapXuatHistory.setNgayTao(LocalDate.now());

            luuKhoClient.synchronizeData(phieuNhapXuatHistory);
            logger.info("Cập nhật kho theo Phiếu nhập đầy kho NhBienBanNhapDayKhoController {}", rowData);*/
          }
        }
      } else if (joinPoint.getTarget().toString().contains("HhBienBanDayKhoControler")) {
        if (result != null && result.getBody().getMsg().equals(EnumResponse.RESP_SUCC.getDescription())) {
          HhBienBanDayKhoHdr rowData = objectMapper.convertValue(result.getBody().getData(), HhBienBanDayKhoHdr.class);
          if (rowData.getTrangThai().equals(TrangThaiAllEnum.DA_DUYET_LDCC.getId())) {
            KtNganLo nganLo = ktNganLoRepository.findFirstByMaNganlo(rowData.getMaLoKho());
            if (!DataUtils.isNullObject(nganLo)) {
              nganLo.setNgayNhapDay(rowData.getNgayKthucNhap());
              nganLo.setTichLuongKdLt(BigDecimal.ZERO);
              nganLo.setTichLuongKdVt(BigDecimal.ZERO);
              ktNganLoRepository.save(nganLo);
            } else {
              KtNganKho nganKho = ktNganKhoRepository.findByMaNgankho(rowData.getMaNganKho());
              if (!DataUtils.isNullObject(nganKho)) {
                nganKho.setNgayNhapDay(rowData.getNgayKthucNhap());
                nganKho.setTichLuongKdLt(BigDecimal.ZERO);
                nganKho.setTichLuongKdVt(BigDecimal.ZERO);
                ktNganKhoRepository.save(nganKho);
              }
            }
            /*PhieuNhapXuatHistory phieuNhapXuatHistory = new PhieuNhapXuatHistory();
            phieuNhapXuatHistory.setMaKho(rowData.getMaLoKho() == null ? rowData.getMaNganKho() : rowData.getMaLoKho());
            phieuNhapXuatHistory.setSoLuong(DataUtils.safeToDouble(rowData.getTongSoLuongNhap()));
            phieuNhapXuatHistory.setIdPhieu(rowData.getId());
            phieuNhapXuatHistory.setSoPhieu(rowData.getSoBbNhapDayKho());
            phieuNhapXuatHistory.setLoaiNhapXuat(0);
            phieuNhapXuatHistory.setNgayDuyet(DataUtils.convertToLocalDate(rowData.getNgayPduyet()));
            phieuNhapXuatHistory.setKieu("BB_DAY_KHO_NHAP_TRUC_TIEP");
            phieuNhapXuatHistory.setBang(rowData.TABLE_NAME);
            phieuNhapXuatHistory.setNamNhap(rowData.getNamKh());
            phieuNhapXuatHistory.setNgayNhapDay(DataUtils.convertToLocalDate(rowData.getNgayKthucNhap()));
            phieuNhapXuatHistory.setNgayTao(LocalDate.now());

            luuKhoClient.synchronizeData(phieuNhapXuatHistory);
            logger.info("Cập nhật kho theo Phiếu nhập đầy kho HhBienBanDayKhoControler {}", rowData);*/
          }
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @AfterReturning(value = "BbXuatDocKhoPointCut()", returning = "result")
  public void BbXuatDocKhoAfterLog(JoinPoint joinPoint, ResponseEntity<BaseResponse> result) {
    try {
      if (joinPoint.getTarget().toString().contains("XhDgBbTinhKhoController")) {
        if (result != null && result.getBody().getMsg().equals(EnumResponse.RESP_SUCC.getDescription())) {
          NhBbNhapDayKho rowData = objectMapper.convertValue(result.getBody().getData(), NhBbNhapDayKho.class);
          if (rowData.getTrangThai().equals(TrangThaiAllEnum.DA_DUYET_LDCC.getId())) {
            KtNganLo nganLo = ktNganLoRepository.findFirstByMaNganlo(rowData.getMaLoKho());
            if (!DataUtils.isNullObject(nganLo)) {
              nganLo.setNgayNhapDay(rowData.getNgayKetThucNhap());
              nganLo.setTichLuongKdLt(nganLo.getTichLuongTkLt());
              nganLo.setTichLuongKdVt(nganLo.getTichLuongTkVt());
              ktNganLoRepository.save(nganLo);
            } else {
              KtNganKho nganKho = ktNganKhoRepository.findByMaNgankho(rowData.getMaNganKho());
              if (!DataUtils.isNullObject(nganKho)) {
                nganKho.setNgayNhapDay(rowData.getNgayKetThucNhap());
                nganKho.setTichLuongKdLt(nganKho.getTichLuongTkLt());
                nganKho.setTichLuongKdVt(nganKho.getTichLuongTkVt());
                ktNganKhoRepository.save(nganKho);
              }
            }
          }
        }
      } else if (joinPoint.getTarget().toString().contains("XhBbTinhkBttControler")) {
        if (result != null && result.getBody().getMsg().equals(EnumResponse.RESP_SUCC.getDescription())) {
          HhBienBanDayKhoHdr rowData = objectMapper.convertValue(result.getBody().getData(), HhBienBanDayKhoHdr.class);
          if (rowData.getTrangThai().equals(TrangThaiAllEnum.DA_DUYET_LDCC.getId())) {
            KtNganLo nganLo = ktNganLoRepository.findFirstByMaNganlo(rowData.getMaLoKho());
            if (!DataUtils.isNullObject(nganLo)) {
              nganLo.setNgayNhapDay(rowData.getNgayKthucNhap());
              ktNganLoRepository.save(nganLo);
            } else {
              KtNganKho nganKho = ktNganKhoRepository.findByMaNgankho(rowData.getMaNganKho());
              if (!DataUtils.isNullObject(nganKho)) {
                nganKho.setNgayNhapDay(rowData.getNgayKthucNhap());
                nganKho.setTichLuongKdLt(BigDecimal.ZERO);
                nganKho.setTichLuongKdVt(BigDecimal.ZERO);
                ktNganKhoRepository.save(nganKho);
              }
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
      List<Object> lst = new ArrayList<>();
      for (Object a : args) {
        if (!(a instanceof HttpServletRequest) && !(a instanceof HttpServletResponse) && !(a instanceof CustomUserDetails)) {
          lst.add(a);
        }
      }
      ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(lst);
    } catch (JsonProcessingException e) {
      return null;
    }
  }

  public Boolean getAllow(HttpServletRequest request) throws Exception {
    String type = "SERVICE";
    if ("/login".equalsIgnoreCase(request.getRequestURI())) {
      type = "LOGIN";
    }
    if ("/user-info/logout".equalsIgnoreCase(request.getRequestURI())) {
      type = "LOGOUT";
    }
    Boolean isAllow = false;
    UserActivitySetting setting = userActivitySettingService.getSetting();
    switch (type) {
      case "LOGIN":
        isAllow = setting.getWriteLogLogin();
        break;
      case "LOGOUT":
        isAllow = setting.getWriteLogLogout();
        break;
      case "SERVICE":
        isAllow = setting.getWriteLogUserActivity();
        break;
      default:
        break;
    }
    return isAllow;
  }
}
