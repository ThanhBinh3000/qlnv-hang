package com.tcdt.qlnvhang.jasper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JasperReportManager {

    public static String buildReport(JasperReport jasperReport) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(JasperReport.class);
        Marshaller mar = context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter writer = new StringWriter();
        mar.marshal(jasperReport, writer);
        String xmlString = writer.toString();
        return replaceContentInFile(xmlString);
    }

    private static void setTotalColumnWidth(JasperReport jasperReport) {
        ColumnHeader columnHeader = jasperReport.getColumnHeader();
        if (columnHeader != null) {
            Band band = columnHeader.getBand();
            if (band != null) {
                Set<StaticText> staticText = band.getStaticText();
                if (staticText != null) {
                    Integer totalWidth = staticText.stream().map(item -> item.getReportElement().getWidth() + item.getReportElement().getX()).mapToInt(Integer::valueOf).max().orElse(0);
                    jasperReport.setColumnWidth(totalWidth);
                    jasperReport.setPageWidth(totalWidth + 40);

                }
            }
        }

    }

    public static String replaceContentInFile(String content) {
        // Thay thế nội dung
        content = content.replace("]]&gt;", "]]>");
        content = content.replace("&lt;![CDATA", "<![CDATA");
        content = content.replace("&quot;", "\\\"");
        return content;
    }

    public static void createProperty(JasperReport jasperReport, String name, String value) {
        jasperReport.setProperty(new Property(name, value));
    }

    public static void addParameter(JasperReport jasperReport, String name, String _class) {
        Set<Parameter> parameter = jasperReport.getParameter();
        if (parameter == null) {
            jasperReport.setParameter(new LinkedHashSet<>());
            jasperReport.getParameter().add(new Parameter(name, _class));
        } else {
            jasperReport.getParameter().add(new Parameter(name, _class));
        }
    }

    public static void createQueryString(JasperReport jasperReport, String queryString) {
        jasperReport.setQueryString(queryString);
    }

    public static void addField(JasperReport jasperReport, String name, String _class) {
        Set<Field> field = jasperReport.getField();
        if (field == null) {
            jasperReport.setField(new LinkedHashSet<>());
            jasperReport.getField().add(new Field(name, _class));
        } else {
            jasperReport.getField().add(new Field(name, _class));
        }
    }

    public static void addVariable(JasperReport jasperReport, String name, String _class, String variableExpression, String calculation) {
        Set<Variable> variable = jasperReport.getVariable();
        if (variable == null) {
            jasperReport.setVariable(new LinkedHashSet<>());
            jasperReport.getVariable().add(new Variable(name, _class, variableExpression, calculation));
        } else {
            jasperReport.getVariable().add(new Variable(name, _class, variableExpression, calculation));
        }
    }

    public static void addVariable(JasperReport jasperReport, String name, String _class, String variableExpression) {
        addVariable(jasperReport, name, _class, variableExpression, null);
    }

    public static void createBackground(JasperReport jasperReport, Set<TextField> textField, Set<StaticText> staticText) {
        jasperReport.setBackground(new Background(new Band("Stretch", textField, getHeightBand(textField, staticText), staticText)));
    }

    public static void addTitle(JasperReport jasperReport, String text, String expression, boolean isBold) {
        addTitle(jasperReport, jasperReport.getColumnWidth(), 20, text, expression, isBold);
    }

    public static void addTitle(JasperReport jasperReport, int width, int height, String text, String expression, boolean isBold) {
        Title title = jasperReport.getTitle();
        if (title == null) {
            title = new Title(null);
            jasperReport.setTitle(title);
        }
        Band band = title.getBand();
        if (band == null) {
            band = new Band("Stretch", new LinkedHashSet<>(), 0, new LinkedHashSet<>());
            title.setBand(band);
        }
        Set<TextField> textFields = band.getTextField();

        int y = JasperReportManager.getNextTitleY(jasperReport);

        int x = 0;
        Set<Property> property = new LinkedHashSet<>();
        property.add(new Property("com.jaspersoft.studio.unit.height", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.width", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.x", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.y", "px"));

        ReportElement reportElement = new ReportElement(property, x, y, width, height, UUID.randomUUID().toString());
        TextElement textElement = new TextElement(new Font("SVN-Times New Roman 2", null, isBold), "Center", "Middle");

        textFields.add(new TextField(reportElement, textElement, "\"" + text + "\"" + (expression == null ? "" : expression), null));

        // tính lại chiều cao columnHeader
        setTotalBandHeight(band, 50);

        // Tính toán tổng chiều rộng columnWidth
        setTotalColumnWidth(jasperReport);

        // Tính toán tổng chiều rộng titleWidth
        setTotalTitleWidth(jasperReport);
    }

    public static void addUnit(JasperReport jasperReport, String text, boolean isBold) {
        Title title = jasperReport.getTitle();
        if (title == null) {
            title = new Title(null);
            jasperReport.setTitle(title);
        }
        Band band = title.getBand();
        if (band == null) {
            band = new Band("Stretch", new LinkedHashSet<>(), 0, new LinkedHashSet<>());
            title.setBand(band);
        }
        Set<TextField> textFields = band.getTextField();
        Set<StaticText> staticTexts = band.getStaticText();

        int y = JasperReportManager.getNextUnitTitleY(textFields);

        int x = JasperReportManager.getNextUnitTitleX(jasperReport);

        Set<Property> property = new LinkedHashSet<>();
        property.add(new Property("com.jaspersoft.studio.unit.height", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.width", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.x", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.y", "px"));

        ReportElement reportElement = new ReportElement(property, x, y, 99, 30, UUID.randomUUID().toString());
        TextElement textElement = new TextElement(new Font("SVN-Times New Roman 2", null, isBold), "Right", null);

        staticTexts.add(new StaticText(reportElement, null, textElement, text, true));

        // tính lại chiều cao columnHeader
        setTotalBandHeight(band, 0);
    }

    private static int getNextUnitTitleX(JasperReport jasperReport) {
        return jasperReport.getColumnWidth() - 100;
    }

    private static int getNextUnitTitleY(Set<TextField> textField) {
        if (textField == null || textField.isEmpty()) {
            return 0;
        }
        Integer[] yValuesArray = textField.stream().map(item -> item.getReportElement().getY() + item.getReportElement().getHeight()).toArray(Integer[]::new);
        return yValuesArray[yValuesArray.length - 1] + 10;
    }


    public static void createPageHeader(JasperReport jasperReport, Set<TextField> textField, Set<StaticText> staticText) {
        jasperReport.setPageHeader(new PageHeader(new Band("Stretch", textField, getHeightBand(textField, staticText), staticText)));
    }

    public static void addColumnHeader(int level, JasperReport jasperReport, int x, int y, int width, int height, String text, boolean haveChildren) {
        ColumnHeader columnHeader = jasperReport.getColumnHeader();
        if (columnHeader == null) {
            columnHeader = new ColumnHeader(null);
            jasperReport.setColumnHeader(columnHeader);
        }
        Band band = columnHeader.getBand();
        if (band == null) {
            band = new Band("Stretch", new LinkedHashSet<>(), 0, new LinkedHashSet<>());
            columnHeader.setBand(band);
        }
        Set<StaticText> staticTexts = band.getStaticText();
        Set<Property> property = new LinkedHashSet<>();
        property.add(new Property("com.jaspersoft.studio.unit.height", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.width", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.x", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.y", "px"));

        ReportElement reportElement = new ReportElement(property, x, y, width, height, UUID.randomUUID().toString());

        TextElement textElement = new TextElement(new Font("SVN-Times New Roman 2", null, true), "Center", "Middle");
        staticTexts.add(new StaticText(reportElement, new Box(new Pen(1.0d, "Solid"), null, null), textElement, text, level, haveChildren));

        // tính lại chiều cao columnHeader
        setTotalBandHeight(band, 0);

        // Tính toán tổng chiều rộng columnWidth
        setTotalColumnWidth(jasperReport);

        // Tính toán tổng chiều rộng titleWidth
        setTotalTitleWidth(jasperReport);
    }

    private static void setTotalBandHeight(Band band, Integer baseHeight) {
        band.setHeight(baseHeight + getHeightBand(band.getTextField(), band.getStaticText()));
    }

    private static void setTotalTitleWidth(JasperReport jasperReport) {
        Title title = jasperReport.getTitle();
        if (title != null) {
            Band band = title.getBand();
            if (band != null) {
                Set<TextField> textField = band.getTextField();
                for (TextField text : textField) {
                    ReportElement reportElement = text.getReportElement();
                    if (reportElement != null) {
                        reportElement.setWidth(jasperReport.getColumnWidth());
                    }
                }
                Set<StaticText> staticText = band.getStaticText();
                for (StaticText text : staticText) {
                    ReportElement reportElement = text.getReportElement();
                    if (reportElement != null && text.getDonViTinh() != null && text.getDonViTinh()) {
                        reportElement.setX(getNextUnitTitleX(jasperReport));
                    }
                }
            }
        }
    }

    private static int getNextX(int level, Set<StaticText> staticTexts) {
        return staticTexts.stream().filter(it -> it.getLevel().equals(level)).map(item -> item.getReportElement().getX() + item.getReportElement().getWidth()).mapToInt(Integer::valueOf).max().orElse(0);
    }

    private static int getNextTitleY(JasperReport jasperReport) {
        Set<TextField> textField = jasperReport.getTitle().getBand().getTextField();
        Set<StaticText> staticText = jasperReport.getTitle().getBand().getStaticText();

        if (textField == null || textField.isEmpty() && staticText == null || staticText.isEmpty()) {
            return 0;
        }
        Integer[] yTextFieldValuesArray = textField.stream().map(item -> item.getReportElement().getY() + item.getReportElement().getHeight()).toArray(Integer[]::new);
        Integer[] yStaticTextValuesArray = staticText.stream().map(item -> item.getReportElement().getY() + item.getReportElement().getHeight()).toArray(Integer[]::new);
        Integer[] combinedArray = Stream.concat(
                        Stream.of(yTextFieldValuesArray),
                        Stream.of(yStaticTextValuesArray))
                .toArray(Integer[]::new);
        Arrays.sort(combinedArray);

        return combinedArray[combinedArray.length - 1] + 10;
    }


    private static int getNextY(Set<StaticText> staticTexts) {
        Integer[] yValuesArray = staticTexts.stream().map(item -> item.getReportElement().getY()).toArray(Integer[]::new);
        return yValuesArray[yValuesArray.length - 1];
    }

    private static Integer getHeightBand(Set<TextField> textField, Set<StaticText> staticText) {
        int maxTextField = 0;
        int minTextField = 0;
        int maxStaticText = 0;
        int minStaticText = 0;
        if (textField != null) {
            maxTextField = textField.stream().map(item -> item.getReportElement().getY() + item.getReportElement().getHeight()).mapToInt(Integer::valueOf).max().orElse(0);
            minTextField = textField.stream().map(item -> item.getReportElement().getY()).mapToInt(Integer::valueOf).min().orElse(0);
        }
        if (staticText != null) {
            maxStaticText = staticText.stream().map(item -> item.getReportElement().getY() + item.getReportElement().getHeight()).mapToInt(Integer::valueOf).max().orElse(0);
            minStaticText = staticText.stream().map(item -> item.getReportElement().getY()).mapToInt(Integer::valueOf).min().orElse(0);
        }
        Integer min = Math.min(minTextField, minStaticText);
        Integer max = Math.max(maxTextField, maxStaticText);
        return max - min;
    }

    private static int getTotalColumn(List<HeaderColumn> rowNames) {
        List<HeaderColumn> flattened = HeaderColumn.flattenList(rowNames);
        flattened = flattened.stream().filter(item -> item.getChildren().size() == 0).collect(Collectors.toList());
        return flattened.size();
    }

    private static int getSizeCell(HeaderColumn h) {
        // size
        int sizeCell = h.getChildren().size();
        for (HeaderColumn hc : h.getChildren()) {
            if (hc.getChildren() != null && hc.getChildren().size() > 0) {
                sizeCell = sizeCell - 1 + getSizeCell(hc);
            }
        }
        return sizeCell;
    }

    public static void setColumnHeader(JasperReport jasperReport, List<HeaderColumn> headerColumns) {
        // xử lý Column Header
        handleSetColumnHeader(jasperReport, headerColumns);

        // xử lý Field và Variable
        List<HeaderColumn> showList = HeaderColumn.getShowList(headerColumns);
        for (HeaderColumn sl : showList) {
            JasperReportManager.addField(jasperReport, sl.getVariableName(), sl.getClazz());
            JasperReportManager.addVariable(jasperReport, "col_" + sl.getVariableName(), sl.getClazz(), "$F{" + sl.getVariableName() + "}");
            if (sl.getIsSum() != null && sl.getIsSum()) {
                JasperReportManager.addVariable(jasperReport, "total_" + sl.getVariableName(), sl.getClazz(), "$F{" + sl.getVariableName() + "}", "Sum");
            }
        }

        // xử lý Detail
        handleSetDetail(jasperReport, headerColumns, showList);

        // xử lý Summary
        handleSetSummary(jasperReport, headerColumns, showList);
    }

    private static void handleSetSummary(JasperReport jasperReport, List<HeaderColumn> headerColumns, List<HeaderColumn> showList) {
        Summary summary = jasperReport.getSummary();
        if (summary == null) {
            summary = new Summary(new Band("Stretch", new LinkedHashSet<>(), 33, new LinkedHashSet<>()));
            jasperReport.setSummary(summary);
        }
        boolean isFirst = true;
        boolean isCreatedTitle = false;
        int w = 0;
        for (HeaderColumn hc : showList) {
            if (hc.getIsSum() != null && hc.getIsSum()) {
                if (!isCreatedTitle) {
                    createCellTitle(summary, hc, w);
                    isCreatedTitle = true;
                    isFirst = false;
                }
                createCellSum(summary, hc);
            } else {
                if (isFirst && !isCreatedTitle) {
                    // sum with
                    w += getWidth(hc);
                } else {
                    createCellBlank(summary, hc);
                }
            }
        }
    }

    private static void createCellBlank(Summary summary, HeaderColumn hc) {
        Set<Property> property = new LinkedHashSet<>();
        property.add(new Property("com.jaspersoft.studio.unit.height", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.width", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.x", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.y", "px"));
        property.add(new Property("net.sf.jasperreports.export.xls.pattern", "#,##0.##"));

        int y = 0;
        int x = JasperReportManager.getNextSummaryX(summary.getBand().getTextField());
        ReportElement reportElement = new ReportElement(property, x, y, getWidth(hc), 33, UUID.randomUUID().toString());
        TextElement textElement = new TextElement(new Font("SVN-Times New Roman 2", null, true), "Center", "Middle");
        Box box = new Box(new Pen(1.0d, "Solid"), 4, 4);
        summary.getBand().getTextField().add(new TextField(reportElement, textElement, null, box, null, true));
    }

    private static void createCellTitle(Summary summary, HeaderColumn hc, int w) {
        Set<Property> property = new LinkedHashSet<>();
        property.add(new Property("com.jaspersoft.studio.unit.height", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.width", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.x", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.y", "px"));
        property.add(new Property("net.sf.jasperreports.export.xls.pattern", "#,##0.##"));

        int y = 0;
        int x = 0;
        ReportElement reportElement = new ReportElement(property, x, y, w, 33, UUID.randomUUID().toString());
        TextElement textElement = new TextElement(new Font("SVN-Times New Roman 2", null, true), "Center", "Middle");
        Box box = new Box(new Pen(1.0d, "Solid"), 4, 4);
        summary.getBand().getTextField().add(new TextField(reportElement, textElement, "\"TỔNG\"", box, null, true));
    }

    private static void createCellSum(Summary summary, HeaderColumn hc) {
        Set<Property> property = new LinkedHashSet<>();
        property.add(new Property("com.jaspersoft.studio.unit.height", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.width", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.x", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.y", "px"));
        property.add(new Property("net.sf.jasperreports.export.xls.pattern", "#,##0.##"));

        int y = 0;
        int x = JasperReportManager.getNextSummaryX(summary.getBand().getTextField());
        ReportElement reportElement = new ReportElement(property, x, y, getWidth(hc), 33, UUID.randomUUID().toString());
        String pattern = null;
        TextElement textElement = new TextElement(new Font("SVN-Times New Roman 2", null, true), "Right", "Middle");
        if (hc.getPattern() != null) {
            pattern = hc.getPattern();
        } else {
            pattern = "#,##0.##";
        }
        Box box = new Box(new Pen(1.0d, "Solid"), 4, 4);
        summary.getBand().getTextField().add(new TextField(reportElement, textElement, "$V{total_" + hc.getVariableName() + "}", box, pattern, true));
    }

    private static int getNextSummaryX(Set<TextField> textField) {
        return textField.stream().map(item -> item.getReportElement().getX() + item.getReportElement().getWidth()).mapToInt(Integer::valueOf).max().orElse(0);
    }

    private static void handleSetDetail(JasperReport jasperReport, List<HeaderColumn> headerColumns, List<HeaderColumn> showList) {
        Detail detail = jasperReport.getDetail();
        if (detail == null) {
            detail = new Detail(new Band("Stretch", new LinkedHashSet<>(), 33, new LinkedHashSet<>()));
            jasperReport.setDetail(detail);
        }
        for (HeaderColumn hc : showList) {
            Set<Property> property = new LinkedHashSet<>();
            property.add(new Property("com.jaspersoft.studio.unit.height", "px"));
            property.add(new Property("com.jaspersoft.studio.unit.width", "px"));
            property.add(new Property("com.jaspersoft.studio.unit.x", "px"));
            property.add(new Property("com.jaspersoft.studio.unit.y", "px"));
            property.add(new Property("net.sf.jasperreports.export.xls.pattern", "#,##0.##"));

            int y = 0;
            int x = JasperReportManager.getNextDetailX(detail.getBand().getTextField());
            ReportElement reportElement = new ReportElement(property, x, y, getWidth(hc), 33, UUID.randomUUID().toString());
            TextElement textElement = null;
            String pattern = null;
            if (hc.getClazz().equalsIgnoreCase("java.lang.String")) {
                textElement = new TextElement(new Font("SVN-Times New Roman 2", null, true), "Left", "Middle");
            } else {
                textElement = new TextElement(new Font("SVN-Times New Roman 2", null, true), "Right", "Middle");
                if (hc.getPattern() != null) {
                    pattern = hc.getPattern();
                } else {
                    pattern = "#,##0.##";
                }
            }
            Box box = new Box(new Pen(1.0d, "Solid"), 4, 4);
            detail.getBand().getTextField().add(new TextField(reportElement, textElement, "$V{col_" + hc.getVariableName() + "}", box, pattern, true));
        }
    }

    private static int getNextDetailX(Set<TextField> textField) {
        return textField.stream().map(item -> item.getReportElement().getX() + item.getReportElement().getWidth()).mapToInt(Integer::valueOf).max().orElse(0);
    }

    private static int getNextDetailY(Set<TextField> textField) {
        if (textField == null || textField.isEmpty()) {
            return 0;
        }
        Integer[] yValuesArray = textField.stream().map(item -> item.getReportElement().getY() + item.getReportElement().getHeight()).toArray(Integer[]::new);
        return yValuesArray[yValuesArray.length - 1] + 10;
    }

    private static void handleSetColumnHeader(JasperReport jasperReport, List<HeaderColumn> headerColumns) {
        List<List<HeaderColumn>> lists = HeaderColumn.flattenListList2(headerColumns);
        int totalColumn = getTotalColumn(headerColumns);
        boolean save = true;
        int x = 0;
        boolean allow = false;
        for (int i = 0; i < HeaderColumn.getTotalLevels(headerColumns); i++) {
            List<HeaderColumn> h = lists.get(i);
            int currentPoint = 0;
            for (int n = 0; n < totalColumn; n++) {
                if (jasperReport.getColumnHeader() == null) {
                    jasperReport.setColumnHeader(new ColumnHeader(new Band("Stretch", new LinkedHashSet<>(), 0, new LinkedHashSet<>())));
                }
                if (h.size() > currentPoint && h.get(currentPoint).getChildren().size() > 0) {
                    if (save) {
                        x = getNextX(i, jasperReport.getColumnHeader().getBand().getStaticText());
                        save = false;
                    }
                    addColumnHeader(i, jasperReport,
                            getNextX(i, jasperReport.getColumnHeader().getBand().getStaticText()),
                            33 * i,
                            getWidth(h.get(currentPoint)), 33,
                            h.get(currentPoint).getColumnName(), true);

                } else if (h.size() > currentPoint) {
                    addColumnHeader(i, jasperReport,
                            allow ? x : isOverride(i, jasperReport.getColumnHeader().getBand().getStaticText()) ? getNextXOverride(i, jasperReport.getColumnHeader().getBand().getStaticText()) : getNextX(i, jasperReport.getColumnHeader().getBand().getStaticText()),
                            33 * i,
                            getWidth(h.get(currentPoint)), 33 * (HeaderColumn.getTotalLevels(headerColumns) - i),
                            h.get(currentPoint).getColumnName(), false);
                    if (allow) {
                        save = true;
                        x = 0;
                        allow = false;
                    }
                }
                currentPoint++;
            }
            if (!save) {
                allow = true;
            }
        }
    }

    private static int getNextXOverride(int i, Set<StaticText> staticText) {
        int nextX = getNextX(i, staticText);
        Set<StaticText> collect = staticText.stream().filter(it -> it.getLevel() == i || !it.getHaveChildren()).filter(item -> item.getReportElement().getX() <= nextX && nextX < item.getReportElement().getX() + item.getReportElement().getWidth()).collect(Collectors.toSet());
        Optional<StaticText> first = collect.stream().findFirst();
        return first.map(text -> text.getReportElement().getX() + text.getReportElement().getWidth()).orElse(0);
    }

    private static boolean isOverride(int i, Set<StaticText> staticText) {
        int nextX = getNextX(i, staticText);
        Set<StaticText> collect = staticText.stream().filter(it -> it.getLevel() == i || !it.getHaveChildren()).filter(item -> item.getReportElement().getX() <= nextX && nextX < item.getReportElement().getX() + item.getReportElement().getWidth()).collect(Collectors.toSet());
        return !collect.isEmpty();
    }

    private static int getWidth(HeaderColumn headerColumn) {
        if (headerColumn.getWidth() != null) {
            return headerColumn.getWidth();
        }
        int width = headerColumn.getColumnName().length() * 10;
        if (headerColumn.getChildren() != null && !headerColumn.getChildren().isEmpty()) {
            int w = 0;
            for (HeaderColumn hc : headerColumn.getChildren()) {
                if (hc.getChildren() != null && !hc.getChildren().isEmpty()) {
                    w += getWidth(hc);
                } else {
                    if (hc.getWidth() != null) {
                        w += hc.getWidth();
                    } else {
                        w += hc.getColumnName().length() * 10;
                    }
                }
            }
            width = w;
        }
        return width;
    }

    public static void addDepartment(JasperReport jasperReport, String text) {
        addDepartment(jasperReport, text, 10, getWidthTitleDepartment(jasperReport, text));
    }

    private static int getWidthTitleDepartment(JasperReport jasperReport, String text) {
        if (jasperReport.getTitle() != null) {
            if (jasperReport.getTitle().getBand() != null) {
                if (jasperReport.getTitle().getBand().getStaticText() != null && !jasperReport.getTitle().getBand().getStaticText().isEmpty()) {
                    Optional<StaticText> staticText = jasperReport.getTitle().getBand().getStaticText().stream().findFirst();
                    if (staticText.isPresent()) {
                        return staticText.get().getReportElement().getWidth();
                    }
                }
            }
        }
        return text.length() * 10;
    }

    public static void addDepartment(JasperReport jasperReport, String text, int fontSize) {
        addDepartment(jasperReport, text, fontSize, getWidthTitleDepartment(jasperReport, text));
    }

    public static void addDepartment(JasperReport jasperReport, String text, int fontSize, int width) {
        Title title = jasperReport.getTitle();
        if (title == null) {
            title = new Title(null);
            jasperReport.setTitle(title);
        }
        Band band = title.getBand();
        if (band == null) {
            band = new Band("Stretch", new LinkedHashSet<>(), 0, new LinkedHashSet<>());
            title.setBand(band);
        }
        Set<StaticText> staticTexts = band.getStaticText();

        int y = getNextDepartmentTitleY(staticTexts, 0);

        int x = 33;

        Set<Property> property = new LinkedHashSet<>();
        property.add(new Property("com.jaspersoft.studio.unit.height", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.width", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.x", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.y", "px"));

        ReportElement reportElement = new ReportElement(property, x, y, width, 30, UUID.randomUUID().toString());
        TextElement textElement = new TextElement(new Font("SVN-Times New Roman 2", fontSize, true), "Center", "Middle");

        staticTexts.add(new StaticText(reportElement, null, textElement, text));

        // tính lại chiều cao columnHeader
        setTotalBandHeight(band, 0);
    }

    private static int getNextDepartmentTitleY(Set<StaticText> staticTexts) {
        return getNextDepartmentTitleY(staticTexts, 10);
    }

    private static int getNextDepartmentTitleY(Set<StaticText> staticTexts, int margin) {
        if (staticTexts == null || staticTexts.isEmpty()) {
            return 0;
        }
        Integer[] yValuesArray = staticTexts.stream().map(item -> item.getReportElement().getY() + item.getReportElement().getHeight()).toArray(Integer[]::new);
        return yValuesArray[yValuesArray.length - 1] + margin;
    }

    public static void addTableTitle(JasperReport jasperReport, String text,  boolean isBold) {
        Title title = jasperReport.getTitle();
        if (title == null) {
            title = new Title(null);
            jasperReport.setTitle(title);
        }
        Band band = title.getBand();
        if (band == null) {
            band = new Band("Stretch", new LinkedHashSet<>(), 0, new LinkedHashSet<>());
            title.setBand(band);
        }
        Set<TextField> textFields = band.getTextField();
        Set<StaticText> staticTexts = band.getStaticText();

        int y = JasperReportManager.getNextUnitTitleY(textFields);

        int x = 0;

        Set<Property> property = new LinkedHashSet<>();
        property.add(new Property("com.jaspersoft.studio.unit.height", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.width", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.x", "px"));
        property.add(new Property("com.jaspersoft.studio.unit.y", "px"));

        ReportElement reportElement = new ReportElement(property, x, y, jasperReport.getColumnWidth(), 20, UUID.randomUUID().toString());
        TextElement textElement = new TextElement(new Font("SVN-Times New Roman 2", null, isBold), "Left", null);

        staticTexts.add(new StaticText(reportElement, null, textElement, text, false));

        // tính lại chiều cao columnHeader
        setTotalBandHeight(band, 0);
    }
}

