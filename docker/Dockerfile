FROM openjdk:8-jre
MAINTAINER Alexandra Cerottini <alexandra.cerottini@heig-vd.ch>

#
# When we build the image, we copy the executable jar in the image file system. 
#
COPY MockMock-1.4.0.one-jar.jar /opt/app/mockmock.jar

#
# Expose ports
#
EXPOSE 2525 8282

#
# This is the command that is executed when the Docker container starts
#
CMD ["java", "-jar", "/opt/app/mockmock.jar", "-p", "2525"]
