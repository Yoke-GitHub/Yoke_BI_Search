<template>
  <div class="index-page">
    <a-input-search
        v-model:value="searchText"
        placeholder="input search text"
        enter-button="Search"
        size="large"
        @search="onSearch"
    />
    <my-divider />
    <a-tabs v-model:activekey="activeKey" @change="onTabChange">
      <a-tab-pane key="post" tab="文章">
        <PostList :post-list="postList" />
      </a-tab-pane>
      <a-tab-pane key="user" tab="用户">
        <UserList :user-list="userList" />
      </a-tab-pane>
      <a-tab-pane key="picture" tab="图片">
        <PictureList :picture-list="pictureList" />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, watchEffect } from "vue";
import PostList from "@/components/PostList.vue";
import UserList from "@/components/UserList.vue";
import PictureList from "@/components/PictureList.vue";
import MyDivider from "@/components/MyDivider.vue";
import { useRoute, useRouter } from "vue-router";
import MyAxios from "@/plugins/MyAxios";
import {message} from "ant-design-vue";

const postList = ref([]);
const userList = ref([]);
const pictureList = ref([]);

const route = useRoute();
const router = useRouter();
const activeKey = route.params.category;

const initSearchParams = {
  type:"",
  text: "",
  pageSize: 10,
  pageNum: 1,
};

const searchText = ref(route.query.text || "");

/**
 * 加载数据
 * @param Params
 * */
const loadDataOld = (Params: any) => {
  const postQuery = {
    ...Params,
    searchText: Params.text,
  };
  MyAxios.post("post/list/page/vo", postQuery).then((res: any) => {
    postList.value = res.records;
  });
  const userQuery = {
    ...Params,
    userName: Params.text,
  };
  MyAxios.post("user/list/page/vo", userQuery).then((res: any) => {
    userList.value = res.records;
  });
  const pictureQuery = {
    ...Params,
    searchText: Params.text,
  };
  MyAxios.post("picture/list/page/vo", pictureQuery).then((res: any) => {
    pictureList.value = res.records;
  });
};

/**
 * 加载聚合搜索数据
 * @param Params
 * */
const loadAllData = (params: any) => {
  const allQuery = {
    ...params,
    searchText: params.text,

  };
  MyAxios.post("search/all",allQuery).then((res: any)=> {
    userList.value = res.userList;
    postList.value = res.postList;
    pictureList.value = res.pictureList;
  })
};

/**
 * 分类加载聚合搜索数据
 * @param Params
 * */
const loadData = (params: any) => {
  const { type = "post" } = params;
  if(!type){
    message.error("类型为空")
    return;
  }
  const Query = {
    ...params,
    searchText: params.text,

  };
  MyAxios.post("search/all",Query).then((res: any) => {
    if(type === "user"){
      userList.value = res.dataList;
    }else if(type === "post"){
      postList.value = res.dataList;
    }else if(type === "picture"){
      pictureList.value = res.dataList;
    }
  })
};
const searchParams = ref(initSearchParams);

watchEffect(() => {
  searchParams.value = {
    ...initSearchParams,
    text: route.query.text,
    type: route.params.category,
  } as any;
  loadData(searchParams.value);
});

const onSearch = (value: string) => {
  router.push({
    query: {
      ...searchParams.value,
      text: value,
    },
  });
};

const onTabChange = (key: string) => {
  router.push({
    path: `/${key}`,
    query: searchParams.value,
  });
};
</script>
