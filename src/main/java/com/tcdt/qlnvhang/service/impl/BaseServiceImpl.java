package com.tcdt.qlnvhang.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tcdt.qlnvhang.common.DocxToPdfConverter;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.jwt.TokenAuthenticationService;
import com.tcdt.qlnvhang.repository.DanhMucRepository;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.QlnvDmDonviSearchReq;
import com.tcdt.qlnvhang.request.QlnvDmVattuSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.request.object.HhDmDviLquanSearchReq;
import com.tcdt.qlnvhang.response.CommonResponse;
import com.tcdt.qlnvhang.service.feign.BaoCaoClient;
import com.tcdt.qlnvhang.service.feign.CategoryServiceProxy;
import com.tcdt.qlnvhang.service.feign.SystemServiceProxy;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.table.DmDonViDTO;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.QlnvDanhMuc;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.catalog.DmVattuDTO;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanLayMauHdr;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Slf4j
@Service
public abstract class BaseServiceImpl {
    @Autowired
    private CategoryServiceProxy categoryServiceProxy;
    @Autowired
    private BaoCaoClient baoCaoClient;

    @Autowired
    DanhMucRepository danhMucRepository;

    @Autowired
    private QlnvDmDonviRepository qlnvDmDonviRepository;

    @Autowired
    private Gson gson;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public ObjectMapper objectMapper;
//	@Autowired
//	private HttpServletRequest req;

    @Autowired
    private SystemServiceProxy systemServiceProxy;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    public DocxToPdfConverter docxToPdfConverter;

    @Value("${path.base-report-folder}")
    public String baseReportFolder;

    public QlnvDmDonvi getDviByMa(String maDvi, HttpServletRequest req) throws Exception {
        QlnvDmDonvi qlnvDmDonvi = null;
        try {

            // Call feign get dvql
            BaseRequest baseRequest = new BaseRequest();
            baseRequest.setStr(maDvi);
            ResponseEntity<String> response = categoryServiceProxy.getDetailByCode(getAuthorizationToken(req),
                    baseRequest);
            log.info("Kết quả danh mục đơn vị: {}", gson.toJson(response));
            if (Request.getStatus(response.getBody()) != EnumResponse.RESP_SUCC.getValue())
                qlnvDmDonvi = new QlnvDmDonvi();

            // Passed ket qua tra ve, tuy bien type list or object
            String str = Request.getAttrFromJson(response.getBody(), "data");
            Type type = new TypeToken<QlnvDmDonvi>() {
            }.getType();

            qlnvDmDonvi = gson.fromJson(str, type);

        } catch (Exception e) {
            log.error("Không lấy thông tin danh mục đơn vị", e);
            throw e;
        }
        return qlnvDmDonvi;
    }

    public Map<String, String> getListDanhMucChung(String loai) {
        ResponseEntity<String> response = categoryServiceProxy.getDanhMucChung(getAuthorizationToken(request),
                loai);
        String str = Request.getAttrFromJson(response.getBody(), "data");
        HashMap<String, String> data = new HashMap<String, String>();
        List<Map<String, Object>> retMap = new Gson().fromJson(str, new TypeToken<List<HashMap<String, Object>>>() {
        }.getType());
        for (Map<String, Object> map : retMap) {
            data.put(String.valueOf(map.get("ma")), String.valueOf(map.get("giaTri")));
        }
        return data;
    }

    public Map<String, String> getListDanhMucDvi(String capDvi, String maDviCha, String trangThai) {
        QlnvDmDonviSearchReq objRequest = new QlnvDmDonviSearchReq();
        objRequest.setCapDvi(capDvi);
        objRequest.setMaDviCha(maDviCha);
        objRequest.setTrangThai(trangThai);
        ResponseEntity<String> response = categoryServiceProxy.getDanhMucDvi(getAuthorizationToken(request),
                objRequest);
        String str = Request.getAttrFromJson(response.getBody(), "data");
        HashMap<String, String> data = new HashMap<String, String>();
        List<Map<String, Object>> retMap = new Gson().fromJson(str, new TypeToken<List<HashMap<String, Object>>>() {
        }.getType());
        for (Map<String, Object> map : retMap) {
            data.put(String.valueOf(map.get("maDvi")), String.valueOf(map.get("tenDvi")));
        }
        return data;
    }

