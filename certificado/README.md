# Certificado SSL
(Sustituir los # por la clave (minimo 6 caracteres))
compilar:
  - keytool -genkeypair -keyalg RSA -alias certificado_servidor -keystore keystore_servidor.jks -storepass ######
  - keytool -exportcert -keystore keystore_servidor.jks -alias certificado_servidor -rfc -file certificado_servidor.pem
  - keytool -import -alias certificado_servidor -file certificado_servidor.pem -keystore keystore_cliente.jks -storepass ######
  - javac ServidorSSL.java
  - javac ClienteSSL.java
  - java -Djavax.net.ssl.keyStore=keystore_servidor.jks -Djavax.net.ssl.keyStorePassword=1234567 ServidorSSL
  - java -Djavax.net.ssl.trustStore=keystore_cliente.jks -Djavax.net.ssl.trustStorePassword=1234567 ClienteSSL
