set version=0.12.0
:: https://thrift.apache.org/download.html
curl --url https://mirrors.bfsu.edu.cn/apache/thrift/%version%/thrift-%version%.exe --output thrift.exe
thrift.exe -gen java hello.thrift
