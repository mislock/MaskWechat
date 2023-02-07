# MaskWechat

[Source link / 项目地址](https://github.com/Mingyueyixi/MaskWechat)

反馈问题可点击以上地址，发起issues


## 介绍
这是一个微信 Xposed 模块，她可以隐藏掉特定用户的聊天记录，防止私密的聊天被第三人偷看


## 使用说明

### 添加配置

1.  激活模块，作用域勾选微信
2.  在模块App中点击`添加配置`卡片
3.  跳转到微信主页（若微信本身不在首页，请自行返回主页后操作），点击用户发起聊天
4.  模块会抓取糊脸Id（微信用户的唯一id），并弹出对话框。
5.  确认后，点击对话框确定按钮。再次进入聊天页即隐藏与此用户的聊天记录

### 临时解除隐藏（Since v1.6）

v1.6版本：

在聊天记录空白处，连续点击5次以上，每次点击间隔不超过150毫秒（超过则重新计数），则解除隐藏

v1.8版本：
实现了自定义点击次数与时间间隔

### 搜索列表隐藏特定用户所在行（Since V1.7）

v1.7版本新增功能（预计**Only For 8.0.32**）当Wechat主页发起的搜索结果命中糊脸ID时，将隐藏所在行视图


### 清除配置

通过`配置管理`清除即可


PS.

- 模块仅仅是隐藏了视图，不对用户数据进行修改
- 模块目前仅隐藏聊天记录，防止被偷窥，而不会”伪装“或修改好友/群组信息，此类功能暂时不会添加
- 模块正常只隐藏主页相关消息，不包括通知栏等渠道的消息，有较强隐私需求用户，建议关闭微信的`通知显示消息详情`

## 适配版本

8.0.22 (2140) 2022-04-29    
8.0.32 (2300) 2023-01-06

PS.
- 其他版本号以及32位版本未经测试，不保证功能可用
- 模块一般只测试通过了最后一个适配的微信版本，因为作者精力有限+穷没有多余手机测试
- 微信版本集合： https://weixin.qq.com/cgi-bin/readtemplate?lang=zh_CN&t=weixin_faq_list&head=true


## 交流

CI编译telegram频道（频道不能聊天）： 点击添加 [https://t.me/MaskWechatCI](https://t.me/MaskWechatCI)

telegram普通群：点击添加 [https://t.me/MaskWechatX](https://t.me/MaskWechatX)

## 声明

1. 项目旨在测试与学习开发，请勿用于商业用途，请勿用于非法用途  
2. 项目所发布的所有App版本，虽名为release，实际均为开发包，均使用同一个测试签名，因此它将不会在应用市场发布  
3. 项目只保证自身不会包含任何恶意代码，不会主动收集任何个人信息，但不能保证第三方库安全  
4. 您应当知道并理解使用`模块`的风险，使用此模块如造成问题与作者无关  
5. 您只有在清楚并同意本声明的情况下，才可使用本项目的App  
