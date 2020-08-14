set JAVA_HOME=D:\Java\jdk1.8.0_241
set version=3.3.7
set zip_name=apache-cxf-%version%
set wsdl=http://api.xxx.cn/xxxAPI/service/auditResBatchQueryService?wsdl
curl --url https://downloads.apache.org/cxf/%version%/%zip_name%.zip --output %zip_name%.zip
tar -xf %zip_name%.zip
cd %zip_name%/bin
wsdl2java -d ./target -client %wsdl%
