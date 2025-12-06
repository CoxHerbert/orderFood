# 点餐管理系统 MVP 需求 & 技术规格（通用 + RuoYi 版）

本说明文档用于指导 AI 代码助手（例如 Codex）在现有 **RuoYi** 基础工程上，开发一个点餐管理系统的第一版（MVP）。

---

## 0. 范围与目标

### 0.1 目标

实现一个餐厅点餐系统的 MVP，包含：

-   **用户端 H5**

    -   从二维码进入门店/桌台
    -   浏览菜单
    -   加入购物车
    -   创建订单
    -   查看订单详情

-   **商家端 H5/Pad**

    -   查看待处理订单列表
    -   查看订单详情
    -   接单、标记出餐

-   **PC 后台管理端（简单）**
    -   门店管理
    -   桌台管理
    -   菜单管理（分类 + 菜品 + 规格）
    -   订单查询

### 0.2 非目标（后续迭代）

以下均不在 MVP 范围内，仅预留扩展空间：

-   会员/登录、积分、优惠券
-   支付联调（微信/支付宝）
-   复杂库存/估清
-   档口/后厨分单、大屏、打印
-   外卖配送/到店自取

---

## 1. 技术栈与 RuoYi 约定

### 1.1 后端

-   基于 **RuoYi** 后端框架（Spring Boot + MyBatis）
-   包结构示例（按领域）：

    -   `com.ruoyi.order.domain` 实体类（继承 `BaseEntity`）
    -   `com.ruoyi.order.mapper` Mapper 接口 + XML
    -   `com.ruoyi.order.service` Service 接口
    -   `com.ruoyi.order.service.impl` Service 实现
    -   `com.ruoyi.order.controller` 控制器（继承 `BaseController`）

-   Controller 返回：
    -   使用 `com.ruoyi.common.core.domain.AjaxResult`
    -   列表分页使用 `TableDataInfo`（本期用户/商家接口可不分页）

### 1.2 前端

-   基于若依前端（Vue + Element 或 Vue3 + Element Plus）
-   API 封装使用现有 `@/utils/request`
-   新增模块建议结构：
    -   `src/api/order/*.js`
    -   `src/views/order/user/*.vue`（用户端 H5 页面）
    -   `src/views/order/merchant/*.vue`（商家端页面）

### 1.3 统一接口前缀

-   用户端接口：`/api/user/**`
-   商家端接口：`/api/merchant/**`
-   后台管理接口：`/api/admin/**` 或沿用若依 `/system/**` 等风格

---

## 2. 领域模型（数据表与实体）

> 所有实体类继承 `BaseEntity`，包含 `createTime`、`updateTime` 等字段。

### 2.1 门店 Store

-   表名：`store`
-   字段（核心）：
    -   `id` (bigint) 主键
    -   `name` 门店名称
    -   `code` 门店编码（唯一）
    -   `address` 地址
    -   `enabled` 是否启用（0/1）

### 2.2 桌台 StoreTable

-   表名：`store_table`
-   字段：
    -   `id`
    -   `store_id`
    -   `table_code` 桌码
    -   `table_name` 桌名
    -   `qrcode_content` 二维码内容（如 URL 或参数）
    -   `enabled`

### 2.3 菜品分类 Category

-   表名：`category`
-   字段：
    -   `id`
    -   `store_id`
    -   `name` 分类名称
    -   `sort_order` 排序

### 2.4 菜品 SPU（ProductSpu）

-   表名：`product_spu`
-   字段：
    -   `id`
    -   `store_id`
    -   `category_id`
    -   `name` 菜品名称
    -   `description` 描述
    -   `image_url` 主图
    -   `on_sale` 是否上架（0/1）

### 2.5 菜品 SKU（ProductSku）

-   表名：`product_sku`
-   字段：
    -   `id`
    -   `spu_id`
    -   `sku_name` 规格名（如 大份/中份/加料 等）
    -   `price` 单价（decimal(10,2)）
    -   `available` 是否可售（0/1，用于估清）

> 首版不做复杂加料，仅支持一个 SPU 对多规格 SKU。

### 2.6 订单 Order

