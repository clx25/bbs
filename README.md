# 一个简易论坛项目

前端：Bootstrap,Vue

后端：Spring Boot，MyBatis

登录验证：Shiro

数据库：MySQL，Redis

api文档：Swagger

src
├─main
│  ├─java
│  │  └─com
│  │      └─bbs
│  │          ├─annotation	//自定义注解
│  │          ├─config		//配置类
│  │          ├─controller	//控制层
│  │          ├─entity		//实体类
│  │          │  ├─database	//数据库相关
│  │          │  ├─dto		//接收页面数据
│  │          │  └─vo		//返回页面数据
│  │          ├─exception		//统一异常处理
│  │          │  └─custom 		//自定义异常
│  │          ├─interceptor	//拦截器
│  │          ├─mapper		//mapper接口
│  │          │  └─validation	//mapper入参校验
│  │          ├─service	//service层接口
│  │          │  └─impl		//service层实现类
│  │          ├─shiro			//重写部分shiro类，使shiro支持RESTful
│  │          │  └─realm		
│  │          └─util		//工具包
│  │
│  └─resources	//springboot配置文件
│      └─mapper	//MyBatis xml映射文件
└─test
    └─java
        └─com
            └─bbs	//测试类

功能：

1. 用户模块：登录，注册，重置密码，修改个人信息、头像，安全信息可选择是否保密，查看发帖记录

2. 验证码模块：发送邮箱验证码，保存，校验

3. 版块模块：查看版块

4. 帖子、楼中楼模块：查看，发布，对有权限的内容进行删除

5. 消息模块：查看收到的回帖，标记已读，查看历史消息

6. 聊天模块：多人聊天

   [![8CZ4PA.gif](https://s2.ax1x.com/2020/03/09/8CZ4PA.gif)](https://imgchr.com/i/8CZ4PA)

## 测试运行

1. 修改项目配置文件application-dev.yml。配置mysql，redis，邮箱连接。日志文件位置。
2. 配置nginx
3. 导入sql
4. 默认头像文件名是default.jpg，需要在nginx配置的图片路径中添加该文件，默认图片文件名由user表的avatar字段的默认值设置
