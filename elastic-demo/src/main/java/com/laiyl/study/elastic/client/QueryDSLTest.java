package com.laiyl.study.elastic.client;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.*;
import org.junit.Test;

import static org.elasticsearch.index.query.QueryBuilders.*;

public class QueryDSLTest {

    @Test
    public void test() {

        MatchQueryBuilder matchQueryBuilder = matchQuery("gender", "male");

        MultiMatchQueryBuilder multiMatchQueryBuilder = multiMatchQuery("kimchy", "user", "message");

        String s = multiMatchQueryBuilder.toString();
        System.out.println(matchQueryBuilder.toString());

        ExistsQueryBuilder name = existsQuery("name");

        TypeQueryBuilder my_type = typeQuery("my_type");
        System.out.println(my_type.toString());

        ConstantScoreQueryBuilder boost = constantScoreQuery(
                termQuery("name", "kimchy"))
                .boost(2.0f);
        System.out.println(boost);

        TermQueryBuilder termQueryBuilder = termQuery("content", "test1");
        System.out.println(termQueryBuilder.toString());
        BoolQueryBuilder must = boolQuery().must(termQueryBuilder)
                .must(name);
        System.out.println(must);

        DisMaxQueryBuilder disMaxQueryBuilder = disMaxQuery()
                .add(termQuery("name", "kimchy"))
                .add(termQuery("name", "elasticsearch"))
                .boost(1.2f)
                .tieBreaker(0.7f);

        System.out.println(disMaxQueryBuilder);
    }

    @Test
    public void test2() {
        NestedQueryBuilder nestedQueryBuilder = nestedQuery(
                "obj1",
                boolQuery()
                        .must(matchQuery("obj1.name", "blue"))
                        .must(rangeQuery("obj1.count").gt(5)),
                ScoreMode.Avg);

        System.out.println(nestedQueryBuilder);
    }
}
