#!/bin/bash

# 本シェルは、以下のterraformから使用するもの
# https://github.com/namickey/spring-boot3-aws-terraform

# cd
cd /home/ec2-user/spring-boot3-train

# chmod
chmod 755 mvnw

# spring-boot:run
nohup ./mvnw spring-boot:run &
