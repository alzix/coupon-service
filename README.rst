Installations
=============

- jdk 8
- STS - Eclipse based Spring IDE - https://spring.io/tools/sts/all

  #. download
  #. unzip
  #. execute `STS.exe`

Command Line
============
- build: ``gradlew build``
- test: ``gradlew test``
- start server: ``gradlew bootRun``

STS
===
import project ``File->Import...->Gradle->Existing Gradle Project``

- Test: ``Right Click on project->Run As->JUnit Test``
- Start Server: In ``Boot Dashboard`` find your server and click ``(Re)start``

Hint: if you cannot find ``Boot Dashboard``: click ``Window->Show View->Other...`` and look for ``Boot Dashboard``


Tutorials
=========
- https://spring.io/guides/gs/spring-boot/
- https://spring.io/guides/gs/rest-service/
- https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html
- https://spring.io/guides/gs/accessing-data-rest/
- https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-creating-database-queries-from-method-names/
- https://en.wikibooks.org/wiki/Java_Persistence/ManyToOne
- http://javasampleapproach.com/spring-framework/spring-data/spring-jpa-many-to-many

URL Mappings
============
- http://localhost:8080/mappings
- http://localhost:8080/admin


H2 console
==========

- WEB browser url: http://localhost:8080/h2-console/
- JDBCURL: ``jdbc:h2:mem:testdb``
- Driver Class: ``org.h2.Driver``
- Saved Settings: ``Generic H2 (Embedded)``
