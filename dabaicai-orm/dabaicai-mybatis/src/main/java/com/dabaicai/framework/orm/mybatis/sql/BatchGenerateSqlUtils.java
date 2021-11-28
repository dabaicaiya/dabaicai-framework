package com.dabaicai.framework.orm.mybatis.sql;

import com.baomidou.mybatisplus.annotation.TableField;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author zhangyanbing
 * @Description: 批量操作数据库工具类
 * @date 2021/3/3 14:04
 */
public class BatchGenerateSqlUtils {

    /**
     * 默认一次性插入数量
     */
    private static final Integer DEFAULT_SIZE = 500;

    private static List<String> suffixList = new ArrayList<>();

    /**
     * 获取忽略的类名后缀
     *
     * @return
     */
    private static List<String> getSuffixList() {
        if (!suffixList.isEmpty()) {
            return suffixList;
        }
        suffixList.add("DO");
        suffixList.add("Do");
        suffixList.add("DTO");
        suffixList.add("Entity");
        suffixList.add("Dto");
        return suffixList;
    }


    /**
     * 生成sql执行语句 并执行
     *
     * @param doList
     * @param doClass
     * @param <T>
     * @return
     */
    public static <T> void batchGenerateInsertExecute(List<T> doList, Class<T> doClass, Consumer<? super String> action) {
        batchGenerateInsertExecute(doList, doClass, DEFAULT_SIZE, action);
    }

    public static <T> void batchGenerateInsertExecute(List<T> doList, Class<T> doClass, Integer size, Consumer<? super String> action) {
        List<String> insertListSql = batchGenerateInsert(doList, size, doClass);
        for (String s : insertListSql) {
            action.accept(s);
        }
    }

    /**
     * 生成sql执行语句
     *
     * @param doList
     * @param doClass
     * @param <T>
     * @return
     */
    public static <T> List<String> batchGenerateInsert(List<T> doList, Class<T> doClass) {
        return batchGenerateInsert(doList, DEFAULT_SIZE, doClass);
    }

    /**
     * 生成sql执行语句
     *
     * @param doList  插入的列表
     * @param size    每一次插入的大小
     * @param doClass do类型
     * @param <T>     类型
     * @return
     */
    public static <T> List<String> batchGenerateInsert(List<T> doList, Integer size, Class<T> doClass) {
        // 计算数据库名称
        String dataBaseName = judgeDataBaseName(doClass);
        Field[] declaredFields = doClass.getDeclaredFields();
        // 数据库字段
        List<Field> databaseFiled = new ArrayList<>();
        for (Field declaredField : declaredFields) {
            if (!judgeIgnoreField(declaredField)) {
                declaredField.setAccessible(true);
                // 不为忽略字段
                databaseFiled.add(declaredField);
            }
        }
        String dataBaseFieldJoining = databaseFiled.stream().map(e -> humpToLine(e.getName())).collect(Collectors.joining(","));
        // 生成的数据库字段
        List<String> generateSqlList = new ArrayList<>();
        List<Object> parameterList = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < doList.size(); i++) {
            T t = doList.get(i);
            if (i % size == 0) {
                // 初始化语句
                sb.setLength(0);
                sb.append("INSERT INTO ").append(dataBaseName).append('(').append(dataBaseFieldJoining).append(')')
                        .append("VALUES");
            }
            // 添加一行插入记录
            addToLast(sb, parameterList, t, databaseFiled);
            sb.append(',');
            if ((i % size) == (size - 1)) {
                String format = valuesFormat(sb, parameterList);
                generateSqlList.add(format);
                parameterList.clear();
            }
        }
        if (!parameterList.isEmpty()) {
            String format = valuesFormat(sb, parameterList);
            generateSqlList.add(format);
            parameterList.clear();
        }

        return generateSqlList;
    }

    private static String valuesFormat(StringBuffer sb, List<Object> parameterList) {
        Object[] parameters = new String[parameterList.size()];
        for (int j = 0; j < parameterList.size(); j++) {
            parameters[j] = parameterFormat(parameterList.get(j));
        }
        String s = sb.substring(0, sb.length() - 1);
        return String.format(s, parameters);
    }

    private static String parameterFormat(Object object) {
        if (object == null) {
            return "null";
        }
        if (object instanceof String) {
            return "'" + object + "'";
        }
        if (object instanceof Boolean || object instanceof Integer || object instanceof Long) {
            return object.toString();
        }
        if (object instanceof LocalDate) {
            return "'" + formatLocalDate((LocalDate) object) + "'";
        }
        if (object instanceof Date) {
            return "'" + getDateTimeStringByDate((Date) object) + "'";
        }
        return "'" + object.toString() + "'";
    }

    /**
     * 添加一行插入数据
     *
     * @param sb
     * @param parameter
     */
    private static <T> void addToLast(StringBuffer sb, List<Object> parameter, T t, List<Field> databaseFiled) {
        sb.append('(');
        for (int i = 0; i < databaseFiled.size(); i++) {
            sb.append("%s,");
            try {
                Object o = databaseFiled.get(i).get(t);
                parameter.add(o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sb.delete(sb.length() - 1, sb.length());
        sb.append(')');
    }


    /**
     * 判断数据库名称
     *
     * @return
     */
    private static String judgeDataBaseName(Class doClass) {
        String name = doClass.getName();
        String className = name.substring(name.lastIndexOf(".") + 1);
        className = lowerFirstCase(className);
        // 首字母小写
        // 截取最后两个字符
        String substring = className.substring(className.length() - 2);
        if (getSuffixList().contains(substring)) {
            className = className.substring(0, className.length() - substring.length());
        }
        // 驼峰转下划线
        return humpToLine(className);
    }


    /**
     * 判断是否为忽略字段
     *
     * @param field 字段
     * @return
     */
    public static Boolean judgeIgnoreField(Field field) {
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.getClass().getName().equals(TableField.class.getName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 驼峰转下划线
     *
     * @param str
     * @return
     */
    public static String humpToLine(String str) {
        return str.replaceAll("[A-Z]", "_$0").toLowerCase();
    }

    /**
     * 首字母小写
     *
     * @param str
     * @return
     */
    public static String lowerFirstCase(String str) {
        char[] chars = str.toCharArray();
        //首字母小写方法，大写会变成小写，如果小写首字母会消失
        chars[0] += 32;
        return String.valueOf(chars);
    }

    public static String formatLocalDate(LocalDate localDate) {
        if (localDate == null) {
            return "";
        }
        //3.定义格式
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //4.把 2019-01-01  转成  2019/01/01
        return localDate.format(dtf);
    }

    private static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 获得传入时间的年月日，时分秒字符串
     *
     * @param date 时间
     * @return yyyy-MM-dd HH:mm:ss
     * @author :lh
     */
    public static String getDateTimeStringByDate(Date date) {
        if (date == null) {
            return "";
        }
        return DateTimeFormatter.ofPattern(DATE_TIME).format(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
    }
}


