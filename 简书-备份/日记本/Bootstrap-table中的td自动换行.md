> 自定义函数:

```
function tdAutoLine(value, row, index) {//赋予的参数
    return '<span style="white-space: pre-wrap;">'+value+'</span>';
}
```

> 在BootstrapTable 需要转行的列中添加如下代码:

```
{
	field: 'todoDesc',
	title: '任务内容描述',
	width:200,
	visible: true,
	formatter: tdAutoLine
}
```

即可
