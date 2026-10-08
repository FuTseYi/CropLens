<template>
	<div>
		<el-dialog
			v-model="state.isShowDialog"
			width="1200px"
			draggable
			:close-on-click-modal="false"
			:close-on-press-escape="false"
		>
			<div class="disease-detail">
				<div class="detail-header">
					<div class="title">{{ state.disease.name }}</div>
				</div>
				<div class="detail-content">
					<div class="content-left">
						<div class="section">
							<div class="section-title">
								<el-icon><ele-Warning /></el-icon>
								为害症状
							</div>
							<div class="section-content">{{ state.disease.symptoms }}</div>
						</div>
						<div class="section">
							<div class="section-title">
								<el-icon><ele-InfoFilled /></el-icon>
								发生因素
							</div>
							<div class="section-content">{{ state.disease.causes }}</div>
						</div>
						<div class="section">
							<div class="section-title">
								<el-icon><ele-Operation /></el-icon>
								防治方法
							</div>
							<div class="section-content">{{ state.disease.prevention }}</div>
						</div>
					</div>
					<div class="content-right">
						<div class="section">
							<div class="section-title">
								<el-icon><ele-Picture /></el-icon>
								案例图片
							</div>
							<div class="section-content image-content">
								<el-image
									style="width: 400px; height: 400px; border-radius: 8px;"
									:src="state.disease.image"
									:preview-src-list="[state.disease.image]"
									:preview-teleported="true"
									:hide-on-click-modal="true"
									fit="cover"
									:preview-options="{
										zoom: false,
										closeOnPressEscape: true,
										toolbar: false
									}"
								/>
							</div>
						</div>
					</div>
				</div>
			</div>
		</el-dialog>
	</div>
</template>

<script setup lang="ts" name="diseaseDetail">
import { reactive } from 'vue';

// 定义变量内容
const state = reactive({
	isShowDialog: false,
	disease: {} as any,
});

// 打开弹窗
const openDialog = (row: any) => {
	state.disease = row;
	state.isShowDialog = true;
};

// 暴露方法
defineExpose({ openDialog });
</script>

<style scoped lang="scss">
.disease-detail {
	padding: 0 20px 20px 20px;
	.detail-header {
		text-align: center;
		margin-bottom: 25px;
		margin-top: -10px;
		.title {
			font-size: 32px;
			font-weight: bold;
			color: #303133;
		}
	}
	.detail-content {
		display: flex;
		gap: 30px;
		.content-left {
			flex: 1;
			.section {
				margin-bottom: 25px;
				.section-title {
					font-size: 20px;
					font-weight: bold;
					color: #303133;
					margin-bottom: 15px;
					display: flex;
					align-items: center;
					.el-icon {
						margin-right: 8px;
						font-size: 22px;
						color: #409EFF;
					}
				}
				.section-content {
					font-size: 16px;
					color: #606266;
					line-height: 2;
					text-align: justify;
					padding: 20px;
					background: #f5f7fa;
					border-radius: 8px;
				}
			}
		}
		.content-right {
			width: 450px;
			.section {
				.section-title {
					font-size: 20px;
					font-weight: bold;
					color: #303133;
					margin-bottom: 15px;
					display: flex;
					align-items: center;
					.el-icon {
						margin-right: 8px;
						font-size: 22px;
						color: #409EFF;
					}
				}
				.image-content {
					display: flex;
					justify-content: center;
					align-items: center;
					padding: 20px;
					background: #f5f7fa;
					border-radius: 8px;
				}
			}
		}
	}
}
</style> 