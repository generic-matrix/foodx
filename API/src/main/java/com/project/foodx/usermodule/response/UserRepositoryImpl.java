package com.project.foodx.usermodule.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.foodx.usermodule.entity.*;
import com.project.foodx.usermodule.service.Util;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Component
public class UserRepositoryImpl implements UserRepository{

    private String host;
    private Integer port;
    private String scheme;
    private String UserIndex;
    private String FoodIndex;
    private RestHighLevelClient client;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public UserRepositoryImpl(@Value("${elastic.host}") String host, @Value("${elastic.port}") Integer port, @Value("${elastic.scheme}") String scheme, @Value("${elastic.userindex}") String UserIndex,@Value("${elastic.foodindex}") String FoodIndex) {
        this.host = host;
        this.port = port;
        this.scheme = scheme;
        this.UserIndex = UserIndex;
        this.FoodIndex = FoodIndex;
        this.client=new RestHighLevelClient(RestClient.builder(new HttpHost(host,port,scheme)));
    }

    @Override
    public Boolean AddUserToElasticSearch(User user) {
        try {
            IndexRequest indexRequest = new IndexRequest(this.UserIndex, "type", user.getId())
                    .source(jsonBuilder()
                            .startObject()
                            .field("Userid", user.getId())
                            .field("email", user.getEmail())
                            .field("password", user.getPassword())
                            .field("name", user.getName())
                            .endObject());
            UpdateRequest updateRequest = new UpdateRequest(this.UserIndex, "type",user.getId())
                    .doc(jsonBuilder()
                            .startObject()
                            .field("Userid", user.getId())
                            .field("email", user.getEmail())
                            .field("password", user.getPassword())
                            .field("name", user.getName())
                            .endObject())
                    .upsert(indexRequest);
            client.update(updateRequest,RequestOptions.DEFAULT).getGetResult();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public String CheckIfUserExistsInElasticSearch(User user) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(UserIndex);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.boolQuery().must(termQuery("email.keyword", user.getEmail())).must(termQuery("password.keyword", user.getPassword())));
        searchRequest.source(searchSourceBuilder);
        String return_id=null;

        try {
            SearchResponse searchResponse = null;
            searchResponse =client.search(searchRequest, RequestOptions.DEFAULT);
            if (searchResponse.getHits().getTotalHits().value > 0) {
                SearchHit[] searchHit = searchResponse.getHits().getHits();
                for (SearchHit hit : searchHit) {
                    return_id=hit.getId();
                    break;
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if(return_id==null){
            return null;
        }else {
            return return_id;
        }
    }

    //More optimzation is needed when size of the data which is returned from the elastic search
    @Override
    public List<AutoComplete> SearchFoodNameElasticSearchForAutoComplete(String food_name) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(FoodIndex);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        food_name=food_name.toLowerCase();
        searchSourceBuilder.query(QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("title",food_name)));
        searchSourceBuilder.size(5);
        searchRequest.source(searchSourceBuilder);
        List<AutoComplete> autocomplete=new ArrayList<>();
        try {
            SearchResponse searchResponse = null;
            searchResponse =client.search(searchRequest, RequestOptions.DEFAULT);
            if (searchResponse.getHits().getTotalHits().value > 0) {
                SearchHit[] searchHit = searchResponse.getHits().getHits();
                for (SearchHit hit : searchHit) {
                    Map<String, Object> map = hit.getSourceAsMap();
                    String title=map.get("title").toString();
                    String id=hit.getId();
                    AutoComplete obj=new AutoComplete(id,title);
                    autocomplete.add(obj);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return autocomplete;
    }

    public Food SearchFoodByIdInElasticSearch(String food_id) {

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(FoodIndex);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.boolQuery().filter(QueryBuilders.termsQuery("_id",food_id)));
        searchRequest.source(searchSourceBuilder);
        Food result=null;
        try {
            SearchResponse searchResponse = null;
            searchResponse =client.search(searchRequest, RequestOptions.DEFAULT);
            if (searchResponse.getHits().getTotalHits().value > 0) {
                SearchHit[] searchHit = searchResponse.getHits().getHits();
                for (SearchHit hit : searchHit) {
                    result=objectMapper.convertValue(hit.toString(),Food.class);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    //by default we took 100 miles radius and max results will be 20
    public List<SnapShot>  SearchFoodByByGeo(float lat,float lng) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(FoodIndex);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.boolQuery().filter(QueryBuilders.geoDistanceQuery("location").point(lat, lng).distance(100,DistanceUnit.MILES)));
        searchRequest.source(searchSourceBuilder);
        searchSourceBuilder.size(20);
        List<SnapShot> autocomplete=new ArrayList<>();
        try {
            SearchResponse searchResponse = null;
            searchResponse =client.search(searchRequest, RequestOptions.DEFAULT);
            if (searchResponse.getHits().getTotalHits().value > 0) {
                SearchHit[] searchHit = searchResponse.getHits().getHits();
                Util util =new Util();
                for (SearchHit hit : searchHit) {
                    Map<String, Object> map = hit.getSourceAsMap();
                    String title=map.get("title").toString();
                    String id=hit.getId();
                    util.GetAvgRating(map.get("rating"));
                    SnapShot obj=new SnapShot(id,title,util.getAvgRating(),util.getRatingCount());
                    autocomplete.add(obj);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return autocomplete;
    }

    @Override
    public Boolean AddRating(String foodId, Rating rating) {
        //Append to document by food_id
        try {

            Map<String,String> obj = new HashMap<>();
            obj.put("user_id",rating.getUser_id());
            obj.put("user_name",rating.getUser_name());
            obj.put("rating_id",rating.getRating_id());
            obj.put("message",rating.getMessage());
            obj.put("star",String.valueOf(rating.getStar()));

            Map<String,Object> parameters = singletonMap("data",obj);
            UpdateRequest request = new UpdateRequest(this.FoodIndex, "_doc", foodId);
            Script script = new Script(ScriptType.INLINE, "painless",
                    "if(ctx._source.rating==null){ctx._source.rating=[]} ctx._source.rating.add(params.data)", parameters);
            request.script(script);
            client.update(request,RequestOptions.DEFAULT);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
