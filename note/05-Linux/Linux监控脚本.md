# Linux监控脚本



```shell
#!/bin/sh
start_time=\"`date +%Y%m%d000000 -d '-1days'`\"
#start_time=\"`date +%Y%m%d000000`\"
#end_time=\"`date +%Y%m%d000000 -d '+1days'`\"
end_time=\"`date +%Y%m%d000000`\"
curr_time=`date +%Y%m%d%H`
#接口调用，详单查询
flag=`curl -H "Content-Type: application/json" -sd '{"phoneNo":"18802789452","startTime":'$start_time',"endTime":'$end_time',"startIndex":"1","offset":"5","opName":"test","opId":"00001","srcSystemCode":"tas","appId":""}' http://10.25.176.136:18090/hq/qry/detail`
#echo $flag
echo $flag > $curr_time.txt
#获取查询状态结果
retCode=`grep -Po 'retCode[" :]+\K[^"]+' $curr_time.txt`
result=`grep -Po 'retMsg[" :]+\K[^"]+' $curr_time.txt`
#获取详单条数
count=`grep -Po 'totalCount[" :]+\K[^"]+' $curr_time.txt`
if [ $retCode = "0" ]
then
   echo "$result,详单条数为${count%%'}'*}条"
else
   echo "$result,请联系开发人员"
fi
```

