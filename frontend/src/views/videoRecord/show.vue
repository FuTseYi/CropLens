<template>
	<div class="system-predict-container layout-padding">
		<div class="system-predict-padding layout-padding-auto layout-padding-view">
			<div class="header">
				<div class="kind">
					<text>作物种类：</text>
					<el-input v-model="state.form.weight" style="width: 150px" size="large" disabled />
				</div>
				<div class="model">
					<text>使用权重：</text>
					<el-input v-model="state.form.weight" style="width: 180px" size="large" disabled />
				</div>
				<div class="count">
					<text>最小阈值：</text>
					<el-input v-model="state.form.conf" style="width: 100px" size="large" disabled />
				</div>
				<div class="username">
					<text>用户：</text>
					<el-input v-model="state.form.username" style="width: 150px" size="large" disabled />
				</div>
				<div class="startTime">
					<text>开始时间：</text>
					<el-input v-model="state.form.startTime" style="width: 200px" size="large" disabled />
				</div>
				<div class="button-section">
					<el-button type="primary" @click="playVideos" class="predict-button">开始播放</el-button>
				</div>
				<div class="button-section">
					<el-button type="success" @click="pauseVideos" class="predict-button">暂停播放</el-button>
				</div>
			</div>
			<div class="cards" ref="cardsContainer">
				<!-- 左侧区域的视频 -->
				<div class="left" :style="{ width: leftWidth + '%' }">
					<video class="video" v-if="state.form.inputVideo" preload="auto" controls>
						<source :src="state.form.inputVideo" type="video/mp4" />
					</video>
				</div>

				<!-- 分割条 -->
				<div class="splitter" @mousedown="startDrag"></div>

				<!-- 右侧区域的视频 -->
				<div class="right" :style="{ width: 100 - leftWidth + '%' }">
					<video class="video" preload="auto" v-if="state.form.outVideo" controls>
						<source :src="state.form.outVideo" type="video/mp4" />
					</video>
				</div>
			</div>
		</div>
	</div>
</template>


<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import request from '/@/utils/request';
import { useRoute } from 'vue-router';

const route = useRoute();
const leftWidth = ref(50); // 左侧 div 初始宽度
const state = reactive({
	form: {} as any,
	id: '' as any
});

const getData = () => {
	request.get('/api/videoRecords/' + state.id).then((res) => {
		if (res.code == 0) {
			// res.data = JSON.parse(res.data);
			state.form = res.data;
			console.log(state.form);
		} else {
			ElMessage.error(res.msg);
		}
	});
};

const playVideos = () => {
    const leftVideo = document.querySelector('.left .video') as HTMLVideoElement;
    const rightVideo = document.querySelector('.right .video') as HTMLVideoElement;

    if (leftVideo) leftVideo.play();
    if (rightVideo) rightVideo.play();
};

const pauseVideos = () => {
    const leftVideo = document.querySelector('.left .video') as HTMLVideoElement;
    const rightVideo = document.querySelector('.right .video') as HTMLVideoElement;

    if (leftVideo) leftVideo.pause();
    if (rightVideo) rightVideo.pause();
};


// 拖动分割条
const startDrag = (e: MouseEvent) => {
	const cardsContainer = document.querySelector('.cards') as HTMLElement; // 获取父容器
	const startX = e.clientX;
	const startLeftWidth = leftWidth.value;

	const cardsContainerLeft = cardsContainer.getBoundingClientRect().left; // 获取父容器的左边位置
	const cardsContainerWidth = cardsContainer.offsetWidth; // 获取父容器的宽度

	const onMouseMove = (moveEvent: MouseEvent) => {
		const deltaX = moveEvent.clientX - startX;
		let newLeftWidth = startLeftWidth + (deltaX / cardsContainerWidth) * 100;

		// 限制左右区域的宽度范围为 0% 到 100%
		newLeftWidth = Math.min(Math.max(newLeftWidth, 0), 100);
		leftWidth.value = newLeftWidth;
	};

	const onMouseUp = () => {
		document.removeEventListener('mousemove', onMouseMove);
		document.removeEventListener('mouseup', onMouseUp);
	};

	document.addEventListener('mousemove', onMouseMove);
	document.addEventListener('mouseup', onMouseUp);
};


onMounted(() => {
	//获取链接参数
	state.id = route.query.id;
	console.log(state.id);
	getData()
});
</script>

<style scoped lang="scss">
.system-predict-container {
	width: 100%;
	height: 100%;
	display: flex;
	flex-direction: column;
	background-color: #f5f7fa; /* 浅灰色背景 */

	.system-predict-padding {
		padding: 20px;
		width: 100%;
		height: 100%;
		box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); /* 轻微阴影 */
		border-radius: 8px;
		background: #fff; /* 白色背景 */

		.el-table {
			flex: 1;
		}
	}
}

.header {
	width: 100%;
	height: auto;
	display: flex;
	justify-content: start;
	align-items: center;
	gap: 20px; /* 等距对齐 */
	padding-bottom: 10px;
	border-bottom: 2px solid #e0e0e0; /* 分割线 */
	font-size: 14px;

	.model,
	.height,
	.username,
	.startTime {
		display: flex;
		align-items: center;

		text {
			// font-weight: bold;
			color: #333;
			margin-right: 5px;
		}
	}
}

.cards {
	width: 100%;
	height: calc(100% - 50px); /* 减去 header 高度 */
	border-radius: 8px;
	margin-top: 15px;
	padding: 0px;
	display: flex;
	flex-direction: row;
	overflow: hidden;
	background-color: #ffffff; /* 白色背景 */
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); /* 卡片阴影 */
}

.left,
.right {
	flex-grow: 1;
	height: 100%;
	position: relative;
	display: flex;
	justify-content: center;
	align-items: center;
	background: radial-gradient(circle, #d3e3f1 0%, #ffffff 100%);
	border-radius: 8px;
	border: 1px solid #e0e0e0; /* 边框 */
	box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.05); /* 内部阴影 */
}

.video {
	width: 100%;
	max-height: 100%;
	height: auto;
	object-fit: contain;
	border-radius: 8px; /* 圆角视频 */
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); /* 视频阴影 */
}

/* 分割条样式 */
.splitter {
	cursor: ew-resize;
	width: 3px; /* 加宽分割条 */
	background: linear-gradient(to bottom, #d3e3f1, #a7bdd8);
	border-left: none;
	height: 100%;
	transition: all 0.3s ease;

	&:hover {
		background: linear-gradient(to bottom, #a7bdd8, #d3e3f1);
		box-shadow: 0 0 8px rgba(0, 0, 0, 0.2); /* 高亮效果 */
	}
}

</style>