package com.laiyl.study.elastic.client;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.mapper.ObjectMapper;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class ClientTest {

    public static TransportClient client;

    static {
        Settings settings = Settings.builder()
                .put("cluster.name","elasticsearch")
                .put("client.transport.sniff", true) //开启嗅探，当有新节点加入集群时可以探测到
                .build();
        try {
             client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("host1"), 9300))
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("host2"),9300));

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void testIndex() throws IOException {
        //构建json数据方式一
        String json = "{\n" +
                "  \"user\" : \"kimchy\",\n" +
                "  \"postDate\" : \"2013-01-30\",\n" +
                "  \"message\" : \"trying out Elasticsearch\"\n" +
                "}";

        //构建json数据方式二
        Map<String, Object> json2 = new HashMap<>();
        json2.put("user", "kimchy");
        json2.put("postDate", "2013-01-30");
        json2.put("message", "trying out Elasticsearch");

        //构建json数据方式三
        XContentBuilder builder = jsonBuilder()
                .startObject()
                .field("user", "kimchy")
                .field("postDate", "2013-01-30")
                .field("message", "trying out Elasticsearch")
                .endObject();

        IndexResponse response = client.prepareIndex("twitter", "tweet", "1")
                .setSource(builder)
                .get();

        IndexResponse response1 = client.prepareIndex("twitter", "tweet", "1")
                .setSource(json, XContentType.JSON)
                .get();

        String index = response.getIndex();
        String type = response.getType();
        String id = response.getId();

    }

    public void getTest() {
        GetResponse response = client.prepareGet("twitter", "tweet", "1").get();
    }

    public void deleteTest() {
        //同步删除
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                .filter(QueryBuilders.matchQuery("gender", "male"))
                .source("persons")
                .get();

        long deleted = response.getDeleted();

        //异步删除
        DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                .filter(QueryBuilders.matchQuery("gender", "male"))
                .source("persons")
                .execute(new ActionListener<BulkByScrollResponse>() {
                    @Override
                    public void onResponse(BulkByScrollResponse bulkByScrollResponse) {
                        long deleted1 = bulkByScrollResponse.getDeleted();
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
    }

    public void updateTest() throws IOException, ExecutionException, InterruptedException {
        UpdateRequest updateRequest = new UpdateRequest("index", "type", "1")
                .doc(jsonBuilder()
                        .startObject()
                        .field("gender", "male")
                        .endObject());
        UpdateResponse updateResponse = client.update(updateRequest).get();

    }

    public void upsetTest() throws IOException, ExecutionException, InterruptedException {
        IndexRequest indexRequest = new IndexRequest("index","type","1")
                .source(jsonBuilder()
                .startObject()
                .field("name","Joe Smith")
                .field("gender","male")
                .endObject());

        UpdateRequest updateRequest = new UpdateRequest("index","type","1")
                .doc(
                        jsonBuilder()
                                .startObject()
                                .field("gender", "male")
                                .endObject()
                )
                .upsert(indexRequest);

        UpdateResponse updateResponse = client.update(updateRequest).get();

    }

    public void multiGet() {
        MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
                .add("twitter", "tweet", "1")
                .add("twitter", "tweet", "2", "3", "4")
                .get();

        for (MultiGetItemResponse itemResponses : multiGetItemResponses) {
            GetResponse response = itemResponses.getResponse();
            if (response.isExists()) {
                String json = response.getSourceAsString();
            }
        }
    }
}
