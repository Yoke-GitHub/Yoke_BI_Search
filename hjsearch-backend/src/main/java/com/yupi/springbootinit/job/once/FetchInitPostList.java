package com.yupi.springbootinit.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取帖子初始化数据
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
// Component 取消注释后，每次启动springboot 项目都会执行一次run方法
//@Component
@Slf4j
public class FetchInitPostList implements CommandLineRunner {

    @Resource
    private PostService postService;

    @Override
    public void run(String... args) {
//        1、获取数据
        String json = "{\"pageSize\":12,\"sortOrder\":\"descend\",\"sortField\":\"createTime\",\"tags\":[],\"current\":1,\"reviewStatus\":1,\"category\":\"文章\",\"hiddenContent\":true}";
        String url = "https://api.code-nav.cn/api/post/list/page/vo";
        String result = HttpRequest
                .post(url)
                .body(json)
                .execute()
                .body();
        System.out.println(result);
//        2、json 转 对象
        Map<String, Object> map = JSONUtil.toBean(result, Map.class);
        JSONObject data = (JSONObject) map.get("data");
        JSONArray records = (JSONArray) data.get("records");
        ArrayList<Post> postList = new ArrayList<>();
        for (Object record : records) {
            JSONObject tempRecord = (JSONObject) record;
            Post post = new Post();
            post.setTitle(tempRecord.getStr("title"));
            post.setContent(tempRecord.getStr("content"));
            JSONArray tags = (JSONArray) tempRecord.get("tags");
            List<String> tagsList = tags.toList(String.class);
            post.setTags(JSONUtil.toJsonStr(tagsList));
            post.setUserId(1L);
            postList.add(post);
        }
//        3、数据入库
        boolean b = postService.saveBatch(postList);
        if (b) {
            log.info("添加成功文章, 条数 = {}", postList.size());
        } else {
            log.error("添加文章失败！");
        }
    }
}
