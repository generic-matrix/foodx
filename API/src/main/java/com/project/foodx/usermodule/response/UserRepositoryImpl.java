package com.project.foodx.usermodule.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.foodx.auth.AuthService;
import com.project.foodx.usermodule.entity.*;
import com.project.foodx.usermodule.service.Util;
import net.minidev.json.JSONObject;
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
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

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
    private Jedis jedis;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public UserRepositoryImpl(@Value("${elastic.host}") String host, @Value("${elastic.port}") Integer port, @Value("${elastic.scheme}") String scheme, @Value("${elastic.userindex}") String UserIndex,@Value("${elastic.foodindex}") String FoodIndex,@Value("${redis.host}") String redis_host,@Value("${redis.port}") Integer redis_port) {
        this.host = host;
        this.port = port;
        this.scheme = scheme;
        this.UserIndex = UserIndex;
        this.FoodIndex = FoodIndex;
        this.client=new RestHighLevelClient(RestClient.builder(new HttpHost(host,port,scheme)));
        this.jedis= new Jedis(redis_host,redis_port);
    }

    public  String GetMd5(String input)
    {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String GetIdByEmail(String email){
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(UserIndex);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.boolQuery().must(termQuery("email.keyword", email)));
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

    @Override
    public Boolean AddUserToElasticSearch(User user) {
        try {
            IndexRequest indexRequest = new IndexRequest(this.UserIndex, "type", user.getId())
                    .source(jsonBuilder()
                            .startObject()
                            .field("Userid", user.getId())
                            .field("email", user.getEmail())
                            .field("password", GetMd5(user.getPassword()))
                            .field("name", user.getName())
                            .endObject());
            UpdateRequest updateRequest = new UpdateRequest(this.UserIndex, "type",user.getId())
                    .doc(jsonBuilder()
                            .startObject()
                            .field("Userid", user.getId())
                            .field("email", user.getEmail())
                            .field("password", GetMd5(user.getPassword()))
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
        searchSourceBuilder.query(QueryBuilders.boolQuery().must(termQuery("email.keyword", user.getEmail())).must(termQuery("password.keyword", GetMd5(user.getPassword()))));
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

    //More optimzation is needed when size of the data which is returned from the Redis
    @Override
    public List<AutoComplete> SearchFoodNameElasticSearchForAutoComplete(String searchTerm) {
        List<AutoComplete> autocomplete = new ArrayList<>();
        try {
            if(searchTerm=="&&&&"){
                return autocomplete;
            }
            byte[] prefixByte = ("[" + searchTerm).getBytes();
            byte[] prefixByteExtended = Arrays.copyOf(prefixByte, prefixByte.length + 1);
            prefixByteExtended[prefixByte.length] = (byte) 0xFF;
            Set<String> autofill = jedis.zrangeByLex("autofill", "[" + searchTerm, new String(prefixByteExtended));
            int counter = 0;
            for (String result : autofill) {
                if (counter > 5) {
                    break;
                }
                String id = result.split("&&&&")[1];
                String title = result.split("&&&&")[0];
                AutoComplete autoObj = new AutoComplete(id, title);
                autocomplete.add(autoObj);
                counter++;
            }
        }catch (Exception ex){
            ex.printStackTrace();
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

    @Override
    public JSONObject GetUserDataByAlexaToken(String token) {
        try {
            URL url = new URL("https://api.amazon.com/user/profile");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("GET");
            if(conn.getResponseCode()!=200){
                JSONObject obj = new JSONObject();
                obj.put("error","Invalid Alexa token");
                obj.put("data",null);
                return obj;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String output;

            StringBuffer response = new StringBuffer();
            while ((output = in.readLine()) != null) {
                response.append(output);
            }
            in.close();
            AlexaUser alexaUser=objectMapper.convertValue(response.toString(),AlexaUser.class);
            if(alexaUser.getError()!=null){
                JSONObject obj = new JSONObject();
                obj.put("error",alexaUser.getError());
                obj.put("data",null);
                return obj;
            }else{
                //Search the user by email and get the id
                String id=GetIdByEmail(alexaUser.getEmail());
                if(id==null){
                    JSONObject obj = new JSONObject();
                    obj.put("error","User does not exists");
                    obj.put("data",null);
                    return obj;
                }else{
                    //from the id generate the token and add in the alexauser
                    User userDto = new User();
                    userDto.setId(id);
                    userDto.setEmail(alexaUser.getEmail());
                    AuthService auth=new AuthService();
                    alexaUser.setToken(auth.generateJWTToken(userDto));
                    JSONObject obj = new JSONObject();
                    obj.put("error",null);
                    JSONObject obj2 = new JSONObject();
                    obj2.put("name",alexaUser.getName());
                    obj2.put("token",alexaUser.getToken());
                    obj.put("data",obj2);
                    return obj;
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
            JSONObject obj = new JSONObject();
            obj.put("error","Internal Error");
            obj.put("data",null);
            return obj;
        }
    }
}