    public Map<String, DmDonViDTO> getListDanhMucDviByMadviIn(List<String> listMaDvi, String trangThai) {
        QlnvDmDonviSearchReq objRequest = new QlnvDmDonviSearchReq();
        objRequest.setListMaDvi(listMaDvi);
        objRequest.setTrangThai(trangThai);
        ResponseEntity<String> response = categoryServiceProxy.getDanhMucDviByMaDviIn(getAuthorizationToken(request),
                objRequest);
        String str = Request.getAttrFromJson(response.getBody(), "data");
        HashMap<String, DmDonViDTO> data = new HashMap<String, DmDonViDTO>();
        List<Map<String, Object>> retMap = new Gson().fromJson(str, new TypeToken<List<HashMap<String, Object>>>() {
        }.getType());
        for (Map<String, Object> map : retMap) {
            DmDonViDTO dto = new DmDonViDTO();
            dto.setCapDvi(String.valueOf(map.get("capDvi")));
            dto.setTenDvi(String.valueOf(map.get("tenDvi")));
            dto.setMaDvi(String.valueOf(map.get("maDvi")));
            dto.setDiaChi(String.valueOf(map.get("diaChi")));
            data.put(String.valueOf(map.get("maDvi")), dto);
        }
        return data;
    }

    public Map<String, Map<String, Object>> getListDanhMucDviObject(String capDvi, String maDviCha, String trangThai) {
        QlnvDmDonviSearchReq objRequest = new QlnvDmDonviSearchReq();
        objRequest.setCapDvi(capDvi);
        objRequest.setMaDviCha(maDviCha);
        objRequest.setTrangThai(trangThai);
        ResponseEntity<String> response = categoryServiceProxy.getDanhMucDvi(getAuthorizationToken(request),
                objRequest);
        String str = Request.getAttrFromJson(response.getBody(), "data");
        HashMap<String, Map<String, Object>> data = new HashMap<String, Map<String, Object>>();
        List<Map<String, Object>> retMap = new Gson().fromJson(str, new TypeToken<List<HashMap<String, Object>>>() {
        }.getType());
        for (Map<String, Object> map : retMap) {
            data.put(String.valueOf(map.get("maDvi")), map);
        }
        return data;
    }

    public Map<String, String> getListDanhMucDviLq(String loai) {
        HhDmDviLquanSearchReq objReq = new HhDmDviLquanSearchReq();
        objReq.setTypeDvi(loai);
        ResponseEntity<String> response = categoryServiceProxy.getDanhMucDviLquan(getAuthorizationToken(request),
                objReq);
        String str = Request.getAttrFromJson(response.getBody(), "data");
        HashMap<String, String> data = new HashMap<String, String>();
        List<Map<String, Object>> retMap = new Gson().fromJson(str, new TypeToken<List<HashMap<String, Object>>>() {
        }.getType());
        for (Map<String, Object> map : retMap) {
            data.put(String.valueOf(map.get("id")), String.valueOf(map.get("tenDvi")));
        }
        return data;
    }

    public Map<String, DmVattuDTO> getListObjectDanhMucHangHoa(String maDvql) {
        QlnvDmVattuSearchReq objRequest = new QlnvDmVattuSearchReq();
        objRequest.setDviQly(maDvql);
        ResponseEntity<String> response = categoryServiceProxy.getListObjectDanhMucHangHoaByMaDvql(getAuthorizationToken(request),
                objRequest);
        String str = Request.getAttrFromJson(response.getBody(), "data");
        HashMap<String, DmVattuDTO> data = new HashMap<String, DmVattuDTO>();
        List<Map<String, Object>> retMap = new Gson().fromJson(str, new TypeToken<List<HashMap<String, Object>>>() {
        }.getType());
        for (Map<String, Object> map : retMap) {
            DmVattuDTO dto = new DmVattuDTO();
            dto.setMa(String.valueOf(map.get("ma")));
            dto.setTen(String.valueOf(map.get("ten")));
            dto.setNhomHhBaoHiem(String.valueOf(map.get("nhomHhBaoHiem")));
            dto.setCap(String.valueOf(map.get("cap")));
            dto.setMaDviTinh(String.valueOf(map.get("maDviTinh")));
            dto.setLoaiHang(String.valueOf(map.get("loaiHang")));
            data.put(String.valueOf(map.get("ma")), dto);
        }
        return data;
    }

