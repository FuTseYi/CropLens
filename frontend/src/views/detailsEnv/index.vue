<template>
  <div class="container">
    <div class="left-panel">
      <el-select v-model="selectedGreenhouse" placeholder="请选择温室" class="greenhouse-select">
        <el-option
          v-for="(greenhouse, index) in greenhouses"
          :key="index"
          :label="greenhouse"
          :value="greenhouse">
        </el-option>
      </el-select>
      <el-row :gutter="20" justify="center">
        <!-- 第一行 -->
        <el-col :span="8" v-for="(item, index) in firstRow" :key="index" class="env-item">
          <i :class="`iconfontjs ${item.icon}`" class="env-icon"></i>
          <div class="env-label">{{ item.label }}</div>
          <div class="env-value">{{ item.value }}</div>
        </el-col>
        <!-- 第二行 -->
        <el-col :span="8" v-for="(item, index) in secondRow" :key="index" class="env-item">
          <i :class="`iconfontjs ${item.icon}`" class="env-icon"></i>
          <div class="env-label">{{ item.label }}</div>
          <div class="env-value">{{ item.value }}</div>
        </el-col>
        <!-- 第三行 -->
        <el-col :span="8" v-for="(item, index) in thirdRow" :key="index" class="env-item">
          <i :class="`iconfontjs ${item.icon}`" :style="{ color: item.status ? '#409EFF' : '#909399' }" class="env-icon"></i>
          <div class="env-label">{{ item.label }}</div>
          <el-switch v-model="item.status" active-color="#409EFF" inactive-color="#909399"></el-switch>
        </el-col>
      </el-row>
    </div>
    <div class="right-panel">
      <div class="suggestion-box">
        <h3>智能建议</h3>
        <el-button type="primary" @click="getSuggestions" :loading="loading">获取建议</el-button>
        <div class="suggestions" v-if="suggestions.length">
          <p v-for="(suggestion, index) in suggestions" :key="index">{{ suggestion }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';

const greenhouses = ref(['1号温室', '2号温室', '3号温室', '4号温室', '5号温室', '6号温室', '7号温室', '8号温室', '9号温室']);
const selectedGreenhouse = ref(greenhouses.value[0]);

const firstRow = ref([
  { icon: 'icon-daqiwendu', label: '室内温度', value: '25.5°C' },
  { icon: 'icon-kongqishidu_kongqishidu', label: '空气湿度', value: '62.3%' },
  { icon: 'icon-turangshidu', label: '土壤湿度', value: '64.7%' },
]);

const secondRow = ref([
  { icon: 'icon-eryanghuatan', label: '二氧化碳', value: '434ppm' },
  { icon: 'icon-turangPH', label: 'PH值', value: '6.5' },
  { icon: 'icon-guangzhaoqiangdu', label: '光照强度', value: '893lux' },
]);

const thirdRow = ref([
  { icon: 'icon-shuibeng', label: '水泵', status: false },
  { icon: 'icon-a-buguangdengzidong', label: '补光灯', status: true },
  { icon: 'icon-fengshan', label: '风扇', status: false },
]);

const suggestions = ref([]);
const loading = ref(false);
const apiKey = ''; // 请替换为您的DeepSeekAPI密钥

async function getSuggestions() {
  loading.value = true;
  try {
    const cropInfo = selectedGreenhouse.value === '1号温室' ? '当前种植作物是玉米。' : '当前种植作物信息未提供。';
    const response = await axios.post('https://api.deepseek.com/v1/chat/completions', {
      model: 'deepseek-chat',
      messages: [
        { role: 'system', content: '根据以下环境数据和种植作物提供当下环境应该调整的建议，如果不需要调整，则给出不需要调整的理由：' },
        { role: 'user', content: cropInfo },
        ...firstRow.value.map(item => ({ role: 'user', content: `${item.label}: ${item.value}` })),
        ...secondRow.value.map(item => ({ role: 'user', content: `${item.label}: ${item.value}` }))
      ],
      stream: false
    }, {
      headers: {
        'Authorization': `Bearer ${apiKey}`,
        'Content-Type': 'application/json'
      }
    });

    suggestions.value = response.data.choices[0].message.content.split('\n');
  } catch (error) {
    console.error('Error:', error);
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
.container {
  display: flex;
  height:calc(100vh - 60px); /* 设置容器高度为视口高度 */
}

.left-panel {
  width: 50%; /* 设置左边面板宽度为50% */
  overflow-y: auto; /* 允许垂直滚动 */
  padding: 20px;
}

.greenhouse-select {
  margin-bottom: 50px;
  width: 20%;
}

.right-panel {
  width: 50%; /* 设置右边面板宽度为50% */
  background-color: #f5f7fa;
  padding: 20px;
  border-radius: 8px;
  /*overflow-y: auto;  允许垂直滚动 */
}
.suggestions {
  text-align: left;
  margin-top: 20px;
  padding: 15px;
  background-color: #f9f9f9;
  border-radius: 8px;
  line-height: 1.6;
  font-size: 14px;
  color: #333;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.suggestions p {
  margin-bottom: 10px;
}

.suggestions p:last-child {
  margin-bottom: 0;
}
.suggestion-box {
  text-align: center;
  padding: 20px;
  /* background-color: #ffffff; */
  border-radius: 12px;
  /* box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); */
}

.suggestion-box h3 {
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

.el-button {
  background: linear-gradient(90deg, #409EFF, #66b1ff);
  border: none;
  color: white;
  font-weight: bold;
  padding: 10px 20px;
  border-radius: 8px;
  transition: background 0.3s, transform 0.3s;
}

.el-button:hover {
  background: linear-gradient(90deg, #66b1ff, #409EFF);
  transform: translateY(-2px);
}

.env-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 30px;
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 8px;
  transition: background-color 0.3s;
}

.env-item:hover {
  background-color: #e0e4e8;
}

.env-icon {
  font-size: 40px;
  color: #409EFF; /* 默认图标颜色为蓝色 */
}

.env-label {
  margin-top: 10px;
  font-size: 16px;
  color: #606266;
}

.env-value {
  margin-top: 5px;
  font-size: 14px;
  color: #909399;
}
</style>