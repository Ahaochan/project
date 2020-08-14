set JAVA_HOME=D:\Java\jdk1.8.0_241
set version=1_4
set zip_name=axis-bin-%version%
set wsdl=http://api.xxx.cn/xxxAPI/service/auditResBatchQueryService?wsdl
curl --url https://archive.apache.org/dist/ws/axis/%version%/%zip_name%.zip --output %zip_name%.zip
tar -xf %zip_name%.zip
cd axis-%version%/lib

curl --url https://repo1.maven.org/maven2/javax/mail/mail/1.4.7/mail-1.4.7.jar --output mail-1.4.7.jar
curl --url https://repo1.maven.org/maven2/javax/activation/activation/1.1/activation-1.1.jar --output activation-1.1.jar
java -Djava.ext.dirs=./ org.apache.axis.wsdl.WSDL2Java -u %wsdl%



