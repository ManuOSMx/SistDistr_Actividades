# Certificado SSL
(Sustituir los # ó x por la clave (minimo 6 caracteres))
compilar:
  - keytool -genkeypair -keyalg RSA -alias certificado_servidor -keystore keystore_servidor.jks -storepass ######
<img align="center" alt="img" width="40%" height="auto" src="https://github.com/ManuOSMx/SistDistr_Actividades/blob/main/certificado/img/SSL.PNG"/>
  - keytool -exportcert -keystore keystore_servidor.jks -alias certificado_servidor -rfc -file certificado_servidor.pem
  - keytool -import -alias certificado_servidor -file certificado_servidor.pem -keystore keystore_cliente.jks -storepass xxxxxx
  - javac ServidorSSL.java
  - javac ClienteSSL.java
  - java -Djavax.net.ssl.keyStore=keystore_servidor.jks -Djavax.net.ssl.keyStorePassword=###### ServidorSSL
  - java -Djavax.net.ssl.trustStore=keystore_cliente.jks -Djavax.net.ssl.trustStorePassword=xxxxxx ClienteSSL

En caso de error por interrupcion de un software en maquina local, usar Linux. 
Si necesitas instalar el WSL (Windows Subsystem for Linux), sigue las instrucciones de la pagina: https://docs.microsoft.com/es-es/windows/wsl/install-win10

JDK 16 para linux: sudo apt install openjdk-16-jre-headless 
Aunque puedes instalar el de tu preferencia. :)
