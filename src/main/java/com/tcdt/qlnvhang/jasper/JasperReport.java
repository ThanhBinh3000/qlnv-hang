package com.tcdt.qlnvhang.jasper;


import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@XmlAccessorType(XmlAccessType.FIELD)
class Property {
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String value;

    public Property(String name, String value) {
        this.name = name;
        this.value = value;
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
class Parameter {
    @XmlAttribute
    private String name;
    @XmlAttribute(name = "class")
    private String _class;

    public Parameter(String name, String _class) {
        this.name = name;
        this._class = _class;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parameter parameter = (Parameter) o;
        return Objects.equals(name, parameter.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
class Field {
    @XmlAttribute
    private String name;
    @XmlAttribute(name = "class")
    private String _class;

    public Field(String name, String _class) {
        this.name = name;
        this._class = _class;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return Objects.equals(name, field.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
class Variable {
    @XmlElement(name = "variableExpression", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    @XmlJavaTypeAdapter(CDataAdapter.class)
    private String variableExpression;
    @XmlAttribute
    private String name;
    @XmlAttribute(name = "class")
    private String _class;

    @XmlAttribute(name = "calculation")
    private String calculation;

    public Variable(String name, String _class, String variableExpression) {
        this.variableExpression = variableExpression;
        this.name = name;
        this._class = _class;
    }

    public Variable(String name, String _class, String variableExpression, String calculation) {
        this.variableExpression = variableExpression;
        this.name = name;
        this._class = _class;
        this.calculation = calculation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return Objects.equals(name, variable.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
class Band {
    @XmlAttribute
    private String splitType;
    @XmlElement(name = "textField", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private Set<TextField> textField;
    @XmlAttribute
    private Integer height;
    @XmlElement(name = "staticText", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private Set<StaticText> staticText;

    public Band(String splitType, Set<TextField> textField, Integer height, Set<StaticText> staticText) {
        this.splitType = splitType;
        this.textField = textField;
        this.height = height;
        this.staticText = staticText;
    }

    public String getSplitType() {
        return splitType;
    }

    public void setSplitType(String splitType) {
        this.splitType = splitType;
    }

    public Set<TextField> getTextField() {
        return textField;
    }

    public void setTextField(Set<TextField> textField) {
        this.textField = textField;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Set<StaticText> getStaticText() {
        return staticText;
    }

    public void setStaticText(Set<StaticText> staticText) {
        this.staticText = staticText;
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
class Background {
    @XmlElement(name = "band", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private Band band;

    public Background(Band band) {
        this.band = band;
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
class ReportElement {
    @XmlElement(name = "property", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private Set<Property> property;
    @XmlAttribute
    private Integer x;
    @XmlAttribute
    private Integer y;
    @XmlAttribute
    private Integer width;
    @XmlAttribute
    private Integer height;
    @XmlAttribute
    private String uuid;

    public ReportElement(Set<Property> property, Integer x, Integer y, Integer width, Integer height, String uuid) {
        this.property = property;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.uuid = uuid;
    }

    public Set<Property> getProperty() {
        return property;
    }

    public void setProperty(Set<Property> property) {
        this.property = property;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
class Font {
    @XmlAttribute
    private String fontName;
    @XmlAttribute
    private Integer size;
    @XmlAttribute
    private Boolean isBold;

    public Font(String fontName, Integer size, Boolean isBold) {
        this.fontName = fontName;
        this.size = size;
        this.isBold = isBold;
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
class TextElement {
    @XmlElement(name = "font", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private Font font;
    @XmlAttribute
    private String textAlignment;
    @XmlAttribute
    private String verticalAlignment;

    public TextElement(Font font, String textAlignment, String verticalAlignment) {
        this.font = font;
        this.textAlignment = textAlignment;
        this.verticalAlignment = verticalAlignment;
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"reportElement", "box", "textElement", "textFieldExpression"})
class TextField {
    @XmlElement(name = "reportElement", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private ReportElement reportElement;
    @XmlElement(name = "textElement", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private TextElement textElement;
    @XmlElement(name = "textFieldExpression", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    @XmlJavaTypeAdapter(CDataAdapter.class)
    private String textFieldExpression;
    @XmlElement(name = "box", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private Box box;
    @XmlAttribute
    private String pattern;
    @XmlAttribute
    private Boolean isBlankWhenNull;

    public TextField(ReportElement reportElement, TextElement textElement, String textFieldExpression, Box box) {
        this.reportElement = reportElement;
        this.textElement = textElement;
        this.textFieldExpression = textFieldExpression;
        this.box = box;
    }

    public TextField(ReportElement reportElement, TextElement textElement, String textFieldExpression, Box box, String pattern, Boolean isBlankWhenNull) {
        this.reportElement = reportElement;
        this.textElement = textElement;
        this.textFieldExpression = textFieldExpression;
        this.box = box;
        this.pattern = pattern;
        this.isBlankWhenNull = isBlankWhenNull;
    }

    public ReportElement getReportElement() {
        return reportElement;
    }

    public void setReportElement(ReportElement reportElement) {
        this.reportElement = reportElement;
    }

    public TextElement getTextElement() {
        return textElement;
    }

    public void setTextElement(TextElement textElement) {
        this.textElement = textElement;
    }

    public String getTextFieldExpression() {
        return textFieldExpression;
    }

    public void setTextFieldExpression(String textFieldExpression) {
        this.textFieldExpression = textFieldExpression;
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
class Title {
    @XmlElement(name = "band", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private Band band;

    public Title(Band band) {
        this.band = band;
    }

    public Band getBand() {
        return band;
    }

    public void setBand(Band band) {
        this.band = band;
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
class PageHeader {
    @XmlElement(name = "band", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private Band band;

    public PageHeader(Band band) {
        this.band = band;
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
class Pen {
    @XmlAttribute
    private Double lineWidth;
    @XmlAttribute
    private String lineStyle;

    public Pen(Double lineWidth, String lineStyle) {
        this.lineWidth = lineWidth;
        this.lineStyle = lineStyle;
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
class Box {
    @XmlElement(name = "pen", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private Pen pen;
    @XmlAttribute
    private Integer leftPadding;
    @XmlAttribute
    private Integer rightPadding;

    public Box(Pen pen, Integer leftPadding, Integer rightPadding) {
        this.pen = pen;
        this.leftPadding = leftPadding;
        this.rightPadding = rightPadding;
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
class StaticText {
    @XmlElement(name = "reportElement", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private ReportElement reportElement;
    @XmlElement(name = "box", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private Box box;
    @XmlElement(name = "textElement", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private TextElement textElement;
    @XmlElement(name = "text", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    @XmlJavaTypeAdapter(CDataAdapter.class)
    private String text;
    @XmlTransient
    private Integer level;
    @XmlTransient
    private Boolean haveChildren;
    @XmlTransient
    private Boolean isDonViTinh;

    public StaticText(ReportElement reportElement, Box box, TextElement textElement, String text) {
        this.reportElement = reportElement;
        this.box = box;
        this.textElement = textElement;
        this.text = text;
    }

    public StaticText(ReportElement reportElement, Box box, TextElement textElement, String text, Integer level) {
        this.reportElement = reportElement;
        this.box = box;
        this.textElement = textElement;
        this.text = text;
        this.level = level;
    }

    public StaticText(ReportElement reportElement, Box box, TextElement textElement, String text, Integer level, Boolean haveChildren) {
        this.reportElement = reportElement;
        this.box = box;
        this.textElement = textElement;
        this.text = text;
        this.level = level;
        this.haveChildren = haveChildren;
    }

    public StaticText(ReportElement reportElement, Box box, TextElement textElement, String text, Boolean isDonViTinh) {
        this.reportElement = reportElement;
        this.box = box;
        this.textElement = textElement;
        this.text = text;
        this.isDonViTinh = isDonViTinh;
    }

    public ReportElement getReportElement() {
        return reportElement;
    }

    public void setReportElement(ReportElement reportElement) {
        this.reportElement = reportElement;
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public TextElement getTextElement() {
        return textElement;
    }

    public void setTextElement(TextElement textElement) {
        this.textElement = textElement;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getHaveChildren() {
        return haveChildren;
    }

    public void setHaveChildren(Boolean haveChildren) {
        this.haveChildren = haveChildren;
    }

    public Boolean getDonViTinh() {
        return isDonViTinh;
    }

    public void setDonViTinh(Boolean donViTinh) {
        isDonViTinh = donViTinh;
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
class ColumnHeader {
    @XmlElement(name = "band", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private Band band;

    public ColumnHeader(Band band) {
        this.band = band;
    }

    public Band getBand() {
        return band;
    }

    public void setBand(Band band) {
        this.band = band;
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
class Detail {
    @XmlElement(name = "band", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private Band band;

    public Detail(Band band) {
        this.band = band;
    }

    public Band getBand() {
        return band;
    }

    public void setBand(Band band) {
        this.band = band;
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
class LastPageFooter {
    @XmlElement(name = "band", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private Band band;

    public LastPageFooter(Band band) {
        this.band = band;
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
class Summary {
    @XmlElement(name = "band", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private Band band;

    public Summary(Band band) {
        this.band = band;
    }

    public Band getBand() {
        return band;
    }

    public void setBand(Band band) {
        this.band = band;
    }
}

@XmlRootElement(name = "jasperReport", namespace = "http://jasperreports.sourceforge.net/jasperreports")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"property", "parameter", "queryString", "field", "variable", "background", "title", "pageHeader", "columnHeader", "detail", "lastPageFooter", "summary"})
public class JasperReport {
    @XmlAttribute(name = "schemaLocation", namespace = "http://www.w3.org/2001/XMLSchema-instance")
    private String schemaLocation;
    @XmlElement(name = "property", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private Property property;
    @XmlElement(name = "parameter", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private Set<Parameter> parameter;
    @XmlElement(name = "queryString", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    @XmlJavaTypeAdapter(CDataAdapter.class)
    private String queryString;
    @XmlElement(name = "field", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private Set<Field> field;
    @XmlElement(name = "variable", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private Set<Variable> variable;
    @XmlElement(name = "background", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private Background background;
    @XmlElement(name = "title", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private Title title;
    @XmlElement(name = "pageHeader", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private PageHeader pageHeader;
    @XmlElement(name = "columnHeader", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private ColumnHeader columnHeader;
    @XmlElement(name = "detail", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private Detail detail;
    @XmlElement(name = "lastPageFooter", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private LastPageFooter lastPageFooter;
    @XmlElement(name = "summary", namespace = "http://jasperreports.sourceforge.net/jasperreports")
    private Summary summary;
    @XmlAttribute
    private String name;
    @XmlAttribute
    private Integer pageWidth;
    @XmlAttribute
    private Integer pageHeight;
    @XmlAttribute
    private String orientation;
    @XmlAttribute
    private Integer columnWidth;
    @XmlAttribute
    private Integer leftMargin;
    @XmlAttribute
    private Integer rightMargin;
    @XmlAttribute
    private Integer topMargin;
    @XmlAttribute
    private Integer bottomMargin;
    @XmlAttribute
    private Boolean isIgnorePagination;
    @XmlAttribute
    private String uuid;

    public JasperReport() {
        this.name = "Blank_A4";
        this.pageWidth = 2100;
        this.pageHeight = 700;
        this.orientation = "Landscape";
        this.columnWidth = 2060;
        this.leftMargin = 20;
        this.rightMargin = 20;
        this.topMargin = 20;
        this.bottomMargin = 20;
        this.isIgnorePagination = true;
        this.uuid = UUID.randomUUID().toString();
    }

    public JasperReport(String name, int pageWidth, int pageHeight, String orientation) {
        this.name = name;
        this.pageWidth = pageWidth;
        this.pageHeight = pageHeight;
        this.orientation = orientation;
        this.columnWidth = 2060;
        this.leftMargin = 20;
        this.rightMargin = 20;
        this.topMargin = 20;
        this.bottomMargin = 20;
        this.isIgnorePagination = true;
        this.uuid = UUID.randomUUID().toString();
    }

    public String getSchemaLocation() {
        return schemaLocation;
    }

    public void setSchemaLocation(String schemaLocation) {
        this.schemaLocation = schemaLocation;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Set<Parameter> getParameter() {
        return parameter;
    }

    public void setParameter(Set<Parameter> parameter) {
        this.parameter = parameter;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public Set<Field> getField() {
        return field;
    }

    public void setField(Set<Field> field) {
        this.field = field;
    }

    public Set<Variable> getVariable() {
        return variable;
    }

    public void setVariable(Set<Variable> variable) {
        this.variable = variable;
    }

    public Background getBackground() {
        return background;
    }

    public void setBackground(Background background) {
        this.background = background;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public PageHeader getPageHeader() {
        return pageHeader;
    }

    public void setPageHeader(PageHeader pageHeader) {
        this.pageHeader = pageHeader;
    }

    public ColumnHeader getColumnHeader() {
        return columnHeader;
    }

    public void setColumnHeader(ColumnHeader columnHeader) {
        this.columnHeader = columnHeader;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public LastPageFooter getLastPageFooter() {
        return lastPageFooter;
    }

    public void setLastPageFooter(LastPageFooter lastPageFooter) {
        this.lastPageFooter = lastPageFooter;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(Integer pageWidth) {
        this.pageWidth = pageWidth;
    }

    public Integer getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(Integer pageHeight) {
        this.pageHeight = pageHeight;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public Integer getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(Integer columnWidth) {
        this.columnWidth = columnWidth;
    }

    public Integer getLeftMargin() {
        return leftMargin;
    }

    public void setLeftMargin(Integer leftMargin) {
        this.leftMargin = leftMargin;
    }

    public Integer getRightMargin() {
        return rightMargin;
    }

    public void setRightMargin(Integer rightMargin) {
        this.rightMargin = rightMargin;
    }

    public Integer getTopMargin() {
        return topMargin;
    }

    public void setTopMargin(Integer topMargin) {
        this.topMargin = topMargin;
    }

    public Integer getBottomMargin() {
        return bottomMargin;
    }

    public void setBottomMargin(Integer bottomMargin) {
        this.bottomMargin = bottomMargin;
    }

    public Boolean getIgnorePagination() {
        return isIgnorePagination;
    }

    public void setIgnorePagination(Boolean ignorePagination) {
        isIgnorePagination = ignorePagination;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
