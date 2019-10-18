```
server {
    listen       443 ssl;
    server_name  api.druglots.cn;
    # ssl on;   #设置为on启用SSL功能。
    ssl_certificate cert\2911584_api.druglots.cn.pem;   #将domain name.pem替换成您证书的文件名。
    ssl_certificate_key cert\2911584_api.druglots.cn.key;   #将domain name.key替换成您证书的密钥文件名。
    ssl_session_timeout 5m;
    ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:ECDHE:ECDH:AES:HIGH:!NULL:!aNULL:!MD5:!ADH:!RC4;  #使用此加密套件。
    ssl_protocols TLSv1 TLSv1.1 TLSv1.2;   #使用该协议进行配置。
    ssl_prefer_server_ciphers on;   

    location / {
        root   test;
        index  index.html index.htm;
        try_files $uri $uri/ /index.html;
    }

    location = /50x.html {
    	root   html;
    }
}
```

