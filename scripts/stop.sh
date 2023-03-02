docker rm -f $(docker ps -aq)
docker rmi $(docker images -q)
yes | docker system prune