package com.tcdt.qlnvhang.jasper;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class HeaderColumn {
    private String columnName;
    private String variableName;
    private String clazz;
    private List<HeaderColumn> children = new ArrayList<>();
    private int level;
    private Integer width;
    private String pattern;
    private Boolean isSum;

    public HeaderColumn(String columnName, int level) {
        this.columnName = columnName;
        this.level = level;
    }

    public HeaderColumn(String columnName, int level, Boolean isSum) {
        this.columnName = columnName;
        this.level = level;
        this.isSum = isSum;
    }

    public HeaderColumn(String columnName, int level, String variableName, String clazz) {
        this.columnName = columnName;
        this.variableName = variableName;
        this.clazz = clazz;
        this.level = level;
    }

    public HeaderColumn(String columnName, int level, String variableName, String clazz, Boolean isSum) {
        this.columnName = columnName;
        this.variableName = variableName;
        this.clazz = clazz;
        this.level = level;
        this.isSum = isSum;
    }

    public HeaderColumn(String columnName, int level, String variableName, String clazz, Integer width) {
        this.columnName = columnName;
        this.variableName = variableName;
        this.clazz = clazz;
        this.level = level;
        this.width = width;
    }

    public HeaderColumn(String columnName, int level, String variableName, String clazz, Integer width, Boolean isSum) {
        this.columnName = columnName;
        this.variableName = variableName;
        this.clazz = clazz;
        this.level = level;
        this.width = width;
        this.isSum = isSum;
    }

    public HeaderColumn(String columnName, int level, String variableName, String clazz, Integer width, String pattern) {
        this.columnName = columnName;
        this.variableName = variableName;
        this.clazz = clazz;
        this.children = children;
        this.level = level;
        this.width = width;
        this.pattern = pattern;
    }

    public HeaderColumn(String columnName, int level, String variableName, String clazz, Integer width, String pattern, Boolean isSum) {
        this.columnName = columnName;
        this.variableName = variableName;
        this.clazz = clazz;
        this.children = children;
        this.level = level;
        this.width = width;
        this.pattern = pattern;
        this.isSum = isSum;
    }

    public HeaderColumn(String columnName, int level, String variableName, String clazz, String pattern) {
        this.columnName = columnName;
        this.variableName = variableName;
        this.clazz = clazz;
        this.children = children;
        this.level = level;
        this.width = width;
        this.pattern = pattern;
    }

    public HeaderColumn(String columnName, int level, String variableName, String clazz, String pattern, Boolean isSum) {
        this.columnName = columnName;
        this.variableName = variableName;
        this.clazz = clazz;
        this.level = level;
        this.pattern = pattern;
        this.isSum = isSum;
    }

    public static List<HeaderColumn> flattenList(List<HeaderColumn> headerColumns) {
        List<HeaderColumn> flatList = new ArrayList<>();
        for (HeaderColumn headerColumn : headerColumns) {
            flatten(headerColumn, flatList);
        }
        return flatList;
    }

    private static void flatten(HeaderColumn headerColumn, List<HeaderColumn> flatList) {
        flatList.add(headerColumn);
        for (HeaderColumn child : headerColumn.getChildren()) {
            flatten(child, flatList);
        }
    }

    public static List<List<HeaderColumn>> flattenListList(List<HeaderColumn> headerColumns) {
        List<List<HeaderColumn>> flatListList = new ArrayList<>();
        int totalLevels = getTotalLevels(headerColumns);
        for (int i = 0; i < totalLevels; i++) {
            List<HeaderColumn> flatList = flattenList(headerColumns);
            int finalI = i;
            List<HeaderColumn> filter = flatList.stream().filter(item -> item.getLevel() == finalI).collect(Collectors.toList());
            List<HeaderColumn> result = new ArrayList<>();
            if (i > 0) {
                List<HeaderColumn> headerLast = flatListList.get(i - 1);
                for (HeaderColumn h : headerLast) {
                    if (h.getChildren().size() > 0) {
                        for (HeaderColumn c : h.getChildren()) {
                            result.add(c);
                        }
                    } else {
                        result.add(h);
                    }
                }
            } else {
                result = filter;
            }
            flatListList.add(result);
        }
        return flatListList;
    }

    public static List<List<HeaderColumn>> flattenListList2(List<HeaderColumn> headerColumns) {
        List<List<HeaderColumn>> flatListList = new ArrayList<>();
        int totalLevels = getTotalLevels(headerColumns);
        for (int i = 0; i < totalLevels; i++) {
            List<HeaderColumn> flatList = flattenList(headerColumns);
            int finalI = i;
            List<HeaderColumn> filter = flatList.stream().filter(item -> item.getLevel() == finalI).collect(Collectors.toList());
            flatListList.add(filter);
        }
        return flatListList;
    }

    public static int getTotalLevels(HeaderColumn headerColumns) {
        return calculateDepth(headerColumns, 1);
    }

    public static int getTotalLevels(List<HeaderColumn> headerColumns) {
        int max = 1;
        for (HeaderColumn r : headerColumns) {
            int c = calculateDepth(r, 1);
            max = Math.max(max, c);
        }
        return max;
    }

    // Recursive helper method for depth calculation
    private static int calculateDepth(HeaderColumn headerColumn, int currentDepth) {
        if (headerColumn.getChildren().isEmpty()) {
            return currentDepth;
        } else {
            int maxDepth = currentDepth;
            for (HeaderColumn child : headerColumn.getChildren()) {
                int childDepth = calculateDepth(child, currentDepth + 1);
                maxDepth = Math.max(maxDepth, childDepth);
            }
            return maxDepth;
        }
    }

    public static List<HeaderColumn> getShowList(List<HeaderColumn> headerColumns) {
        List<HeaderColumn> flattenList = HeaderColumn.flattenList(headerColumns);
        return flattenList.stream().filter(item -> item.getChildren().isEmpty()).collect(Collectors.toList());
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public List<HeaderColumn> getChildren() {
        return children;
    }

    public void setChildren(List<HeaderColumn> children) {
        this.children = children;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
