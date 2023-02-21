package com.tcdt.qlnvhang.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.repository.quyetdinhpheduyetketqualuachonnhathauvatu.GenericRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Log4j2
public class DataUtils {
  @Autowired
  private ObjectMapper objectMapper;

  public <T> T toObject(Object source, Class<T> clazz) {
    if (source == null) return null;
    return objectMapper.convertValue(source, clazz);
  }

  public <T> List<T> toListObject(Object obj, Class<T> clazz) throws Exception {
    if (obj instanceof Collection) {
      Collection<Object> list = (Collection<Object>) obj;
      if (CollectionUtils.isEmpty(list)) return Collections.emptyList();
      return list.stream().map(item -> this.toObject(item, clazz)).collect(Collectors.toList());
    }
    throw new Exception("Can not convert to the list object");
  }

  public <T> void validateExits(GenericRepository<T, Long> repository, Long id, boolean isCreate) throws Exception {
    if (id == null || repository == null) throw new Exception("Id or repository must not be null");

    Optional<T> entity = repository.findById(id);

    if (isCreate && entity.isPresent()) {
      log.error("Entity {} existed", repository.getClass().getSimpleName());
      throw new Exception("Entity existed");
    } else if (!entity.isPresent()) {
      log.error("Entity {} not found", repository.getClass().getSimpleName());
      throw new Exception("Entity not found");
    }
  }

  public <T> List<T> findAllByIds(GenericRepository<T, Long> repository, Set<Long> ids) throws Exception {
    if (CollectionUtils.isEmpty(ids) || repository == null) {
      throw new Exception("Id or repository must not be null");
    }
    List<T> entities = repository.findByIdIn(ids);

    if (CollectionUtils.isEmpty(entities)) {
      throw new Exception("Entity not found");
    }

    return entities;
  }

  public static String toStringValue(Object obj) {
    return Optional.ofNullable(obj).map(Object::toString).orElse("");
  }

  public static Long safeToLong(Object obj1) {
    return safeToLong(obj1, 0L);
  }

  public static Long safeToLong(Object obj1, Long defaultValue) {
    Long result = defaultValue;
    if (obj1 != null) {
      if (obj1 instanceof BigDecimal) {
        return ((BigDecimal) obj1).longValue();
      }
      if (obj1 instanceof BigInteger) {
        return ((BigInteger) obj1).longValue();
      }
      try {
        result = Long.parseLong(obj1.toString());
      } catch (Exception ignored) {
      }
    }

    return result;
  }

  public static int safeToInt(Object obj1) {
    return safeToInt(obj1, 0);
  }

  public static int safeToInt(Object obj1, int defaultValue) {
    int result = defaultValue;
    if (obj1 != null) {
      try {
        result = Integer.parseInt(obj1.toString());
      } catch (Exception ignored) {
      }
    }

    return result;
  }

  public static boolean isNullOrEmpty(final Collection<?> collection) {
    return collection == null || collection.isEmpty();
  }

  public static boolean isNullOrEmpty(String st) {
    return st == null || st.isEmpty() || st.equals("null");
  }

  public static boolean isNull(String s) {
    return s == null || s.equals("null");
  }

  public static boolean isEmpty(String s) {
    return s.equals("");
  }

  public static boolean isNullObject(Object obj1) {
    if (obj1 == null) {
      return true;
    }
    if (obj1 instanceof String) {
      return isNullOrEmpty(obj1.toString());
    }
    return false;
  }

  public static String safeToString(Object obj1, String defaultValue) {
    if (obj1 == null) {
      return defaultValue;
    }

    return obj1.toString();
  }

  public static boolean isNotBlank(Object st) {
    return !DataUtils.isNullObject(st);
  }

  public static String trim(String st) {
    return st.trim();
  }

  public static String safeToString(Object obj1) {
    return safeToString(obj1, "");
  }

  public static Double safeToDouble(Object obj1, Double defaultValue) {
    Double result = defaultValue;
    if (obj1 != null) {
      try {
        result = Double.parseDouble(obj1.toString());
      } catch (Exception ignored) {
      }
    }

    return result;
  }

  public static Double safeToDouble(Object obj1) {
    return safeToDouble(obj1, 0.0);
  }

  public static void copyProperties(Object src, Object target, String... ignore) {
    BeanUtils.copyProperties(src, target, getNullPropertyNames(src, ignore));
  }

  public static String[] getNullPropertyNames(Object source, String... ignore) {
    final BeanWrapper src = new BeanWrapperImpl(source);
    PropertyDescriptor[] pds = src.getPropertyDescriptors();
    Set<String> emptyNames = new HashSet<String>();
    Collections.addAll(emptyNames, ignore);
    for (PropertyDescriptor pd : pds) {
      Object srcValue = src.getPropertyValue(pd.getName());
      if (srcValue == null)
        emptyNames.add(pd.getName());
    }
    String[] result = new String[emptyNames.size()];
    return emptyNames.toArray(result);
  }

  public static LocalDate convertToLocalDate(Date date) {
    Instant instant = date.toInstant();
    ZoneId zoneId = ZoneId.systemDefault();
    return instant.atZone(zoneId).toLocalDate();
  }

  public static BigDecimal safeToBigDecimal(Object value) {
    try {
      return new BigDecimal(value.toString());
    } catch (NumberFormatException e) {
      return BigDecimal.ZERO;
    }
  }

  public static BigInteger safeToBigInteger(Object value) {
    try {
      return new BigInteger(value.toString());
    } catch (NumberFormatException e) {
      return BigInteger.ZERO;
    }
  }
}
