package com.xin.web.utils.jsoup;

import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Map;

/**
 * html文档解析抽象类
 *
 * @author creator mafh 2019/12/2 13:49
 * @author updater
 * @version 1.0.0
 */
public abstract class BaseDocumentAnalyzer {

    /**
     * 根据html文档对象获取List<Map>
     *
     * @param document 文档
     * @return 列表
     */
    public abstract List<Map<String, Object>> getMapList(Document document);

    /**
     * 根据html文档对象获取Map
     *
     * @param document 文档
     * @return 数据
     */
    public abstract Map<String, Object> getMap(Document document);
}
