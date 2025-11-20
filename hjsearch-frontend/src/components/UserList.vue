<script setup lang="ts">
import gege from "../assets/gege.jpg";
import { withDefaults, defineProps } from "vue";

interface Props {
  userList: any[];
}

const props = withDefaults(defineProps<Props>(), {
  userList: () => [],
});
</script>

<template>
  <a-list item-layout="horizontal" :data-source="props.userList">
    <template #renderItem="{ item }">
      <a-card hoverable style="width: 240px">
        <template #cover>
          <img :src="gege" class="gege" />
        </template>
        <a-card-meta :title="item.userName">
          <template #description>{{ item.userProfile }}</template>
        </a-card-meta>
      </a-card>
    </template>
  </a-list>
</template>

<style scoped>
.gege {
  width: 50px;
}
</style>
