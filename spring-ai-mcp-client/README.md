# 前提条件
  NOTE: 在运行前一定要先本地执行安装，如果本地都存在则忽略
  - brew install node
  - npm install -g npx
  - npm install -g @executeautomation/playwright-mcp-server
  - npm install -g @modelcontextprotocol/server-filesystem

# Client Examples

## Java SDK 方式调用 server-filesystem
- 运行McpClientJavaSdkExamples 如果出现如下错误
![报错信息.png](images/%E6%8A%A5%E9%94%99%E4%BF%A1%E6%81%AF.png)

- 排查问题，看前提条件是否满足

- 测试结果
![img.png](images/img.png)


## LLM 与 MCP 结合方式
- 集成两个官方或者第三方提供的 Mcp Server
    - @modelcontextprotocol/server-filesystem 本地文件管理
    - @executeautomation/playwright-mcp-server 操作浏览器
- 大模型方式两种
    - 本地部署千问大模型，需要自己安装 ollama 以及 qwen2.5:latest 
    - 使用阿里大模型平台，大家自行注册，修改 spring.ai.openai.api-key 即可

 ### 测试脚本
- [http-test.http](http-test.http)
大家自行运行，观察效果


# 官方提供的 Mcp-servers
https://github.com/modelcontextprotocol/servers


# playwright-mcp-server

## 作用及提供的能力
Playwright 是一个端到端（E2E）测试框架， 它可在所有现代浏览器中运行功能强大的测试和自动化。用来模拟用户真实操作等。
- 自动化测试
- 数据抓取
- 网页分析
- 智能代理


https://executeautomation.github.io/mcp-playwright/



