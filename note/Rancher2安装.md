## 



```sh
docker run -d --restart=unless-stopped -p 80:80 -p 443:443 \
-v /ranchar/ancher:/var/lib/rancher \
-v /ranchar/auditlog:/var/log/auditlog \
--name rancher rancher/rancher
```



## 本周重点工作:

1. 消息中心项目与RokcetMQ集成
2. RocketMQ重复消费问题处理
3. 武汉商旅-外包人员招聘
4. 外包人员蒋鹏程入职培训工作交接
5. 订单总表etl调整
6. 在线分析报表bug修复
7. 企业开户和多钱包映射配置

## 下周工作计划

1. 消息中心-失败重试，失败告警功能详细设计
2. 武汉商旅-外包人员招聘
3. 数仓etl交接
4. 0924迭代发版



| 1. 消息中心项目与RokcetMQ集成 2. RocketMQ重复消费问题处理 |
| --------------------------------------------------------- |
| 1.  武汉商旅-外包人员、公司内部人员招聘                   |

