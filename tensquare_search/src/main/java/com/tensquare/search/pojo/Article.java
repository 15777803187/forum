package com.tensquare.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

@Document(indexName = "tensquare", type = "article")//索引名和类型名，索引必须先存在，而类型没有会自动创建
public class Article implements Serializable {
    /**
     * 该pojo的属性是参考数据库中，需要在搜索时使用，都是可存储的，表示需要在搜索页面显示
     */
    /**
     * 对搜索关键字有三个关键
     * 是否索引：是否能够通过关键字搜索到
     * 是否分词：在能够索引的前提下选择分词匹配还是整体匹配
     * 是否存储：是否需要在搜索页面显示
     */
    @Id
    private String id;
    /**
     * 表示该域是可索引的，采用分词器的细分词 分词匹配(对于有关键字整体匹配)
     */
    @Field(index = true, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String title;

    @Field(index = true, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String content;//文章正文

    private String state;//审核状态

    public Article() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