    public Map<String, String> getListDanhMucHangHoa() {
        ResponseEntity<String> response = categoryServiceProxy.getDanhMucHangHoa(getAuthorizationToken(request));
        String str = Request.getAttrFromJson(response.getBody(), "data");
        HashMap<String, String> data = new HashMap<String, String>();
        List<Map<String, Object>> retMap = new Gson().fromJson(str, new TypeToken<List<HashMap<String, Object>>>() {
        }.getType());
        for (Map<String, Object> map : retMap) {
            data.put(String.valueOf(map.get("ma")), String.valueOf(map.get("ten")));
        }
        return data;
    }

    public Map<String, Map<String, Object>> getListDanhMucHangHoaObject() {
        ResponseEntity<String> response = categoryServiceProxy.getDanhMucHangHoaAll(getAuthorizationToken(request), new HashMap<>());
        String str = Request.getAttrFromJson(response.getBody(), "data");
        HashMap<String, Map<String, Object>> data = new HashMap<String, Map<String, Object>>();
        List<Map<String, Object>> retMap = new Gson().fromJson(str, new TypeToken<List<HashMap<String, Object>>>() {
        }.getType());
        for (Map<String, Object> map : retMap) {
            data.put(String.valueOf(map.get("ma")), map);
        }
        return data;
    }

    public Map<String, String> getMapCategory() {
        if (MapCategory.map == null && danhMucRepository != null) {
            MapCategory.map = new HashMap<>();
            Iterable<QlnvDanhMuc> list = danhMucRepository.findByTrangThai(Contains.HOAT_DONG);
            for (QlnvDanhMuc cate : list) {
                MapCategory.map.put(cate.getMa(), cate.getGiaTri());
            }
        }
        return MapCategory.map;
    }

    public Map<String, String> getMapTenDvi() {
        if (MapDmucDvi.map == null && qlnvDmDonviRepository != null) {
            MapDmucDvi.map = new HashMap<>();
            Iterable<QlnvDmDonvi> list = qlnvDmDonviRepository.findByTrangThai(Contains.HOAT_DONG);
            for (QlnvDmDonvi cate : list) {
                MapDmucDvi.map.put(cate.getMaDvi(), cate.getTenDvi());
            }
        }
        return MapDmucDvi.map;
    }

    public Map<String, QlnvDmDonvi> getMapDvi() {
        if (MapDmucDvi.mapDonVi == null && qlnvDmDonviRepository != null) {
            MapDmucDvi.mapDonVi = new HashMap<>();
            Iterable<QlnvDmDonvi> list = qlnvDmDonviRepository.findByTrangThai(Contains.HOAT_DONG);
            for (QlnvDmDonvi cate : list) {
                MapDmucDvi.mapDonVi.put(cate.getMaDvi(), cate);
            }
        }
        return MapDmucDvi.mapDonVi;
    }

