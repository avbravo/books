# Docker Alcala

```
[19:55] Huriviades Calderon
sudo apt update -y
sudo apt install docker.io -y
 
[19:56] Huriviades Calderon
curl -L "https://github.com/docker/compose/releases/download/v2.23.3/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
 
[19:56] Huriviades Calderon
chmod +x /usr/local/bin/docker-compose
 
[19:56] Huriviades Calderon
docker-compose --version


sudo docker network create --driver bridge netX --subnet=172.21.0.0/16

``
