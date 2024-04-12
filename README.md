# 基于Luckysheet实现的协同编辑在线表格

从 Luckysheet 社区克隆过来的，修改了部分代码，主要逻辑暂时没有变动。原项目只是一个demo，具备了一些简单的表格协同编辑能力，但没有对多用户的并发操作做处理，需要进一步完善。

本项目为前后端一体，技术栈如下：

> * 框架：SpringBoot + Vue + FreeMarker
> * 数据库：MongoDB 7.0.8
> * 前端核心：Luckysheet

需要在本地安装好 MongoDB 数据库，然后在配置文件里配置数据库路径。

成功启动项目后，访问 **localhost:9999** 即可。

### [友情链接：Luckysheet](https://github.com/mengshukeji/Luckysheet)

