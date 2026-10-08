<template>
	<div class="system-role-dialog-container">
		<el-dialog :title="state.dialog.title" v-model="state.dialog.isShowDialog" width="900px" class="dia">
			<el-form ref="greenhouseDialogFormRef" :model="state.form" size="default" label-width="100px" :rules="state.rules">
				<el-row :gutter="20">
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb15">
						<el-form-item label="温室名称" prop="greenhouseName">
							<el-input v-model="state.form.greenhouseName" placeholder="请输入温室名称" clearable />
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb15">
						<el-form-item label="作物类型" prop="cropType">
							<el-input v-model="state.form.cropType" placeholder="请输入作物类型" clearable />
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb15">
						<el-form-item label="数量" prop="quantity">
							<el-input-number v-model="state.form.quantity" :min="0" style="width: 100%" />
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb15">
						<el-form-item label="生长状态" prop="growthStatus">
							<el-input v-model="state.form.growthStatus" placeholder="请输入生长状态" clearable />
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb15">
						<el-form-item label="温度(℃)" prop="temperature">
							<el-input-number v-model="state.form.temperature" :precision="1" :step="0.1" style="width: 100%" />
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb15">
						<el-form-item label="空气湿度" prop="airHumidity">
							<el-input-number v-model="state.form.airHumidity" :min="0" :max="100" style="width: 100%" />
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb15">
						<el-form-item label="土壤湿度" prop="soilHumidity">
							<el-input-number v-model="state.form.soilHumidity" :min="0" :max="100" style="width: 100%" />
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb15">
						<el-form-item label="CO2浓度" prop="co2Concentration">
							<el-input-number v-model="state.form.co2Concentration" :min="0" style="width: 100%" />
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb15">
						<el-form-item label="土壤pH" prop="soilPh">
							<el-input-number v-model="state.form.soilPh" :precision="1" :step="0.1" :min="0" :max="14" style="width: 100%" />
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb15">
						<el-form-item label="光照强度" prop="lightIntensity">
							<el-input-number v-model="state.form.lightIntensity" :min="0" style="width: 100%" />
						</el-form-item>
					</el-col>
					<!-- <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb15">
						<el-form-item label="补光状态" prop="supplementaryLight">
							<el-select v-model="state.form.supplementaryLight" style="width: 100%">
								<el-option label="开启" value="开启" />
								<el-option label="关闭" value="关闭" />
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb15">
						<el-form-item label="通风状态" prop="ventilation">
							<el-select v-model="state.form.ventilation" style="width: 100%">
								<el-option label="开启" value="开启" />
								<el-option label="关闭" value="关闭" />
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb15">
						<el-form-item label="灌溉状态" prop="irrigation">
							<el-select v-model="state.form.irrigation" style="width: 100%">
								<el-option label="开启" value="开启" />
								<el-option label="关闭" value="关闭" />
							</el-select>
						</el-form-item>
					</el-col> -->
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb15">
						<el-form-item label="负责人" prop="manager">
							<el-input v-model="state.form.manager" placeholder="请输入负责人" clearable />
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb15">
						<el-form-item label="记录时间" prop="recordTime">
							<el-date-picker
								v-model="state.form.recordTime"
								type="datetime"
								placeholder="请选择记录时间"
								style="width: 100%"
								value-format="YYYY-MM-DD HH:mm:ss"
							/>
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
const greenhouseDialogFormRef = ref<FormInstance>();
const state = reactive({
	form: {
		id: null,
		greenhouseName: '',
		cropType: '',
		quantity: 0,
		growthStatus: '',
		temperature: 0,
		airHumidity: 0,
		soilHumidity: 0,
		co2Concentration: 0,
		soilPh: 0,
		lightIntensity: 0,
		// supplementaryLight: '关闭',
		// ventilation: '关闭',
		// irrigation: '关闭',
		manager: '',
		recordTime: ''
	},
	rules: {
		greenhouseName: [{ required: true, message: '请输入温室名称', trigger: 'blur' }],
		cropType: [{ required: true, message: '请输入作物类型', trigger: 'blur' }],
		quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }],
		growthStatus: [{ required: true, message: '请输入生长状态', trigger: 'blur' }],
		temperature: [{ required: true, message: '请输入温度', trigger: 'blur' }],
		airHumidity: [{ required: true, message: '请输入空气湿度', trigger: 'blur' }],
		soilHumidity: [{ required: true, message: '请输入土壤湿度', trigger: 'blur' }],
		co2Concentration: [{ required: true, message: '请输入CO2浓度', trigger: 'blur' }],
		soilPh: [{ required: true, message: '请输入土壤pH值', trigger: 'blur' }],
		lightIntensity: [{ required: true, message: '请输入光照强度', trigger: 'blur' }],
		// supplementaryLight: [{ required: true, message: '请选择补光状态', trigger: 'change' }],
		// ventilation: [{ required: true, message: '请选择通风状态', trigger: 'change' }],
		// irrigation: [{ required: true, message: '请选择灌溉状态', trigger: 'change' }],
		manager: [{ required: true, message: '请输入负责人', trigger: 'blur' }],
		recordTime: [{ required: true, message: '请选择记录时间', trigger: 'change' }]
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
		state.dialog.title = '修改温室信息';
		state.dialog.submitTxt = '修 改';
	} else {
		state.dialog.title = '新增温室信息';
		state.dialog.submitTxt = '新 增';
		// 清空表单
		nextTick(() => {
			state.form = {
				id: null,
				greenhouseName: '',
				cropType: '',
				quantity: 0,
				growthStatus: '',
				temperature: 0,
				airHumidity: 0,
				soilHumidity: 0,
				co2Concentration: 0,
				soilPh: 0,
				lightIntensity: 0,
				// supplementaryLight: '关闭',
				// ventilation: '关闭',
				// irrigation: '关闭',
				manager: '',
				recordTime: ''
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
	if (!greenhouseDialogFormRef.value) return;
	greenhouseDialogFormRef.value.validate((valid: boolean) => {
		if (valid) {
			if (state.dialog.title === '修改温室信息') {
				request.post('/api/greenhouse/update', state.form).then((res) => {
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
				request.post('/api/greenhouse', state.form).then((res) => {
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
:deep(.dia) {
	.el-dialog {
		margin-top: 8vh !important;
		.el-dialog__body {
			padding: 15px 20px;
		}
		.el-dialog__header {
			padding: 15px 20px;
			margin-right: 0;
		}
		.el-dialog__footer {
			padding: 15px 20px;
		}
	}
}

.el-form {
	width: 100%;
	margin: 0;
}

.mb15 {
	margin-bottom: 15px;
}
</style>