"# springboot_aws"

restaurar o banco pelo terminal - aplicar migration - precisa porta 8080 liberada (apagar as tabelas antes)

Opção 1:
- mvn clean package spring-boot:run 

Opção 2:
puglin maven - flyway-maven-plugin
- mvn flyway:migrate

```yml
<plugins>
    <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
    </plugin>
        <plugin>
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-maven-plugin</artifactId>
        <configuration>
            <url>jdbc:mysql://localhost:3306/rest_with_spring_boot_erudio?useTimezone=true&serverTimezone=UTC&useSSL=false</url>
            <user>root</user>
            <password>admin123</password>
        </configuration>
        <dependencies>
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>${mysql.version}</version>
                <exclusions>
                    <exclusion>
                        <artifactId>slf4j-api</artifactId>
                        <groupId>org.slf4j</groupId>
                    </exclusion>
                </exclusions>
                <artifactId>slf4j-api</artifactId>
                <groupId>org.slf4j</groupId>  
            </dependency>
        </dependencies>
    </plugin>
</plugins>
```

Acessar json do swagger:
http://localhost:8080/v3/api-docs

Acessar a interface:
http://localhost:8080/swagger-ui/index.html



