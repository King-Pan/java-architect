# RocketMQ高性能分析之zeroCopy零拷贝技术

> 高效原因

​	CommitLog顺序写，存储了MessageBody、messagekey、tag等信息

​	ConsumeQueue随机读 + 操作系统的PageCache + 零拷贝技术ZeroCopy

​	零拷贝技术:

​		read(file,tmp_buf,len);

​	 	write(socket,tmp_buf,len);

例子:将一个file读取并发送出去(Linux有两个上下文，内核态和用户态)

​	File文件经历的四次copy

* 调用read将文件拷贝到kernel内核状态

* CPU控制kernel太的数据copy到用户态

* 调用write时，用户态下的内容会copy到内核态的buffer中

* 最后内核态socket buffer的数据copy到网卡设备中传送

  

  缺点: 增加了上下文切换，浪费了2次无效拷贝(即步骤2、3)

ZeroCopy:

​	请求kernel直接把disk上的data传输给socket，而不是通过应用程序传输。Zero Copy大大提高了应用程序的性能，减少了不必要的内核缓冲区和用户缓冲区的拷贝，从而减少CPU的开销和减少了kernel和user模式下的上下文切换，达到性能的提升。

​	对应零拷贝技术有mmap及shendfile

​	mmap：小文件传输快

​	RocketMQ选择这种方式: mmap+write方式，小块数据传输，效果会比sendfile更好

​	sendfile:大文件传输比mmap快

java中的transferTo()实现了Zero-Copy

应用： kafka、Netty、RocketMQ等都采用了零拷贝技术。



​	