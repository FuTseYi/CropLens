# DeepSeek 密钥配置说明（已更新）

⚠️ **不要按照旧版教程将 API Key 直接粘贴进 Vue 源码。** 前端打包后，密钥可能被其他人读取。

本项目的 DeepSeek 智能建议、智能聊天及温室建议功能现通过 Spring Boot 服务端转发。请参见 [DeepSeek 配置文档](DEEPSEEK.md) 配置服务端环境变量 `DEEPSEEK_API_KEY`，并仅在可信的本地开发环境启用 `DEEPSEEK_PROXY_ENABLED=true`。

公网环境尚未具备认证和限流，不应开启该接口。之前曾粘贴到代码或提交记录中的密钥必须自行撤销并更换。
