package com.tcdt.qlnvhang.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.jwt.TokenAuthenticationService;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.service.feign.CategoryServiceProxy;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.util.Request;

public class BaseController {

	@Autowired
	private CategoryServiceProxy categoryServiceProxy;

	public String getStringParams(Map<String, String> allParams, String nameParam) {
		if (StringUtils.isEmpty(allParams.get(nameParam))) {
			return null;
		}
		return allParams.get(nameParam);
	}

	public String getAuthorizationToken(HttpServletRequest request) {
		return (String) request.getHeader("Authorization");
	}

	public Long getLongParams(Map<String, String> allParams, String nameParam) {
		if (StringUtils.isEmpty(allParams.get(nameParam))) {
			return null;
		}
		return Long.valueOf(allParams.get(nameParam));
	}

	public Integer getIntParams(Map<String, String> allParams, String nameParam) {
		if (StringUtils.isEmpty(allParams.get(nameParam))) {
			return null;
		}
		return Integer.valueOf(allParams.get(nameParam));
	}

	private static final List<String> EXTENSIONS = Arrays.asList(".doc", ".docx", ".xls", ".xlsx");

	public String getDvql(HttpServletRequest req) {
		Authentication authentication = TokenAuthenticationService.getAuthentication(req);
		return authentication.getDetails().toString();
	}

	public String getUserName(HttpServletRequest req) {
		Authentication authentication = TokenAuthenticationService.getAuthentication(req);
		return authentication.getName();
	}

	public QlnvDmDonvi getDvi(HttpServletRequest request) throws Exception {
		// Call feign get dvql
		ResponseEntity<String> response = categoryServiceProxy.getDetail(this.getAuthorizationToken(request),
				this.getDvql(request));

		if (Request.getStatus(response.getBody()) != EnumResponse.RESP_SUCC.getValue())
			throw new Exception("Không tìm truy vấn được thông tin đơn vị");

		// Passed ket qua tra ve, tuy bien type list or object
		String str = Request.getAttrFromJson(response.getBody(), "data");
		Type type = new TypeToken<QlnvDmDonvi>() {
		}.getType();
		QlnvDmDonvi objDonVi = new Gson().fromJson(str, type);

		if (ObjectUtils.isEmpty(objDonVi))
			throw new Exception("Không tìm truy vấn được thông tin đơn vị");
		return objDonVi;
	}

	public QlnvDmDonvi getDviByMa(HttpServletRequest request, String maDvi) throws Exception {
		// Call feign get dvql
		BaseRequest baseRequest = new BaseRequest();
		baseRequest.setStr(maDvi);
		ResponseEntity<String> response = categoryServiceProxy.getDetailByCode(this.getAuthorizationToken(request),
				baseRequest);

		if (Request.getStatus(response.getBody()) != EnumResponse.RESP_SUCC.getValue())
			throw new Exception("Không tìm truy vấn được thông tin đơn vị");

		// Passed ket qua tra ve, tuy bien type list or object
		String str = Request.getAttrFromJson(response.getBody(), "data");
		Type type = new TypeToken<QlnvDmDonvi>() {
		}.getType();
		QlnvDmDonvi objDonVi = new Gson().fromJson(str, type);

		if (ObjectUtils.isEmpty(objDonVi))
			throw new Exception("Không tìm truy vấn được thông tin đơn vị");
		return objDonVi;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	<T> void updateMapToObject(Map<String, String> params, T source, Class cls) throws JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Object overrideObj = mapper.convertValue(params, cls);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.updateValue(source, overrideObj);
	}

	public <T> void updateObjectToObject(T source, T objectEdit) throws JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.updateValue(source, objectEdit);
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

	public String getSort(Map<String, String> allParams) {
		if (allParams.get("sort") == null) {
			return "desc";
		}
		return allParams.get("sort");
	}

	boolean allowExtension(String fileName) {
		for (String ext : EXTENSIONS) {
			if (fileName.endsWith(ext)) {
				return true;
			}
		}
		return false;
	}

	public static Date getDateTimeNow() throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String local = df.format(date);
		Date datenow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(local);
		return datenow;
	}

	public static String getUUID(String code) {
		if (StringUtils.isEmpty(code))
			return UUID.randomUUID().toString().replace("-", "");
		return code + UUID.randomUUID().toString().replace("-", "");
	}
}
