import request from '/@/utils/request';

export function getUniteLoginUrl(params: any) {
	return request({
		url: 'http://1.15.180.194:9090',
		method: 'GET',
		params,
	});
}
