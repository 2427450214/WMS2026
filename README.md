# 在线仓库管理系统

基于 Spring Boot + Vue 3 的前后端分离在线仓库管理系统。

## 技术栈

### 后端
- Spring Boot 3.2.5
- Spring Security
- MyBatis
- MySQL 8.0
- Redis 6.2

### 前端
- Vue 3
- Vite
- Element Plus
- ECharts

## 项目结构

```
WMS_2026/
├── wms_backend/         # 后端项目
│   ├── src/main/java/
│   └── tables/          # 数据库建表脚本
└── wms-frontend/        # 前端项目
    └── src/
```

## 功能特性

- 用户管理与权限控制
- 商品管理
- 仓库管理
- 批次管理与效期预警
- 入库/出库管理
- 数据统计可视化
- 操作日志审计

## 快速开始

### 1. 数据库准备

执行 `wms_backend/tables/` 目录下的 SQL 脚本创建数据库和表。

### 2. 后端启动

```bash
cd wms_backend
cp src/main/resources/application.properties.example src/main/resources/application.properties
# 修改配置文件中的数据库连接信息
mvnw spring-boot:run
```

### 3. 前端启动

```bash
cd wms-frontend
npm install
npm run dev
```

## 初始账号

注册时使用邀请码 `RootShengWms` 可创建管理员账号，其他邀请码（或无邀请码）创建普通用户账号。

## License

毕业设计项目
