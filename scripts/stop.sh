docker rm -f $(docker ps -aq)
docker rmi $(docker images -q)
docker system prune