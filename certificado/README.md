# Certificado SSL
(Sustituir los # ó x por la clave (minimo 6 caracteres))
compilar:
  - keytool -genkeypair -keyalg RSA -alias certificado_servidor -keystore keystore_servidor.jks -storepass ######
<img align="center" alt="img" width="25%" height="auto" src="https://scontent.fmex5-1.fna.fbcdn.net/v/t1.6435-9/221988173_144961674420259_2802470538519361387_n.png?_nc_cat=102&ccb=1-3&_nc_sid=09cbfe&_nc_eui2=AeEvNOasE3mh_hgwUCNiiPbFkwpK6CaubX6TCkroJq5tfuRAXcYoFraH6i063glDfubFrXtQ9bYNK5cvi1XQCBqZ&_nc_ohc=4pr0o9uf75IAX9az9kW&_nc_ht=scontent.fmex5-1.fna&oh=e0c8c1b67a62f75756314f0bf87b1326&oe=6130C734" />
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
