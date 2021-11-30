package com.dabaicai.framework.common.beans;



import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author zhangyanbing
 * @Description 类扫描
 * @date 2020/7/11 23:26
 */
public abstract class PackageScanner {

    /**
     * 扫描的包路径
     */
    private String basePackage;

    /**
     * 那些注解需要扫描的
     */
    private List<Class<? extends Annotation>> scanList;

    public PackageScanner(String basePackage, List<Class<? extends Annotation>> scanList) {
        this.basePackage = basePackage;
        this.scanList = scanList;
    }

    public abstract void dealClass(Class<?> klass);

    /**
     * 扫描一般的包
     * @param packageName
     * @param currentfile
     */
    private void scanPackage(String packageName, File currentfile) {
        // FileFilter是文件过滤器,源代码只写了一个accapt的抽象方法。
        File[] fileList = currentfile.listFiles(pathName -> {
            //判断是否是目录
            if (pathName.isDirectory()) {
                return true;
            }
            return pathName.getName().endsWith(".class");
        });
        if (fileList == null) return;
        for (File file : fileList) {
            scanFile(file, packageName);
        }
    }

    /**
     * 扫描文件
     * @param file
     * @param packageName
     */
    private void scanFile(File file, String packageName) {
        if (file.isDirectory()) {
            scanPackage(packageName + "." + file.getName(), file);
        } else {
            String fileName = file.getName().replace(".class", "");
            String className = packageName + "." + fileName;
            try {
                // 加载类
                Class<?> klass = Class.forName(className);
                if (containAnnotation(klass)) {
                    dealClass(klass);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断当前的这个类是否为要扫描的类
     * @param klass
     * @return
     */
    private boolean containAnnotation(Class<?> klass) {
        for (Class<? extends Annotation> annotation : scanList) {
            if (klass.isAnnotationPresent(annotation)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 扫描jar包方法
     * @param url
     * @throws IOException
     */
    private void scanPackage(URL url) throws IOException {
        JarURLConnection urlConnection = (JarURLConnection) url.openConnection();
        JarFile jarfile = urlConnection.getJarFile();
        Enumeration<JarEntry> jarEntries = jarfile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String jarName = jarEntry.getName();
            if (!jarName.endsWith(".class")) {
                continue;
            }
            String className = jarName.replace(".class", "").replaceAll("/", ".");

            try {
                Class<?> klass = Class.forName(className);
                if (containAnnotation(klass)) {
                    dealClass(klass);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 包扫描入口
     */
    public void packageScan() {
        packageScan(basePackage);
    }

    /**
     * 用类名扫描
     * @param klass
     */
    public void packageScan(Class<?> klass) {
        packageScan(klass.getPackage().getName());
    }

    /**
     * 用包名进行扫描
     * @param packageName
     */
    public void packageScan(String packageName) {
        String packPath = packageName.replace(".", "/");

        //线程上下文类加载器得到当前的classpath的绝对路径.（动态加载资源）
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try {
            //(来得到当前的classpath的绝对路径的URI表示法。)
            Enumeration<URL> resources = classloader.getResources(packPath);
            while (resources.hasMoreElements()) {
                //先获得本类的所在位置
                URL url = resources.nextElement();
                //url.getProtocol()是获取URL的HTTP协议。
                if (url.getProtocol().equals("jar")) {
                    //判断是不是jar包
                    scanPackage(url);
                } else {
                    //此方法不会自动将链接中的非法字符转义。
                    //而在File转化成URI的时候，会将链接中的特殊字符如#或!等字符进行编码。
                    File file = new File(url.toURI());
                    if (!file.exists()) {
                        continue;
                    }
                    scanPackage(packageName, file);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
