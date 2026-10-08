<template>
	<div class="system-role-dialog-container">
		<el-dialog :title="state.dialog.title" v-model="state.dialog.isShowDialog" width="700px" class="dia">
			<el-form ref="storageDialogFormRef" :model="state.form" :rules="state.rules" size="default" label-width="80px">
				<el-row :gutter="20">
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb15">
						<el-form-item label="产品" prop="product">
							<el-input v-model="state.form.product" placeholder="请输入产品" clearable />
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb15">
						<el-form-item label="用途" prop="warehouse">
							<el-input v-model="state.form.warehouse" placeholder="请输入用途" clearable />
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb15">
						<el-form-item label="存储区" prop="storageArea">
							<el-select v-model="state.form.storageArea" placeholder="请选择存储区" style="width: 100%">
								<el-option label="1号仓库" value="1号仓库" />
								<el-option label="2号仓库" value="2号仓库" />
								<el-option label="3号仓库" value="3号仓库" />
								<el-option label="4号仓库" value="4号仓库" />
								<el-option label="5号仓库" value="5号仓库" />
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb15">
						<el-form-item label="数量" prop="quantity">
							<el-input-number v-model="state.form.quantity" :min="0" style="width: 100%" />
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb15">
						<el-form-item label="管理员" prop="manager">
							<el-input v-model="state.form.manager" placeholder="请输入仓库管理员" clearable />
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb15">
						<el-form-item label="手机号" prop="phone">
							<el-input v-model="state.form.phone" placeholder="请输入手机号" clearable />
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb15">
						<el-form-item label="备注" prop="remark">
							<el-input v-model="state.form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
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
import { ElMessage } from 'element-plus';
import type { FormInstance } from 'element-plus';
import request from '/@/utils/request';

// 定义子组件向父组件传值/事件
const emit = defineEmits(['refresh']);

// 定义变量内容
const storageDialogFormRef = ref<FormInstance>();
const state = reactive({
	form: {
		id: null,
		product: '',
		warehouse: '',
		storageArea: '',
		quantity: 0,
		manager: '',
		phone: '',
		remark: ''
	},
	rules: {
		product: [{ required: true, message: '请输入产品', trigger: 'blur' }],
		warehouse: [{ required: true, message: '请输入仓库', trigger: 'blur' }],
		storageArea: [{ required: true, message: '请选择存储区', trigger: 'change' }],
		quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }],
		manager: [{ required: true, message: '请输入管理员', trigger: 'blur' }],
		phone: [
			{ required: true, message: '请输入手机号', trigger: 'blur' },
			// { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
		],
		remark: [{ required: true, message: '请输入备注', trigger: 'blur' }]
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
		state.dialog.title = '修改库存信息';
		state.dialog.submitTxt = '修 改';
	} else {
		state.dialog.title = '新增库存信息';
		state.dialog.submitTxt = '新 增';
		// 清空表单
		nextTick(() => {
			state.form = {
				id: null,
				product: '',
				warehouse: '',
				storageArea: '',
				quantity: 0,
				manager: '',
				phone: '',
				remark: ''
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
	if (!storageDialogFormRef.value) return;
	storageDialogFormRef.value.validate((valid: boolean) => {
		if (valid) {
			if (state.dialog.title === '修改库存信息') {
				request.post('/api/storage/update', state.form).then((res) => {
					if (res.code == 0) {
						ElMessage.success('修改成功！');
						closeDialog();
						emit('refresh');
					} else {
						ElMessage({
							type: 'error',
							message: res.msg,
						});
					}
				});
			} else {
				request.post('/api/storage', state.form).then((res) => {
					if (res.code == 0) {
						ElMessage.success('添加成功！');
						closeDialog();
						emit('refresh');
					} else {
						ElMessage({
							type: 'error',
							message: res.msg,
						});
					}
				});
			}
		} else {
			return false;
		}
	});
};

// 暴露变量
defineExpose({
	openDialog,
});
</script>

<style scoped lang="scss">
:deep(.el-dialog) {
	margin-top: 8vh !important;
	.el-dialog__body {
		padding: 15px 20px;
	}
	.el-dialog__header {
		padding: 12px 20px;
		margin-right: 0;
		border-bottom: 1px solid #eee;
	}
	.el-dialog__footer {
		padding: 12px 20px;
		border-top: 1px solid #eee;
	}
}

.el-form {
	width: 100%;
	margin: 0;
	.el-form-item {
		margin-bottom: 15px;
		&:last-child {
			margin-bottom: 0;
		}
	}
}

.mb15 {
	margin-bottom: 15px;
}

:deep(.el-form-item__label) {
	font-weight: 400;
}

:deep(.el-input-number) {
	width: 100%;
}

.dialog-footer {
	text-align: right;
}
</style> 