package com.xin.web.utils.jsoup;

import com.xin.web.utils.convert.ConvertUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 网页抓取工具
 *
 * @author creator mafh 2019/12/2 13:45
 * @author updater
 * @version 1.0.0
 */
public class JsoupUtils {

    private static Logger logger = LoggerFactory.getLogger(JsoupUtils.class);

    /**
     * 获取文档列表
     *
     * @param url              解析地址
     * @param documentAnalyzer 文档解析器
     * @param clazz            返回对象类
     * @param <T>              泛型
     * @return 列表
     */
    public static <T> List<T> getDocumentList(String url, BaseDocumentAnalyzer documentAnalyzer, Class<T> clazz) throws Exception {

        /*----------------------------------------- 日志记录 -----------------------------------------*/
        logger.debug("获取文档列表，解析地址：{}", url);

        /*----------------------------------------- 参数声明 -----------------------------------------*/
        List<T> results = new ArrayList<>();

        /*----------------------------------------- 业务处理 -----------------------------------------*/
        documentAnalyzer.getMapList(Jsoup.connect(url).timeout(5000).get()).forEach(map -> {
            try {
                T t = ConvertUtils.convertMapToBean(map, clazz);
                results.add(t);
            } catch (Exception e) {
                logger.error("对象转换异常，错误：{}", e.getMessage());
            }
        });

        /*----------------------------------------- 日志记录 -----------------------------------------*/
        logger.debug("获取文档列表，结果数：{}", results.size());

        /*----------------------------------------- 方法返回 -----------------------------------------*/
        return results;
    }

    /**
     * 获取文档列表
     *
     * @param url              解析地址
     * @param documentAnalyzer 文档解析器
     * @return 列表
     */
    public static List<Map<String, Object>> getDocumentList(String url, BaseDocumentAnalyzer documentAnalyzer) throws Exception {

        /*----------------------------------------- 日志记录 -----------------------------------------*/
        logger.debug("获取文档列表，解析地址：{}", url);

        /*----------------------------------------- 方法返回 -----------------------------------------*/
        return documentAnalyzer.getMapList(Jsoup.connect(url).timeout(5000).get());
    }

    /**
     * 获取文档数据
     *
     * @param url              解析地址
     * @param documentAnalyzer 文档解析器
     * @return 数据
     */
    public static Map<String, Object> getDocumentMap(String url, BaseDocumentAnalyzer documentAnalyzer) throws Exception {

        /*----------------------------------------- 日志记录 -----------------------------------------*/
        logger.debug("获取文档数据，解析地址：{}", url);

        /*----------------------------------------- 方法返回 -----------------------------------------*/
        return documentAnalyzer.getMap(Jsoup.connect(url).timeout(5000).get());
    }
}
