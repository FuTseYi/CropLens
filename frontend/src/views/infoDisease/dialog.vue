<template>
	<div class="system-role-dialog-container">
		<el-dialog :title="state.dialog.title" v-model="state.dialog.isShowDialog" width="900px" class="dia">
			<el-form ref="diseaseDialogFormRef" :model="state.form" size="default" label-width="90px">
				<el-row :gutter="20">
					<el-col :span="12" class="mb20">
						<el-form-item label="作物类型">
							<el-select v-model="state.form.cropType" placeholder="请选择作物类型" style="width: 100%">
								<el-option v-for="item in cropTypes" :key="item.value" :label="item.label" :value="item.value" />
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12" class="mb20">
						<el-form-item label="病害名称">
							<el-input v-model="state.form.name" placeholder="请输入病害名称" clearable />
						</el-form-item>
					</el-col>
					<el-col :span="24" class="mb20">
						<el-form-item label="病害症状">
							<el-input type="textarea" v-model="state.form.symptoms" placeholder="请输入病害症状" :rows="6" />
						</el-form-item>
					</el-col>
					<el-col :span="24" class="mb20">
						<el-form-item label="发病原因">
							<el-input type="textarea" v-model="state.form.causes" placeholder="请输入发病原因" :rows="3" />
						</el-form-item>
					</el-col>
					<el-col :span="24" class="mb20">
						<el-form-item label="防治方法">
							<el-input type="textarea" v-model="state.form.prevention" placeholder="请输入防治方法" :rows="6" />
						</el-form-item>
					</el-col>
					<el-col :span="24" class="mb20">
						<el-form-item label="图片">
							<el-upload
								ref="uploadFile"
								class="avatar-uploader"
								action="http://localhost:9999/files/upload"
								:show-file-list="false"
								:on-success="handleAvatarSuccess"
							>
								<img v-if="imageUrl" :src="imageUrl" class="avatar">
								<el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
							</el-upload>
						</el-form-item>
					</el-col>
				</el-row>
			</el-form>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="onCancel" size="default">取 消</el-button>
					<el-button type="primary" @click="onSubmit" size="default">{{ state.dialog.submitTxt }}</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>

<script setup lang="ts" name="systemRoleDialog">
import { nextTick, reactive, ref } from 'vue';
import type { UploadInstance, UploadProps } from 'element-plus';
import { ElMessage } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import request from '/@/utils/request';

// 定义子组件向父组件传值/事件
const emit = defineEmits(['refresh']);

const imageUrl = ref('');
const uploadFile = ref<UploadInstance>();

// 定义作物类型数据
const cropTypes = [
	{ value: '玉米', label: '玉米' },
	{ value: '小麦', label: '小麦' },
	{ value: '水稻', label: '水稻' },
	{ value: '马铃薯', label: '马铃薯' },
	{ value: '棉花', label: '棉花' },
	{ value: '苹果', label: '苹果' },
	{ value: '葡萄', label: '葡萄' },
	{ value: '番茄', label: '番茄' },
	{ value: '草莓', label: '草莓' }
];

// 定义变量内容
const diseaseDialogFormRef = ref();
const state = reactive({
	form: {
		id: null,
		cropType: '',
		name: '',
		symptoms: '',
		causes: '',
		prevention: '',
		images: ''
	},
	dialog: {
		isShowDialog: false,
		type: '',
		title: '',
		submitTxt: '',
	},
});

// 打开弹窗
const openDialog = (type: string, row: any) => {
	if (type === 'edit') {
		state.form = { ...row };
		state.dialog.title = '修改病害信息';
		state.dialog.submitTxt = '修 改';
		imageUrl.value = state.form.images;
	} else {
		state.dialog.title = '新增病害信息';
		state.dialog.submitTxt = '新 增';
		// 清空表单
		nextTick(() => {
			uploadFile.value!.clearFiles();
			imageUrl.value = '';
			state.form = {
				id: null,
				cropType: '',
				name: '',
				symptoms: '',
				causes: '',
				prevention: '',
				images: ''
			};
		});
	}
	state.dialog.isShowDialog = true;
};

// 关闭弹窗
const closeDialog = () => {
	state.dialog.isShowDialog = false;
};

// 取消
const onCancel = () => {
	closeDialog();
};

// 提交
const onSubmit = () => {
	if (state.dialog.title === '修改病害信息') {
		request.put('/api/disease', state.form).then((res) => {
			if (res.code == 0) {
				ElMessage.success('修改成功！');
				setTimeout(() => {
					closeDialog();
					emit('refresh');
				}, 500);
			} else {
				ElMessage({
					type: 'error',
					message: res.msg,
				});
			}
		}).catch(error => {
			ElMessage({
				type: 'error',
				message: '修改失败：' + error.message,
			});
		});
	} else {
		request.post('/api/disease', state.form).then((res) => {
			if (res.code == 0) {
				ElMessage.success('添加成功！');
			} else {
				ElMessage({
					type: 'error',
					message: res.msg,
				});
			}
			setTimeout(() => {
				closeDialog();
				emit('refresh');
			}, 500);
		}).catch(error => {
			ElMessage({
				type: 'error',
				message: '添加失败：' + error.message,
			});
		});
	}
};

// 图片上传成功
const handleAvatarSuccess: UploadProps['onSuccess'] = (response, uploadFile) => {
	imageUrl.value = URL.createObjectURL(uploadFile.raw!);
	state.form.images = response.data;
};

// 暴露变量
defineExpose({
	openDialog,
});
</script>

<style scoped lang="scss">
:deep(.dia) {
	.el-dialog {
		margin-top: 8vh !important;
		.el-dialog__body {
			padding: 20px;
		}
		.el-dialog__header {
			padding: 20px;
			margin-right: 0;
			border-bottom: 1px solid var(--el-border-color-light);
		}
		.el-dialog__footer {
			padding: 20px;
			border-top: 1px solid var(--el-border-color-light);
		}
	}
}

.avatar-uploader {
	:deep(.el-upload) {
		border: 1px dashed var(--el-border-color);
		border-radius: 6px;
		cursor: pointer;
		position: relative;
		overflow: hidden;
		transition: var(--el-transition-duration-fast);
		width: 200px;
		height: 200px;
		display: flex;
		justify-content: center;
		align-items: center;

		&:hover {
			border-color: var(--el-color-primary);
		}
	}
}

.avatar-uploader-icon {
	font-size: 28px;
	color: #8c939d;
	width: 100%;
	height: 100%;
	display: flex;
	justify-content: center;
	align-items: center;
}

.avatar {
	width: 100%;
	height: 100%;
	object-fit: contain;
	display: block;
}

.mb20 {
	margin-bottom: 15px;
}

:deep(.el-form-item__label) {
	font-weight: 500;
}

:deep(.el-input__wrapper),
:deep(.el-textarea__inner) {
	box-shadow: 0 0 0 1px #dcdfe6 inset;
	&:hover {
		box-shadow: 0 0 0 1px var(--el-color-primary) inset;
	}
}
</style> 