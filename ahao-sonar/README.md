# 步骤
1. 下载[`sonar`](./script/install-docker.sh)
2. 访问 `http://ip:9000/admin/marketplace?search=chinese` 安装中文插件
3. 访问 `http://ip:9000/projects/create?mode=manual` 创建新项目, 记录`sonar.projectKey`、`sonar.host.url`、`sonar.login`
4. 修改[`pom.xml`](./pom.xml)的`properties`属性
5. 运行`mvn clean install`
6. 访问`http://ip:9000/dashboard?id=ahao-sonar` 就可以看到报告了
