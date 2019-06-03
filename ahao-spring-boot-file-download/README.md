# 简介
作为后端开发, 最经常的需求就是导出`Excel`表的需求, 本质上就是下载文件的需求.
目前我了解到有4种下载方法
1. 临时文件下载(不依赖任何`jar`)
1. `ServletOutputStream`下载(依赖`Servlet api`)
1. 返回`ResponseEntity`下载(依赖`Spring`)
1. 返回`View`下载(依赖`Spring`)

# 临时文件下载
将数据导出到`Excel`保存到本地临时文件夹`/opt/file`后, 使用`nginx`对静态资源做反向代理.
这样访问`http://192.168.0.2/1.txt`就可以直接下载`/opt/file/1.txt`文件了, 注意改成`nginx`所在主机的`IP`地址.
```
location / {
    root /opt/file;
    try_files $uri $uri/ =404;
}
```
然后写一个`Shell`定时删除`30`天前的临时文件.
```bash
#!/bin/sh
location="/opt/file/"
find $location -mtime +30 -type f | xargs rm -f
```

# 原生 ServletOutputStream 输出流下载
依赖`Servlet-api`
```java
@GetMapping(value = "/download.txt")
public void download(HttpServletResponse response) throws IOException {
    // 1. 初始化响应头
    response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition", "attachment; filename="+"hello"+".txt");
    
    // 2. 生成响应体
    byte[] buf = "你好世界".getBytes(StandardCharsets.UTF_8);
    OutputStream os = response.getOutputStream();
    os.write(buf);
    os.flush();
}
```

# Resource 下载
```java
@GetMapping(value = "/download.txt")
public ResponseEntity<Resource> download() {
    // 1. 初始化响应头
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.add("Content-Disposition", "attachment; filename="+"hello"+".txt");
    
    // 2. 生成响应体
    byte[] buf = "你好世界".getBytes(StandardCharsets.UTF_8);
    ByteArrayInputStream bais = new ByteArrayInputStream(buf);
    Resource body = new InputStreamResource(bais);

    // 3. 返回
    return new ResponseEntity<>(body, headers, HttpStatus.OK);
}
```

# View 下载
```java
@GetMapping("/download.xlsx")
public View xlsx() {
    return new AbstractXlsxView() {
        @Override
        protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
            // 1. 设置文件名和响应头
            response.setHeader("Content-Disposition", "attachment; filename=\""+"hello.xlsx"+"\"");

            // 2. 写入数据
            Sheet sheet = workbook.createSheet();
            Row header = sheet.createRow(0);
            header.createCell(col).setCellValue("你好世界");
        }
    };
}
```