-   表名：`orders`
-   字段：
    -   `id`
    -   `store_id`
    -   `table_id` 堂食桌台 ID
    -   `user_id` 用户标识（暂存字符串）
    -   `total_amount` 订单总金额
    -   `pay_amount` 实付金额（= 总金额）
    -   `status` 订单状态（字符串）
    -   `remark` 备注（可选）

### 2.7 订单明细 OrderItem

-   表名：`order_item`
-   字段：
    -   `id`
    -   `order_id`
    -   `spu_id`
    -   `sku_id`
    -   `product_name` 冗余：菜品名快照
    -   `sku_name` 冗余：规格名快照
    -   `quantity`
    -   `unit_price`
    -   `total_price`

### 2.8 订单状态枚举

-   `CREATED` 用户已提交订单
-   `CONFIRMED` 商家已接单
-   `COOKING` 制作中（预留）
-   `READY` 已出餐
-   `COMPLETED` 已完成
-   `CANCELLED` 已取消

实体中可用 `String status` 存上述枚举值。

---

## 3. 接口设计（Rest API）

所有接口使用 RuoYi 的 `AjaxResult` 返回：

```jsonc
{
  "code": 200,
  "msg": "操作成功",
  "data": { ... }
}
```

### 3.1 用户端：获取门店菜单

-   方法：`GET /api/user/stores/{storeId}/menu`
-   功能：按门店返回分类 + 菜品 + 规格列表

返回结构（逻辑结构）：

```ts
type StoreMenuResponse = CategoryMenuDTO[];

interface CategoryMenuDTO {
    categoryId: number;
    categoryName: string;
    products: ProductDTO[];
}

interface ProductDTO {
    spuId: number;
    name: string;
    description: string;
    imageUrl: string;
    skus: SkuDTO[];
}

interface SkuDTO {
    skuId: number;
    skuName: string;
    price: number;
}
```

规则：

-   只返回：
    -   `on_sale = 1` 的 ProductSpu
    -   `available = 1` 的 ProductSku

---

### 3.2 用户端：创建订单

-   方法：`POST /api/user/orders`
-   请求体：

```ts
interface CreateOrderRequest {
    storeId: number;
    tableId?: number;
    userId?: string; // MVP 前端可传固定字符串，如 "mock-user-1"
    items: {
        skuId: number;
        quantity: number; // >= 1
    }[];
}
```

-   响应：`OrderSummaryDTO`（见下）

创建逻辑概要：

1. 校验 items 非空。
2. 校验每个 skuId 对应的 ProductSku 存在且 `available = 1`。
3. 根据请求创建 Order，初始化 status = `CREATED`。
4. 为每项生成 OrderItem：
    - 根据 SKU（和 SPU）取出名称/价格。
    - 单价 \* 数量 = 行小计。
5. 汇总 totalAmount，设置 payAmount = totalAmount。
6. 返回完整订单详情（OrderSummaryDTO）。

---

### 3.3 用户端：查询订单详情

-   方法：`GET /api/user/orders/{id}`
-   响应：`OrderSummaryDTO`

```ts
interface OrderSummaryDTO {
    id: number;
    storeId: number;
    tableId?: number;
    totalAmount: number;
    payAmount: number;
    status: string; // CREATED / CONFIRMED / READY ...
    createTime: string;

    items: {
        id: number;
        productName: string;
        skuName: string;
        quantity: number;
        unitPrice: number;
        totalPrice: number;
    }[];
}
```

---

### 3.4 商家端：订单列表

-   方法：`GET /api/merchant/orders`
-   参数：
    -   `storeId` 必填
    -   `status` 选填，默认为 `CREATED`
-   响应：`List<MerchantOrderListItem>`

```ts
interface MerchantOrderListItem {
    id: number;
    storeId: number;
    tableId?: number;
    totalAmount: number;
    payAmount: number;
    status: string;
    createTime: string;
}
```

> 实体可直接由 Order 转 DTO。

---

### 3.5 商家端：订单详情

-   方法：`GET /api/merchant/orders/{id}`
-   响应：`OrderSummaryDTO`（同用户端）

---