    public UserInfo getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            return userDetails.getUser();
        }
        return null;
    }

    public String getAuthorizationToken(HttpServletRequest request) {
        return (String) request.getHeader("Authorization");
    }

    public String getDvql(HttpServletRequest req) {
        Authentication authentication = TokenAuthenticationService.getAuthentication(req);
        return authentication.getDetails().toString();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    <T> void updateMapToObject(Map<String, String> params, T source, Class cls) throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat(Contains.FORMAT_DATE_STR));
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Object overrideObj = mapper.convertValue(params, cls);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.updateValue(source, overrideObj);
    }

    public <T> void updateObjectToObject(T source, T objectEdit) throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat(Contains.FORMAT_DATE_STR));
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.updateValue(source, objectEdit);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> T mapToClass(Map data, Class cls) {
        try {
            Object obj = cls.getDeclaredConstructor().newInstance();
            for (Field f : cls.getDeclaredFields()) {
                f.setAccessible(true);
                if (data.get(f.getName()) != null) {
                    try {
                        f.set(obj, data.get(f.getName()));
                    } catch (Exception e) {

                    }
                }
            }
            return (T) cls.cast(obj);
        } catch (Exception e) {
        }

        return null;
    }

    public Date convertStringToDate(String format, String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(strDate);
        } catch (ParseException e) {
        }
        return null;
    }

    public Long convertStringToLong(String format, String strDate) {
        if (strDate == null)
            return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(strDate).getTime();
        } catch (ParseException e) {
        }
        return null;
    }

    public static boolean isValidFormat(String format, String value, Locale locale) {
        LocalDateTime ldt = null;
        DateTimeFormatter fomatter = DateTimeFormatter.ofPattern(format, locale);

        try {
            ldt = LocalDateTime.parse(value, fomatter);
            String result = ldt.format(fomatter);
            return result.equals(value);
        } catch (DateTimeParseException e) {
            try {
                LocalDate ld = LocalDate.parse(value, fomatter);
                String result = ld.format(fomatter);
                return result.equals(value);
            } catch (DateTimeParseException exp) {
                try {
                    LocalTime lt = LocalTime.parse(value, fomatter);
                    String result = lt.format(fomatter);
                    return result.equals(value);
                } catch (DateTimeParseException e2) {
                    // Debugging purposes
                    // e2.printStackTrace();
                }
            }
        }

        return false;
    }

    public static Date getDateTimeNow() throws Exception {
        DateFormat df = new SimpleDateFormat(Contains.FORMAT_DATE_TIME_STR);
        Date date = new Date();
        String local = df.format(date);
        Date datenow = new SimpleDateFormat(Contains.FORMAT_DATE_TIME_STR).parse(local);
        return datenow;
    }

    public static String convertDateToString(Date date) throws Exception {
        if (Objects.isNull(date)) {
            return null;
        }
        DateFormat df = new SimpleDateFormat(Contains.FORMAT_DATE_STR);
        return df.format(date);
    }

    public static String convertDateToString(LocalDate date) throws Exception {
        if (Objects.isNull(date)) {
            return null;
        }
        DateFormat df = new SimpleDateFormat(Contains.FORMAT_DATE_STR);
        return df.format(date);
    }

    public static String convertDate(Date date) throws Exception {
        if (Objects.isNull(date)) {
            return null;
        }
        DateFormat df = new SimpleDateFormat(Contains.FORMAT_DATE);
        return df.format(date);
    }

    public static String convertFullDateToString(Date date) {
        if (Objects.isNull(date)) {
            return null;
        }
        DateFormat df = new SimpleDateFormat(Contains.FORMAT_DATE_TIME_FULL_STR);
        return df.format(date);
    }

    public static String getUUID(String code) {
        if (StringUtils.isEmpty(code))
            return UUID.randomUUID().toString().replace("-", "");
        return code + UUID.randomUUID().toString().replace("-", "");
    }

    public static String getDateText(Date date) throws Exception {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(Contains.FORMAT_DATE_STR);
        String dateStr = convertDateToString(date);
        LocalDate currentDate = LocalDate.parse(dateStr, df);
        // Get day from date
        int day = currentDate.getDayOfMonth();
        // Get month from date
        int month = currentDate.getMonthValue();
        // Get year from date
        int year = currentDate.getYear();
        return "Ngày " + day + " tháng " + month + " năm " + year;
    }

    public static long minusDate(Date date1, Date date2) throws Exception {
        LocalDate d1 = LocalDate.parse(convertDateToString(date1), DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate d2 = LocalDate.parse(convertDateToString(date2), DateTimeFormatter.ISO_LOCAL_DATE);
        Duration diff = Duration.between(d1.atStartOfDay(), d2.atStartOfDay());
        long diffDays = diff.toDays();
        return diffDays;
    }

    public Set<String> getMaDviCon(String maDviCha) {
        return qlnvDmDonviRepository.findMaDviByMaDviChaAndTrangThai(maDviCha, Contains.HOAT_DONG);
    }

    public <T extends BaseRequest> void prepareSearchReq(T req, UserInfo userInfo, Set<String> capDviReqs, Set<String> trangThais) throws Exception {
        String userCapDvi = userInfo.getCapDvi();

        if (!CollectionUtils.isEmpty(capDviReqs)) {
            Set<String> maDvis = new HashSet<>();
            if ((Contains.CAP_TONG_CUC.equals(userCapDvi) && capDviReqs.contains(Contains.CAP_TONG_CUC))
                    || Contains.CAP_CUC.equals(userCapDvi) && capDviReqs.contains(Contains.CAP_CUC)) {
                maDvis.add(userInfo.getDvql());
            }
            if ((Contains.CAP_TONG_CUC.equals(userCapDvi) && capDviReqs.contains(Contains.CAP_CUC)) ||
                    (Contains.CAP_CUC.equals(userCapDvi) && capDviReqs.contains(Contains.CAP_CHI_CUC))) {
                maDvis.addAll(this.getMaDviCon(userInfo.getDvql()));
            }

            if ((Contains.CAP_CUC.equals(userCapDvi) && capDviReqs.contains(Contains.CAP_TONG_CUC))
                    || (Contains.CAP_CHI_CUC.equals(userCapDvi) && capDviReqs.contains(Contains.CAP_CUC))) {
                maDvis.addAll(qlnvDmDonviRepository.findMaDviChaByMaDviAndTrangThai(userInfo.getDvql(), Contains.HOAT_DONG));
            }

            req.setMaDvis(maDvis);
        } else {
            req.setMaDvis(Collections.singleton(userInfo.getDvql()));
        }

        if (CollectionUtils.isEmpty(req.getMaDvis()))
            req.setMaDvis(Collections.singleton(userInfo.getDvql()));

        req.setTrangThais(trangThais);
    }

    public <T extends CommonResponse> void setThongTinDonVi(T res, String maDvi) throws Exception {
        QlnvDmDonvi donvi = getDviByMa(maDvi, request);
        res.setMaDvi(donvi.getMaDvi());
        res.setTenDvi(donvi.getTenDvi());
        res.setMaQhns(donvi.getMaQhns());
    }


    public <T extends TrangThaiBaseEntity> boolean updateStatus(T item, StatusReq stReq, UserInfo userInfo) throws Exception {

        String trangThai = item.getTrangThai();
        if (NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
                return false;

            item.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId());
            item.setNguoiGuiDuyetId(userInfo.getId());
            item.setNgayGuiDuyet(new Date());
        } else if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
                return false;
            item.setTrangThai(NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId());
            item.setNguoiPduyetId(userInfo.getId());
            item.setNgayPduyet(new Date());
        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
                return false;

            item.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId());
            item.setNguoiPduyetId(userInfo.getId());
            item.setNgayPduyet(new Date());
        } else {
            throw new Exception("Bad request.");
        }

        return true;
    }

    public Map<String, String> getAllHangByBoNganh(String dviQly) {
        //bo tai chinh thi chuyen thanh tong cuc du tru

        HashMap req = new HashMap();
        req.put("maDvi", dviQly);
        ResponseEntity<String> response = categoryServiceProxy.getAllHangByBoNganh(req);

        String str = Request.getAttrFromJson(response.getBody(), "data");
        HashMap<String, String> data = new HashMap<String, String>();
        List<Map<String, Object>> retMap = new Gson().fromJson(str, new TypeToken<List<HashMap<String, Object>>>() {
        }.getType());
        for (Map<String, Object> map : retMap) {
            data.put(String.valueOf(map.get("maHangHoa")), String.valueOf(map.get("tenHangHoa")));
        }
        return data;
    }

    public ReportTemplate findByTenFile(ReportTemplateRequest obj) {
        ResponseEntity<String> response = baoCaoClient.findByTenFile(getAuthorizationToken(request), obj);
        String str = Request.getAttrFromJson(response.getBody(), "data");
        Type type = new TypeToken<ReportTemplate>() {
        }.getType();
        ReportTemplate objDonVi = new Gson().fromJson(str, type);
        return objDonVi;
    }

    public Long getNextSequence(String sequenceName) {
        ResponseEntity<String> response = systemServiceProxy.getSequence(sequenceName);
        String id = Request.getAttrFromJson(response.getBody(), "data");
        return DataUtils.safeToLong(id);
    }

    public void saveFileDinhKem(List<FileDinhKemReq> fileDinhKems, Long idHdr, String tableName) {
        List<FileDinhKem> bienBanLayMauDinhKem = fileDinhKemService.saveListFileDinhKem(fileDinhKems, idHdr, tableName);
    }


}


