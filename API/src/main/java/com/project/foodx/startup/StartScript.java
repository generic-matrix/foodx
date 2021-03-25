package com.project.foodx.startup;


import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
@Order(1)
class StartScript implements ApplicationRunner {

    private String host;
    private Integer port;
    private String scheme;
    private String userindex;
    private String redis_raw;
    private String redis_host;
    private Integer redis_port;
    private  RestHighLevelClient client;

    @Autowired
    public StartScript(@Value("${elastic.host}") String host, @Value("${elastic.port}") Integer port, @Value("${elastic.scheme}") String scheme, @Value("${elastic.userindex}") String userindex,@Value("${redis.host}") String redis_host,@Value("${redis.port}") Integer redis_port) {
        this.host = host;
        this.port = port;
        this.scheme = scheme;
        this.userindex = userindex;
        this.client=new RestHighLevelClient(RestClient.builder(new HttpHost(host,port,scheme)));
        this.redis_host=redis_host;
        this.redis_port=redis_port;
    }


    private Boolean IsIndexExists(String index){
        GetIndexRequest request = new GetIndexRequest(userindex);
        boolean exists = false;
        try {
            exists = client.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exists;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(IsIndexExists(userindex)==Boolean.FALSE){
            CreateIndexRequest request =new CreateIndexRequest(userindex);
            try {
                client.indices().create(request, RequestOptions.DEFAULT);
            }catch (Exception ex){
                throw new Exception(ex);
            }
        }
        client.close();
        System.out.println("Indices intialization is successful");
        //Populate the redis instance with food title TTL : unlimited
        Jedis jedis = new Jedis(redis_host,redis_port);
        Path path=Paths.get(new File(".").getAbsolutePath().replace(".","src/main/java/com/project/foodx/startup/")+"redis_food_title.txt");
        List<String> names = Files.readAllLines(path);
        for (String name : names)
        {
            jedis.zadd("autofill",0,name.toLowerCase());
        }

        //jedis.close();
        System.out.println("Redis init is successful");
    }
}