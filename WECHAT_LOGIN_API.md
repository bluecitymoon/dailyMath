# 微信登录 API 文档

## 概述

本文档描述了微信小程序登录功能的实现和使用方法。

## 功能说明

微信登录功能允许用户通过微信小程序登录系统。第一次登录时会在 `student` 表中创建一条新记录，后续登录会更新用户的微信信息。用户可以在后续通过注册流程完善个人信息。

## 数据库变更

在 `student` 表中添加了以下字段：

- `wechat_user_id` (VARCHAR 255, UNIQUE): 微信用户唯一标识 (openid)
- `wechat_nickname` (VARCHAR 255): 微信昵称
- `wechat_avatar` (VARCHAR 500): 微信头像 URL
- `wechat_signature` (VARCHAR 500): 微信个性签名

## 配置说明

### 1. 环境变量配置

需要在环境变量或配置文件中设置以下参数：

```yaml
wechat:
  app-id: your-wechat-app-id # 微信小程序 AppID
  app-secret: your-wechat-app-secret # 微信小程序 AppSecret
```

可以通过以下方式设置：

**方式一：环境变量**

```bash
export WECHAT_APP_ID=your-app-id
export WECHAT_APP_SECRET=your-app-secret
```

**方式二：application-dev.yml**

```yaml
wechat:
  app-id: wx1234567890abcdef
  app-secret: 1234567890abcdef1234567890abcdef
```

### 2. 获取微信小程序 AppID 和 AppSecret

1. 登录[微信公众平台](https://mp.weixin.qq.com/)
2. 进入"开发" -> "开发管理" -> "开发设置"
3. 在"开发者ID"部分可以找到 AppID 和 AppSecret

## API 接口

### 微信登录

**接口地址：** `POST /api/wechat/login`

**请求参数：**

```json
{
  "code": "微信登录凭证code",
  "encryptedData": "加密数据（可选）",
  "iv": "加密算法的初始向量（可选）",
  "signature": "数据签名（可选）",
  "userInfo": {
    "nickName": "用户昵称",
    "avatarUrl": "用户头像URL",
    "signature": "用户个性签名",
    "gender": 1,
    "city": "城市",
    "province": "省份",
    "country": "国家"
  }
}
```

**响应参数：**

```json
{
  "token": "登录token",
  "student": {
    "id": 1,
    "name": "学生姓名",
    "gender": "性别",
    "wechatUserId": "微信openid",
    "wechatNickname": "微信昵称",
    "wechatAvatar": "微信头像URL",
    "wechatSignature": "微信个性签名",
    ... // 其他学生信息
  },
  "isNewUser": true  // 是否为新用户
}
```

**响应状态码：**

- `200 OK`: 登录成功
- `400 Bad Request`: 请求参数错误
- `500 Internal Server Error`: 服务器错误

## 前端集成示例

### 微信小程序端

```javascript
// 1. 获取微信登录凭证
wx.login({
  success: res => {
    const code = res.code;

    // 2. 获取用户信息
    wx.getUserProfile({
      desc: '用于完善用户资料',
      success: userRes => {
        const userInfo = userRes.userInfo;

        // 3. 调用后端登录接口
        wx.request({
          url: 'https://your-api-domain.com/api/wechat/login',
          method: 'POST',
          data: {
            code: code,
            encryptedData: userRes.encryptedData,
            iv: userRes.iv,
            signature: userRes.signature,
            userInfo: {
              nickName: userInfo.nickName,
              avatarUrl: userInfo.avatarUrl,
              signature: userInfo.signature,
              gender: userInfo.gender,
              city: userInfo.city,
              province: userInfo.province,
              country: userInfo.country,
            },
          },
          success: loginRes => {
            const { token, student, isNewUser } = loginRes.data;

            // 保存token
            wx.setStorageSync('token', token);

            if (isNewUser) {
              // 新用户，跳转到完善信息页面
              wx.navigateTo({
                url: '/pages/register/index',
              });
            } else {
              // 老用户，跳转到首页
              wx.switchTab({
                url: '/pages/index/index',
              });
            }
          },
          fail: err => {
            console.error('登录失败', err);
            wx.showToast({
              title: '登录失败，请重试',
              icon: 'none',
            });
          },
        });
      },
      fail: err => {
        console.error('获取用户信息失败', err);
      },
    });
  },
  fail: err => {
    console.error('wx.login失败', err);
  },
});
```

## 登录流程

1. **前端调用微信登录**

   - 调用 `wx.login()` 获取临时登录凭证 `code`
   - 调用 `wx.getUserProfile()` 获取用户信息

2. **后端处理登录请求**

   - 接收前端传来的 `code` 和用户信息
   - 使用 `code` 向微信服务器换取 `openid` 和 `session_key`
   - 根据 `openid` 查询数据库中是否存在该用户

3. **用户处理**

   - **新用户**：创建新的 student 记录，保存微信信息，标记为新用户
   - **老用户**：更新微信信息（昵称、头像、签名等）

4. **返回响应**

   - 生成登录 token（当前为简单实现，建议使用 JWT）
   - 返回用户信息和是否为新用户的标识

5. **前端处理响应**
   - 保存 token 用于后续 API 调用
   - 根据 `isNewUser` 字段决定跳转页面
     - 新用户：跳转到信息完善页面
     - 老用户：跳转到首页

## 安全建议

1. **HTTPS**: 生产环境必须使用 HTTPS 协议
2. **Token 管理**: 建议使用 JWT 替代当前简单的 token 生成方式
3. **AppSecret 保密**: 不要将 AppSecret 提交到代码仓库，使用环境变量或配置中心管理
4. **数据验证**: 对前端传来的用户信息进行验证和清洗
5. **频率限制**: 建议对登录接口添加频率限制，防止恶意请求

## 数据库迁移

系统使用 Liquibase 进行数据库版本管理。微信相关字段的变更已包含在以下文件中：

```
src/main/resources/config/liquibase/changelog/20251014000000_add_wechat_fields_to_student.xml
```

启动应用时会自动执行数据库迁移。

## 故障排查

### 常见问题

1. **微信 API 调用失败**

   - 检查 AppID 和 AppSecret 是否正确
   - 检查网络连接是否正常
   - 查看后端日志中的错误信息

2. **code 已被使用**

   - 微信的 code 只能使用一次，5分钟内有效
   - 前端不要重复发送相同的 code

3. **用户信息获取失败**
   - 确保微信小程序已配置好服务器域名
   - 检查前端是否正确调用 `wx.getUserProfile()`

### 日志查看

后端会记录以下关键日志：

- 微信登录请求：`Processing WeChat login with code: xxx`
- 微信 openid：`Retrieved WeChat openid: xxx`
- 用户创建/更新：`Creating new student for WeChat user` 或 `Found existing student`

可以通过这些日志追踪问题。

## 后续优化

1. **JWT Token**: 使用 JWT 替代简单的 UUID token
2. **Session 管理**: 实现 session_key 的存储和管理，用于解密微信数据
3. **手机号获取**: 支持获取用户手机号
4. **多端登录**: 支持同一用户在多个设备登录
5. **Token 刷新**: 实现 token 过期和刷新机制
