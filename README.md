# 一个简易论坛项目

前端：bootstrap,vue

后端：springboot2，mybatis3

登录验证：shiro

数据库：mysql8.0，redis（缓存）

api文档：swagger2

主要功能：登录，注册，重置密码，发布帖子，回帖，楼中楼，对有权限的内容进行删除，接收消息，个人信息查看修改。

## 测试运行

1. 导入idea，修改项目配置文件application-dev.yml。配置mysql，redis，邮箱连接。日志文件位置。

2. 默认头像文件名是default.jpg，需要在nginx配置的图片路径中添加该文件，默认图片文件名由数据库的user.avatar字段的默认值设置

3. nginx添加配置

   ```
   # 图片服务器位置，由于nginx,tomcat,图片服务器在同一台电脑，所以配置的本地
   location /avatar/ {
       root E:/;
       error_page 405 =200 $uri;
       autoindex  on;
   }
   # 反向代理到项目位置和修改cookie路径
   location /bbs/ {
       proxy_cookie_path /bbs /;
       proxy_pass http://localhost:8080;
   }
   # 网页文件位置和url重写
   location / {
       root   E:/bbs/static/;
       index  index.html;
       rewrite ^/(\w+)(/\d*)?$ /page/$1.html;
   }
   # 404页面位置
   error_page  404              /page/404.html;
   ```

