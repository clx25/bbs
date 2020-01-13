# 一个简易论坛项目

前端：bootstrap,vue

后端：SSM

数据库：mysql8.0，redis

## 使用

1. 修改项目配置文件application-dev.yml。配置mysql，redis，邮箱连接。日志文件位置。

2. nginx添加配置

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

