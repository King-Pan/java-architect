Bootstrap-table 分页pageList显示不全?

￼![Bootstrap-table分页个数](http://upload-images.jianshu.io/upload_images/6331401-3e8e797f7f7b6709.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

前端js设置: pageList: [10, 25, 50, 100]

![前端js配置](http://upload-images.jianshu.io/upload_images/6331401-d713a29877efd9c4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> 原因
* 为什么显示不全是因为数据不足。
* 数据总量超过25不足50显示50、
* 数据总量超过50不足100将会显示100。