### 3.6 商家端：接单 / 出餐

-   接单：`POST /api/merchant/orders/{id}/confirm`
    -   行为：状态改为 `CONFIRMED`
-   出餐：`POST /api/merchant/orders/{id}/ready`
    -   行为：状态改为 `READY`
-   响应：`AjaxResult.success()` 即可

MVP 阶段可不做严格状态流转校验（后续扩展）。

---

## 4. Codex / AI 助手任务拆分

> 下面定义了一组“任务”，用于驱动 Codex 在 RuoYi 工程中自动写代码。

### 4.1 通用 CRUD 模块生成（示例：Order）

**目标：** 生成 RuoYi 风格的 Order 模块：domain / mapper / xml / service / impl。

任务说明（用于 Prompt）：

-   实体名：Order
-   包名前缀：`com.ruoyi.order`
-   表名：`orders`
-   字段：见 2.6
-   要求：
    -   实体继承 `BaseEntity`
    -   Mapper 接口 + XML 按若依代码生成器风格
    -   Service 接口 + ServiceImpl 标准实现

同样模式可用于：Store / StoreTable / Category / ProductSpu / ProductSku / OrderItem。

---

### 4.2 任务：用户端菜单查询接口

-   创建 `IUserMenuService` / `UserMenuServiceImpl`
-   创建 `UserMenuController`
-   实现 `GET /api/user/stores/{storeId}/menu`
-   返回 `List<CategoryMenuDTO>`，结构见 3.1

---

### 4.3 任务：用户端下单 & 查询接口

-   DTO：
    -   `CreateOrderRequest`
    -   `OrderSummaryDTO` + `OrderItemDTO`
-   在 `IOrderService` 增加：
    -   `OrderSummaryDTO createOrder(CreateOrderRequest request);`
    -   `OrderSummaryDTO getOrderDetail(Long orderId);`
    -   `int updateOrderStatus(Long orderId, String status);`
-   在 `OrderServiceImpl` 实现上述逻辑
-   控制器：
    -   `UserOrderController`（继承 `BaseController`）
    -   `POST /api/user/orders`
    -   `GET /api/user/orders/{id}`

---

### 4.4 任务：商家端订单管理接口

-   DTO：
    -   `MerchantOrderListItem`
-   在 `OrderMapper` 增加按门店+状态查询方法，及对应 XML
-   在 Service 中增加查询方法
-   新建 `MerchantOrderController`：
    -   `GET /api/merchant/orders` 列表
    -   `GET /api/merchant/orders/{id}` 详情
    -   `POST /api/merchant/orders/{id}/confirm`
    -   `POST /api/merchant/orders/{id}/ready`

---

## 5. 前端页面概要（给 Codex 生成 Vue 代码用）

### 5.1 用户端 H5

1. `/menu` 菜单页

    - 从 URL query 拿 `storeId`、`tableId`
    - 调 `/api/user/stores/{storeId}/menu`
    - 左侧分类、右侧菜品
    - 本地购物车

2. `/order/confirm` 确认页

    - 展示购物车明细
    - 组装 `CreateOrderRequest`
    - 调 `/api/user/orders`
    - 跳转 `/order/{id}`

3. `/order/:id` 详情页
    - 调 `/api/user/orders/{id}`
    - 展示状态、明细、金额

### 5.2 商家端 H5

1. `/merchant/orders`

    - 按状态 Tab 请求 `/api/merchant/orders`
    - 展示列表

2. `/merchant/orders/:id`
    - 请求订单详情
    - 按状态展示「接单」「已出餐」按钮
    - 调用对应接口修改状态

---

本文件的作用：

-   作为**统一规范**，告诉 AI 助手：表结构、接口、返回格式、RuoYi 风格约束。
-   搭配具体 Prompt（任务描述），可以让 Codex 在现有 RuoYi 工程中自动生成：
    -   实体 + Mapper + XML + Service + Controller
    -   前端 API 封装 + Vue 页面

开发时，你只需要：

-   打开目标文件，贴上对应“任务描述 Prompt”，
-   让 Codex/AI 按本规范自动写代码，
-   再根据实际工程做少量调整和联调即可。
