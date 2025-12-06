# 多端前端 Monorepo（使用 npm）Codex 作业文档

> 适用场景：同一工程内管理 **用户端 H5 / 商户端 H5 / PC 后台**  
> 目标：**可单独打包**也**可一键全量打包**，并支持共享代码。

---

## 1. 目标

将以下三个端拆入同一仓库：

- 用户端 H5（WebView 内嵌，**非 uniapp**）
- 商户端 H5
- PC 后台管理端

实现：

1) 每个端可独立开发、独立构建、独立发布  
2) 根目录可一键构建全部端  
3) 支持共享通用组件/工具/配置  
4) 产物目录清晰、互不污染  

---

## 2. 技术选型（本次固定）

- 包管理：**npm workspaces**
- 任务编排：**Turborepo**（可选但推荐）
- 构建：**Vite**（H5/PC 若已是 Vite）

> 说明：Turborepo 不是包管理器，用 npm 也能正常工作。  
> 如果你后续不想用 Turbo，也能保留 workspaces + 根脚本实现单端/多端构建。

---

## 3. 目标目录结构

```txt
root/
  apps/
    user-h5/          # 用户端 H5
    merchant-h5/      # 商户端 H5
    admin-pc/         # PC 后台
  packages/
    utils/            # 共享工具库（本次必做）
    ui/               # 共享 UI 组件库（可选）
    config/           # 共享 eslint/tsconfig/style（可选）
  package.json
  turbo.json
  tsconfig.base.json  # 可选
```

---

## 4. 迁移策略

若已有三个独立项目：

1) 将三个项目整体移动到 `apps/` 下对应目录  
2) 保留各自原本的 `package.json`（再做脚本对齐）  
3) 在根目录启用 npm workspaces  
4) 逐步收敛可复用代码到 `packages/utils` 或 `packages/ui`  

---

## 5. Codex 需要完成的任务

### 5.1 根目录启用 npm workspaces

创建/修改根 `package.json`：

```json
{
  "name": "multi-client-frontend",
  "private": true,
  "workspaces": [
    "apps/*",
    "packages/*"
  ],
  "scripts": {
    "dev:user": "npm run -w user-h5 dev",
    "dev:merchant": "npm run -w merchant-h5 dev",
    "dev:admin": "npm run -w admin-pc dev",

    "build:user": "npm run -w user-h5 build",
    "build:merchant": "npm run -w merchant-h5 build",
    "build:admin": "npm run -w admin-pc build",

    "build:all": "turbo run build",
    "lint": "turbo run lint",
    "typecheck": "turbo run typecheck"
  },
  "devDependencies": {
    "turbo": "^2.0.0"
  }
}
```

**关键要求：**  
`npm run -w xxx` 依赖 workspace 的 `name` 字段。  
因此三个 app 的 `package.json.name` 必须分别为：

- `user-h5`
- `merchant-h5`
- `admin-pc`

---

### 5.2 初始化 Turborepo

根目录创建 `turbo.json`：

```json
{
  "$schema": "https://turbo.build/schema.json",
  "pipeline": {
    "build": {
      "dependsOn": ["^build"],
      "outputs": ["dist/**", "build/**"]
    },
    "dev": {
      "cache": false
    },
    "lint": {},
    "typecheck": {}
  }
}
```

---

### 5.3 为各 app 补齐/对齐脚本

以 `apps/user-h5/package.json` 为例：

```json
{
  "name": "user-h5",
  "private": true,
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview",
    "lint": "eslint .",
    "typecheck": "tsc -p tsconfig.json --noEmit"
  }
}
```

`merchant-h5`、`admin-pc` 同理。

> 如果 `admin-pc` 原本不是 Vite：  
> - 保留其原有构建命令  
> - 但脚本名尽量仍统一为 `dev/build/lint/typecheck`  
> 以便根目录脚本与 Turbo 能无脑编排。

---

### 5.4 新建共享 utils 包（本次必做）

创建 `packages/utils`：

**`packages/utils/package.json`**
```json
{
  "name": "@shared/utils",
  "version": "0.0.1",
  "main": "index.js"
}
```

**`packages/utils/index.js`**
```js
export function noop() {}
```

---

### 5.5 三端引用共享包

在三个 app 的 `package.json` 添加：

```json
{
  "dependencies": {
    "@shared/utils": "*"
  }
}
```

在任一 app 中写最小验证：

```js
import { noop } from "@shared/utils";
noop();
```

---

### 5.6（可选）共享 TypeScript 配置

如果你们大量使用 TS，可以加：

**根 `tsconfig.base.json`**
```json
{
  "compilerOptions": {
    "target": "ES2020",
    "module": "ESNext",
    "moduleResolution": "Bundler",
    "baseUrl": ".",
    "paths": {
      "@shared/utils": ["packages/utils/index.js"]
    }
  }
}
```

各 app `tsconfig.json`：

```json
{
  "extends": "../../tsconfig.base.json",
  "include": ["src"]
}
```

> 若你计划完全不用 TS，可跳过此节，并同步移除 `typecheck` 脚本或改为占位脚本。

---

### 5.7 统一产物输出目录

各 app 的 `vite.config.*` 确保：

```ts
export default {
  build: {
    outDir: "dist"
  }
}
```

---

## 6. 验收标准（必须全部通过）

### 6.1 安装

```bash
npm install
```

无 workspace 报错、无依赖冲突。

---

### 6.2 单端开发

```bash
npm run dev:user
npm run dev:merchant
npm run dev:admin
```

三者都能正常启动。

---

### 6.3 单端构建

```bash
npm run build:user
npm run build:merchant
npm run build:admin
```

产物分别生成在：

- `apps/user-h5/dist`
- `apps/merchant-h5/dist`
- `apps/admin-pc/dist`

---

### 6.4 全量构建

```bash
npm run build:all
```

可一次性构建三端，无顺序问题。

---

### 6.5 共享包生效

三端均能正确引用 `@shared/utils`。

---

## 7. 交付物清单

Codex 需要输出：

1) 调整后的完整目录结构  
2) 根 `package.json`（含 workspaces + scripts）  
3) `turbo.json`  
4) `packages/utils` 最小实现  
5) 三端 `package.json.name` 与脚本对齐  
6) 若涉及路径/别名变更，需同步修复 import  

---

## 8. 关键注意事项

- 不要把三端强行改成 Vite 多页面；我们需要 **三个独立应用**  
- 尽量不改动业务代码  
- 若 `admin-pc` 基于 RuoYi-Vue3：  
  - 保持其现有工程结构  
  - 仅做 monorepo 接入与脚本对齐  
- `@shared/ui` 与 `@shared/config` 可以后续再抽，本次不是硬指标  

---

## 9. 建议的后续增强（非必做）

- 抽共享 UI：`packages/ui`  
- 根目录统一 eslint + prettier  
- 增加 changeset 或简单版本策略  
- CI：基于 `npm run -w <app> build` 做增量发布  

---

## 10. 根目录脚本速查

```bash
# 单端开发
npm run dev:user
npm run dev:merchant
npm run dev:admin

# 单端构建
npm run build:user
npm run build:merchant
npm run build:admin

# 全量构建
npm run build:all
```

---

**完。**
