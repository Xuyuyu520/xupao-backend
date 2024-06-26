# 小徐 - 伙伴匹配系统项目

> 作者：[程序员小徐](https://github.com/Xuyuyu520)

本项目为 https://github.com/Xuyuyu520 的原创全栈项目，后端代码开源。

[加入主页](https://github.com/Xuyuyu520) 可获得该项目从 0 到 1 的完整视频教程 + 源码 + 笔记 + 答疑 + 简历写法。

![加入主页](./doc/加入主页.jpeg)



## 项目简介

https://github.com/Xuyuyu520 原创项目，一个帮助大家找到志同道合的伙伴的移动端网站（APP 风格）。包括用户登录注册、更新个人信息、按标签搜索用户、推荐相似用户、组队等功能。

主页：

![](https://yupi-picture-1256524210.cos.ap-shanghai.myqcloud.com/1/image-20221023120231097.png)



找伙伴：

![](https://yupi-picture-1256524210.cos.ap-shanghai.myqcloud.com/1/image-20221023120338802.png)



组队功能：

![](https://yupi-picture-1256524210.cos.ap-shanghai.myqcloud.com/1/image-20221023120253418.png)



创建队伍：

![](https://yupi-picture-1256524210.cos.ap-shanghai.myqcloud.com/1/image-20221023120311527.png)



个人信息及修改：

![img](https://yupi-picture-1256524210.cos.ap-shanghai.myqcloud.com/1/image-20221023120321632.png)



这个该项目基本覆盖了企业开发中常见的需求以及对应的解决方案，比如登录注册、批量数据导入、信息检索展示、定时任务、资源抢占等。并且涵盖了分布式、并发编程、锁、事务、缓存、性能优化、幂等性、数据一致性、大数据、算法等后端程序员必须了解的知识与实践。



从需求分析、技术选型、系统设计、前后端开发再到最后上线，整个项目的制作过程为 **全程直播** ！除了学做项目之外，还能学会很多思考问题、对比方案的方式方法，并提升排查问题、解决 Bug 的能力。此外，还能学习到最最最方便的项目上线方式，几分钟上线一个项目真的轻轻松松！



## 本项目适合的同学

以下两个条件满足一个即可：

1. 已经学过基本的前端（HTML + CSS + JS 三件套），想学、在学或已学 Vue 移动端开发
2. 学习过后端开发技术（比如 Java Web）



## 技术选型

### 前端

- Vue 3
- Vant UI 组件库
- TypeScript
- Vite 脚手架
- Axios 请求库



### 后端

- Java SpringBoot 2.7.x 框架
- MySQL 数据库
- MyBatis-Plus
- MyBatis X 自动生成
- Redis 缓存（Spring Data Redis 等多种实现方式）
- Redisson 分布式锁
- Easy Excel 数据导入
- Spring Scheduler 定时任务
- Swagger + Knife4j 接口文档
- Gson：JSON 序列化库
- 相似度匹配算法



### 部署

- Serverless 服务
- 云原生容器平台



## 项目收获

1. 全程直播开发，带你了解并巩固做项目的完整流程，能够独立开发及上线项目
2. 学会前后端企业主流开发技术（如 Vue 3、Spring Boot 等）的应用，提升开发经验
3. 学习 Java 8 特性、接口文档、网页内容抓取、分布式登录、大数据量导入、并发编程、Redis、缓存及预热、定时任务、分布式锁、幂等性、算法、免备案上线项目等重要知识
4. 通过多次带大家思考和对比实现方案，帮你开拓思路，学习系统设计的方法和经验
5. 学到项目开发、调试和优化技巧，比如开发工具使用技巧、组件抽象封装、问题定位、性能优化、内存优化等
6. 所有 Bug 和问题均为直播解决，带你提升自主解决问题的能力
7. 学习一些思考底层原理的方式、以及源码阅读技巧


## 学习者的反馈

![img.png](doc/img.png)


## 项目大纲

1. 项目简介和计划
2. 需求分析
3. 技术选型（各技术作用讲解）
4. 前端项目初始化
    1. 脚手架
    2. 组件 / 类库引入
5. 前端页面设计及通用布局开发
6. 后端数据库表设计
7. 按标签搜索用户功能
    1. 前端开发
    2. 后端开发
    3. 性能分析
    4. 接口调试
8. Swagger + Knife4j 接口文档整合
9. 后端分布式登录改造（Session 共享）
10. 用户登录功能开发
11. 修改个人信息功能开发
12. 主页开发（抽象通用列表组件）
13. 批量导入数据功能
    1. 几种方案介绍及对比
    2. 测试及性能优化（并发编程）
14. 主页性能优化
    1. 缓存和分布式缓存讲解
    2. Redis 讲解
    3. 缓存开发和注意事项
    4. 缓存预热设计与实现
    5. 定时任务介绍和实现
    6. 锁 / 分布式锁介绍
    7. 分布式锁注意事项讲解
    8. Redisson 分布式锁实战
    9. 控制定时任务执行的几种方案介绍及对比
15. 组队功能
    1. 需求分析
    2. 系统设计
    3. 多个接口开发及测试
    4. 前端多页面开发
    5. 权限控制
16. 随机匹配功能
    1. 匹配算法介绍及实现
    2. 性能优化及测试
17. 项目优化及完善
18. 免备案方式上线前后端


## 项目资料

[加入主页](https://github.com/Xuyuyu520) 可获得：

1. 完整视频教程
2. 视频教程大纲
3. 完整项目源码
4. 项目学习笔记
5. 本项目交流答疑
6. 本项目简历写法
7. 更多原创项目教程和学习专栏

![加入主页](./doc/加入主页.jpeg)


## 版权声明

请尊重原创！与其泄露资料、二次售卖，不如邀请他人加入主页得大额赏金：https://github.com/Xuyuyu520
