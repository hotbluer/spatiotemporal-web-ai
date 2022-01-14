package com.inzyme.spatiotemporal.web.ai.util;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * <p>
 *
 * @author fyl
 * @since 2021-12-27 16:33
 */

@Slf4j
public class EsUtil {

    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public class User{

       private String id;
       private String name;
       private Integer age;

    }

    public void eventsSaveOrUpdateDocs(RestHighLevelClient client) {
        ArrayList<User> userList = new ArrayList<>();
        userList.add (new User ("1","耀",21));
        userList.add (new User ("2","关羽",22));
        userList.add (new User ("3","张飞",20));
        userList.add (new User ("4","刘备",23));

        BulkRequest request = new BulkRequest();
        userList.forEach(item -> request.add(new IndexRequest("onplay","kingWz")
                .id(item.getId()).source(JSON.toJSONString(item), XContentType.JSON)));
        try {
            BulkResponse bulk = client.bulk(request, RequestOptions.DEFAULT);
            int status = bulk.status().getStatus();
            System.out.println(status);
            log.info("索引:{},批量插入{}条数据成功!", "onPlay", userList.size());
        } catch (IOException e) {
            log.error(e.getMessage());
            log.error("索引:{},批量插入数据失败", "onPlay");

        }
    }

    public void topicsSearch(String keyWord,RestHighLevelClient client) {
        BoolQueryBuilder topicBoolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.wildcardQuery("name", "*" + keyWord + "*"));
//                .must(QueryBuilders.termQuery("status", 1))
//                .must(QueryBuilders.termQuery("deleted", 0));
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(topicBoolQueryBuilder)
                .size(4);
        SearchRequest searchRequest = new SearchRequest()
                .indices("onplay")
                .types("kingWz")
                .source(sourceBuilder);
        try {
            SearchResponse topicResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            List<User> topics = new ArrayList<>();
            if (topicResponse.getHits().getTotalHits().value> 0) {
                for (SearchHit hit : topicResponse.getHits().getHits()) {
                    Map<String, Object> map;
                    map = hit.getSourceAsMap();
                    topics.add(new User().setId(map.get("id").toString()).setName(map.get("name").toString()).setAge((Integer)map.get("age")));
                }
                topics.forEach(item-> System.out.println("es数据\r\n id:"+item.getId()+" name:"+item.getName()+" age:"+item.getAge()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
